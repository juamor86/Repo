package es.inteco.conanmobile.domain.usecases.analisys

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.safetynet.SafetyNetApi
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import es.inteco.conanmobile.BuildConfig
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import okhttp3.internal.notify
import okhttp3.internal.wait
import org.json.JSONObject
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.SecureRandom

/**
 * Boot loader use case
 *
 * @constructor
 *
 * @param context
 * @param analysisItem
 * @param result
 */
class BootLoaderUseCase(
    context: Context, analysisItem: ModuleEntity, result: AnalysisResultEntity
) : BaseAnalysisUseCase(context, analysisItem, result) {

    private val mRandom = SecureRandom()
    private val safetyNetClient = SafetyNet.getClient(context)

    override fun analyse() {
        if(isGooglePlayServicesAvailable()) {
            synchronized(this) {
                val nonceData = "Conan Safety Net: " + System.currentTimeMillis()
                val nonce = getRequestNonce(nonceData)
                safetyNetClient.attest(nonce!!, BuildConfig.API_KEY).addOnCompleteListener { task ->
                    synchronized(this@BootLoaderUseCase) {
                        if (task.isSuccessful) {
                            process(task.result.jwsResult)
                        }
                        this@BootLoaderUseCase.notify()
                    }
                }
                wait()
            }
        }
    }

    private fun process(signedAttestationStatement: String?) {
        val jwsData = extractJwsData(signedAttestationStatement)
        if (jwsData == null) {
            Timber.e("Failure: Failed to parse and verify the attestation statement.")
            return
        }
        val safetyNetResult = JSONObject(String(jwsData))
        val basicIntegrity = safetyNetResult.opt("basicIntegrity") as Boolean
        val ctsProfileMatch = safetyNetResult.opt("ctsProfileMatch") as Boolean
        result.device.isBootloaderUnlocked = if (!ctsProfileMatch && basicIntegrity) 1 else 0
        itemResult.notOk = result.device.isBootloaderUnlocked == 1
    }

    private fun isGooglePlayServicesAvailable(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
        return resultCode == ConnectionResult.SUCCESS
    }

    private fun getRequestNonce(data: String): ByteArray? {
        val byteStream = ByteArrayOutputStream()
        val bytes = ByteArray(24)
        mRandom.nextBytes(bytes)
        try {
            byteStream.write(bytes)
            byteStream.write(data.toByteArray())
        } catch (e: IOException) {
            return null
        }
        return byteStream.toByteArray()
    }

    // Extracts the data part from a JWS signature.
    private fun extractJwsData(jws: String?): ByteArray? {
        // The format of a JWS is: <Base64url encoded header>.<Base64url encoded JSON data>.<Base64url encoded signature>
        val parts = jws?.split("[.]".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
        if (parts?.size != 3) {
            Timber.e("Failure: Illegal JWS signature format")
            return null
        }
        return android.util.Base64.decode(parts[1], android.util.Base64.DEFAULT)
    }
}