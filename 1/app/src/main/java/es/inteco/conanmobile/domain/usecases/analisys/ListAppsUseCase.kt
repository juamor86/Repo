package es.inteco.conanmobile.domain.usecases.analisys

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageInfo.REQUESTED_PERMISSION_GRANTED
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import android.os.Environment
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.safetynet.HarmfulAppsData
import com.google.android.gms.safetynet.SafetyNet
import es.inteco.conanmobile.R
import es.inteco.conanmobile.data.entities.Data
import es.inteco.conanmobile.domain.entities.*
import es.inteco.conanmobile.domain.usecases.GetConfigurationUseCase
import es.inteco.conanmobile.presentation.App
import es.inteco.conanmobile.utils.AnalysisConsts
import es.inteco.conanmobile.utils.ApplicationPackageUtils
import es.inteco.conanmobile.utils.Utils
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.io.*
import java.math.BigInteger
import java.net.SocketTimeoutException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateEncodingException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import javax.inject.Inject


/**
 * List apps use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class ListAppsUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {

    companion object {
        const val IP = "IP"
        const val URL = "URL"
        const val DOMAIN = "DOMAIN"
    }

    init {
        App.baseComponent.inject(this)
    }

    @Inject
    lateinit var getMaliciousAppUseCase: GetMaliciousAppUseCase

    @Inject
    lateinit var getMaliciousAPKUseCase: GetMaliciousAPKUseCase

    @Inject
    lateinit var getConfigurationUseCase: GetConfigurationUseCase

    private val privilegedAppDir = File(Environment.getRootDirectory(), "priv-app").canonicalPath
    private val packageManager: PackageManager = context.packageManager
    private val messageDigestSHA1 = MessageDigest.getInstance("SHA1")
    private val digestSHA256 = MessageDigest.getInstance("SHA-256")
    private val applicationAnalysisList = mutableListOf<ApplicationEntity>()
    private val enabledListeners = NotificationManagerCompat.getEnabledListenerPackages(context)
    private var certFactoryX509: CertificateFactory? = CertificateFactory.getInstance("X509")
    private var harmfulAppsList: MutableList<HarmfulAppsData> = ArrayList()
    private val safetyNetClient = SafetyNet.getClient(context)
    private val master = getConfigurationUseCase.buildUseCase().blockingGet().message.permissions

    lateinit var appsModules: MutableSet<String>

    override fun analyse() {
        checkSafetyNet()
        lunchRXAppsAnalysis()
    }

    private fun checkSafetyNet() {
        safetyNetClient.isVerifyAppsEnabled.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result.isVerifyAppsEnabled) {
                getHarmfulAppsList()
            } else {
                if (task.isSuccessful && task.result.isVerifyAppsEnabled) {
                    safetyNetClient.enableVerifyApps().addOnCompleteListener { _ ->
                        getHarmfulAppsList()
                    }
                }
            }
        }
    }

    private fun getHarmfulAppsList() {
        safetyNetClient.listHarmfulApps().addOnCompleteListener { task ->
            try {
                harmfulAppsList = task.result.harmfulAppsList
            } catch (ex: Exception) {
                Timber.e(ex, "SafetyNet Harmful list not available")
            }
        }
    }

    private fun checkSafetyNetApps(hash: String): String {
        var result = context.getString(R.string.app_not_into_safetynet)
        if (harmfulAppsList.isNotEmpty()) {
            for (harmfulApp in harmfulAppsList) {
                if (harmfulApp.apkSha256.toString() == hash) {
                    result = harmfulApp.apkCategory.toString()
                    harmfulAppsList.remove(harmfulApp)
                    return result
                }
            }
        }
        return result
    }

    private fun lunchRXAppsAnalysis() {
        val applicationInfoList = ApplicationPackageUtils.getInstalledApps(context)
        val flowable = Flowable.fromIterable(applicationInfoList)
        flowable.parallel().runOn(Schedulers.io()).map { processApp(it) }.sequential().toList()
            .blockingSubscribe({
                result.appsItems = it
                itemResult.notOk =
                    result.appsItems.any { applicationEntity -> applicationEntity.isMalicious == 1 || applicationEntity.criticalPermissions.isNotEmpty() }
            }, {
                result.appsItems = applicationAnalysisList
                itemResult.notOk =
                    result.appsItems.any { applicationEntity -> applicationEntity.isMalicious == 1 }
                itemResult.failed = true
                when (it) {
                    is InterruptedException -> Timber.i("Analysis interrupted")
                    else -> Timber.e("Error analysing apps - ${it.message}")
                }
            })
    }

    private fun processApp(applicationInfo: ApplicationInfo): ApplicationEntity {
        val packageName: String = applicationInfo.packageName
        val applicationEntity = ApplicationEntity()
        val installedApkFilePath = getPathInstalledApkFile(applicationInfo)
        val infoFromApk = extractInfoFromApk(installedApkFilePath)
        applicationEntity.packageId = packageName
        applicationEntity.name = getName(packageManager, packageName)
        applicationEntity.isPrivileged = isPrivileged(installedApkFilePath)
        applicationEntity.hash = getSHA256(installedApkFilePath, applicationEntity.isPrivileged)
        applicationEntity.fingerprint = getFingerprint(packageManager, packageName)
        applicationEntity.version = getVersion(packageName).toString()
        applicationEntity.origin = getOrigin(packageManager, packageName)
        applicationEntity.isSystemApplication =
            ApplicationPackageUtils.isApplicationSystem(applicationInfo)
        applicationEntity.isNotificationAccessEnabled =
            isAppNotificationAccessEnabled(applicationInfo)
        applicationEntity.safetynetResult =
            if (appsModules.contains(AnalysisConsts.APP_MODULE_SAFETYNET)) {
                checkSafetyNetApps(applicationEntity.hash)
            } else {
                context.getString(R.string.safetynet_not_enabled)
            }
        if (appsModules.contains(AnalysisConsts.APP_MODULE_PERMISSIONS)) {
            applicationEntity.permissions = getPermissions(packageName)
            applicationEntity.criticalPermissions =
                applicationEntity.permissions.filter { it.riskLevel == PermissionRiskLevel.HIGH && it.isGranted() }
            if (applicationEntity.criticalPermissions.isNotEmpty()) {
                itemResult.notOk = true
            }
        }
        if (appsModules.contains(AnalysisConsts.APP_MODULE_MALICIOUS)) {
            val maliciousType = checkMalicious(applicationEntity, applicationEntity.hash)
            maliciousType?.let { type ->
                applicationEntity.isMalicious =
                    if (type == ServiceEntity.Companion.MaliciousResultType.MALWARE) 1 else 0
                if (type == ServiceEntity.Companion.MaliciousResultType.UNKNOWN) {
                    if (appsModules.contains(AnalysisConsts.APP_MODULE_APKINFO)) {
                        applicationEntity.apkUrls = isMaliciousData(
                            applicationEntity, extractUrls(infoFromApk), URL
                        )
                        applicationEntity.apkIps = isMaliciousData(
                            applicationEntity, extractIps(infoFromApk), IP
                        )
                    }

                    if (appsModules.contains(AnalysisConsts.APP_MODULE_APIKEYS)) {
                        applicationEntity.apkApiKeys = extractApks(infoFromApk)
                    }
                }
            }
        }
        applicationAnalysisList.add(applicationEntity)

        return applicationEntity
    }


    private fun getPermissions(packageName: String): List<PermissionEntity> {
        val permissions: MutableList<PermissionEntity> = mutableListOf()
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
                .let { packageInfo ->

                    packageInfo.requestedPermissions?.indices?.forEach { index ->
                        val name = packageInfo.requestedPermissions[index]

                        val permissionEntity = master[name] ?: PermissionEntity(
                            permissionID = name
                        )
                        if ((packageInfo.requestedPermissionsFlags[index] and REQUESTED_PERMISSION_GRANTED) != 0) {
                            permissionEntity.setGranted()
                        }
                        permissions.add(permissionEntity)
                    }
                }
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e("Name not found Exception: ${e.message}")
        }
        return permissions
    }


    private fun extractInfoFromApk(targetDirectory: String): String {
        var infoResult = ""
        try {
            val file = File(targetDirectory)
            val zis = ZipInputStream(BufferedInputStream(FileInputStream(file)))
            val auxZipFile = ZipFile(file)
            try {
                var ze: ZipEntry? = null
                while (zis.nextEntry.also { it?.let { ze = it } } != null) run {
                    ze.let {
                        val file = File(targetDirectory, ze!!.name)
                        if (file.toString().contains("classes")) {
                            infoResult = Utils.convertStreamToString(auxZipFile.getInputStream(ze))
                        }
                    }
                }
            } finally {
                zis.close()
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return infoResult
    }

    private fun extractUrls(fileStr: String): List<String> {
        val urls = mutableListOf<String>()
        val regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]"
        val p: Pattern = Pattern.compile(regex)
        val matcher: Matcher = p.matcher(fileStr)
        while (matcher.find()) {
            urls.add(matcher.group())
        }
        return urls
    }

    private fun extractIps(fileStr: String): List<String> {
        val ips = mutableListOf<String>()
        val regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"
        val p: Pattern = Pattern.compile(regex)
        val matcher: Matcher = p.matcher(fileStr)
        while (matcher.find()) {
            ips.add(matcher.group())
        }
        return ips
    }

    private fun extractApks(fileStr: String): List<String> {
        val apiKeys = mutableListOf<String>()
        AnalysisConsts.API_KEYS_REGEX.forEach {
            val p: Pattern = Pattern.compile(it)
            val matcher: Matcher = p.matcher(fileStr)
            while (matcher.find()) {
                apiKeys.add(matcher.group())
            }
        }
        return apiKeys
    }

    private fun isPrivileged(installedApkFilePath: String): Int {
        val apkFile = File(installedApkFilePath)
        var isPrivileged = 0
        try {
            if (apkFile.canonicalPath.startsWith(privilegedAppDir)) {
                isPrivileged = 1
            }
        } catch (e: IOException) {
            Timber.e("Unable to access code path: ${e.message}")
        }
        return isPrivileged
    }

    private fun isAppNotificationAccessEnabled(appInfo: ApplicationInfo): Int {
        enabledListeners.forEach {
            val itAppInfo = packageManager.getApplicationInfo(it, PackageManager.GET_META_DATA)
            if (itAppInfo.packageName.equals(appInfo.packageName)) {
                if ((itAppInfo.flags and ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM) {
                    return 1
                }
                return 0
            }
        }
        return 0
    }

    private fun getFingerprint(packageManager: PackageManager, packageName: String): String {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e("name not found: ${e.message}")
        }
        val signatures: Array<Signature> = packageInfo!!.signatures
        val cert: ByteArray = signatures[0].toByteArray()
        val input: InputStream = ByteArrayInputStream(cert)
        var certificate: X509Certificate? = null
        try {
            certificate = certFactoryX509!!.generateCertificate(input) as X509Certificate
        } catch (e2: CertificateException) {
            Timber.e("certificate factory not found: ${e2.message}")
        }
        var hexString: String = ""
        try {
            val publicKey = messageDigestSHA1.digest(certificate!!.encoded)
            hexString = publicKey.toHexString()
        } catch (e3: NoSuchAlgorithmException) {
            Timber.e("no such algorithm exception: ${e3.message}")
        } catch (e4: CertificateEncodingException) {
            Timber.e("certificate encoding not found: ${e4.message}")
        }
        return hexString
    }

    /**
     * To hex string
     *
     * @return
     */
    fun ByteArray.toHexString(): String {
        return this.joinToString(":") {
            String.format("%02x", it).uppercase()
        }
    }

    private fun getVersion(packageName: String): Long {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionCode: Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode.toLong()
        }
        return versionCode
    }


    private fun getOrigin(packageManager: PackageManager, packageName: String): String {
        var origin: String = context.getString(R.string.origin_not_found)
        try {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                if (!(packageManager.getInstallerPackageName(packageName).isNullOrEmpty())) {
                    origin = packageManager.getInstallerPackageName(packageName).toString()
                }
                origin
            } else {
                if (!(packageManager.getInstallSourceInfo(packageName).initiatingPackageName.isNullOrEmpty())) {
                    origin =
                        packageManager.getInstallSourceInfo(packageName).initiatingPackageName.toString()
                }
                origin
            }
        } catch (e: Throwable) {
            Timber.e(e)
        }
        return origin
    }

    private fun getSHA256(installedApkFilePath: String, isPrivileged: Int): String {
        var sha256 = ""
        if (isPrivileged == 1) {
            return sha256
        }
        val apkFile = File(installedApkFilePath)
        if (apkFile.exists()) {
            sha256 = getShaFile(apkFile)
        }
        return sha256
    }

    private fun getPathInstalledApkFile(applicationInfo: ApplicationInfo): String {
        var installedApkFilePath = ""
        try {
            val apkFile = File(applicationInfo.publicSourceDir)
            if (apkFile.exists()) {
                installedApkFilePath = apkFile.absolutePath
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e("getAllInstalledApkFile --> Name not found exception: ${e.message}")
        }
        return installedApkFilePath
    }

    private fun getShaFile(file: File?): String {
        var output = ""
        var inputStream: InputStream? = null
        try {
            inputStream = FileInputStream(file)
            val buffer = ByteArray(8192)
            var read: Int
            while (inputStream.read(buffer).also { read = it } > 0) {
                digestSHA256.update(buffer, 0, read)
            }
            val sha256sum = digestSHA256.digest()
            val bigInt = BigInteger(1, sha256sum)
            output = bigInt.toString(16)
            output = String.format("%64s", output).replace(' ', '0')
        } catch (e: NoSuchAlgorithmException) {
            Timber.e("getFileSHA256 --> not such algorithm: ${e.message}")
        } catch (e1: FileNotFoundException) {
            Timber.e("getFileSHA256 --> file Not found Exception: ${e1.message}")
        } catch (e2: IOException) {
            Timber.e("getFileSHA256 --> IOException: ${e2.message}")
        } catch (e3: OutOfMemoryError) {
            Timber.e("getFileSHA256 --> OutOfMemoryError: ${e3.message}")
        } catch (e4: UndeliverableException) {
            Timber.e("getFileSHA256 --> UndeliverableError: ${e4.message}")
        } finally {
            try {
                inputStream?.close()
            } catch (e4: IOException) {
                Timber.e("getFileSHA256 --> IOException: ${e4.message}")
            }
        }
        return output
    }

    private fun getName(packageManager: PackageManager, packageName: String): String {
        var name = ""
        try {
            name = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                .loadLabel(packageManager).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.message.let {
                Timber.e(it)
            }
        }
        return name
    }

    private fun checkMalicious(
        applicationEntity: ApplicationEntity, hash: String
    ): ServiceEntity.Companion.MaliciousResultType? {
        if (hash.isNotEmpty()) {
            try {
                getMaliciousAppUseCase.buildUseCase(GetMaliciousAppUseCase.Params(hash))
                    .blockingGet().message.let {
                        applicationEntity.hashAnalysis = it.services
                        val service = it.services.first()
                        return service.maliciousType
                    }
            } catch (e: RuntimeException) {
                Timber.i("Analysis interrupted")
            } catch (e: Exception) {
                Timber.e("Error analyzing apps - ${e.message}")
            }
        }

        return null
    }

    private fun isMaliciousData(
        applicationEntity: ApplicationEntity, suspiciousData: List<String>, type: String
    ): List<OtherApkEntity> {
        val others = mutableListOf<OtherApkEntity>()
        val domains = mutableListOf<OtherApkEntity>()
        val dataList = mutableListOf<Data>()

        if (suspiciousData.isNotEmpty()) {
            suspiciousData.forEach {
                val data = Data("", "")
                data.type = type
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val encodeData = Base64.getEncoder().encodeToString(it.toByteArray())
                    data.data = encodeData
                } else {
                    val encodeData = android.util.Base64.encodeToString(
                        it.toByteArray(), android.util.Base64.DEFAULT
                    )
                    data.data = encodeData
                }
                dataList.add(data)
            }
            getMaliciousAPKUseCase.buildUseCase(GetMaliciousAPKUseCase.Params(dataList))
                .blockingSubscribe({
                    it.message.datas.forEach { sAnalysis ->
                        if (sAnalysis.type == DOMAIN) {
                            domains.add(OtherApkEntity(sAnalysis.data, sAnalysis.services))
                        } else {
                            others.add(OtherApkEntity(sAnalysis.data, sAnalysis.services))
                        }
                    }
                    applicationEntity.apkDomains = domains
                }, {
                    when (it) {
                        is SocketTimeoutException -> Timber.i("Timeout")
                        else -> Timber.e("Error analyzing apps - ${it.message}")
                    }
                })
        }
        return others
    }
}