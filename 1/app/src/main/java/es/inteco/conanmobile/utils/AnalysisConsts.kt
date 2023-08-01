package es.inteco.conanmobile.utils

import es.inteco.conanmobile.domain.usecases.analisys.*
import kotlin.reflect.jvm.jvmName

/**
 * Analysis consts
 *
 * @constructor Create empty Analysis consts
 */
class AnalysisConsts {
    companion object {
        private val useCaseMap: HashMap<String, String> = hashMapOf(
            "COM-001" to RootedDeviceUseCase::class.jvmName,
            "COM-002" to CheckADBUseCase::class.jvmName,
            "COM-003" to UnknowSourcesUseCase::class.jvmName,
            "COM-004" to ShowPasswordsUseCase::class.jvmName,
            "COM-005" to IsDeviceSecureUseCase::class.jvmName,
            "COM-006" to LockTimeUseCase::class.jvmName,
            "COM-007" to InsecureWifiUseCase::class.jvmName,
            "COM-008" to InsecureBluetoothDevicesUseCase::class.jvmName,
            "COM-010" to PackageVerifiedUseCase::class.jvmName,
            "COM-011" to WifiTetheringUseCase::class.jvmName,
            "COM-012" to BluetoothTetheringUseCase::class.jvmName,
            "COM-013" to HostFileExistUseCase::class.jvmName,
            "COM-014" to HiddenNotificationsUseCase::class.jvmName,
            "COM-016" to BootLoaderUseCase::class.jvmName,
            "COM-017" to AppNotificationsUseCase::class.jvmName,
            "COM-018" to ActiveScreenUseCase::class.jvmName,
            "COM-019" to DangerousDeviceNameUseCase::class.jvmName,
            "COM-025" to IsBluetoothEnabledUseCase::class.jvmName,
            "COM-026" to OpenNetworkNotificationsUseCase::class.jvmName,
            "COM-027" to AutomaticOpenWifiUseCase::class.jvmName,
            "COM-028" to IsWifiEnabledUseCase::class.jvmName,
            "COM-029" to NfcAlwaysActiveUseCase::class.jvmName,
            "COM-030" to AndroidBeamUseCase::class.jvmName,
            "COM-202" to CheckLanguageUseCase::class.jvmName,
            "COM-027" to AutomaticOpenWifiUseCase::class.jvmName,
            "COM-203" to CheckDeviceIPUseCase::class.jvmName,
            "COM-201" to GetDeviceInfoUseCase::class.jvmName,
            "COM-102" to ListAppsUseCase::class.jvmName
        )


        private const val ROOTED_DEVICE = "COM-001"
        internal const val APP_MODULE_WIFI = "COM-007"
        private const val HOST_FILE_EXIST = "COM-013"
        private const val LIST_APPS = "COM-102"
        internal const val APP_MODULE_PERMISSIONS = "COM-103"
        internal const val APP_MODULE_SAFETYNET = "COM-104"
        internal const val APP_MODULE_APKINFO = "COM-106"
        internal const val APP_MODULE_MALICIOUS = "COM-107"
        internal const val APP_MODULE_APIKEYS = "COM-109"
        private const val DEVICE_INFO = "COM-201"
        private const val CHECK_LANGUAGE = "COM-202"
        private const val CHECK_DEVICE_IP = "COM-203"

        internal const val BLUETOOTHPAN_CLASS = "android.bluetooth.BluetoothPan"
        internal const val IS_TETHERING_ON = "isTetheringOn"
        internal const val LOCK_TIME_AFTER_SCREEN_LOCK = "lock_screen_lock_after_timeout"

        val excludedLaunchActionAnalysis =
            listOf(
                ROOTED_DEVICE,
                HOST_FILE_EXIST,
                LIST_APPS,
                DEVICE_INFO,
                CHECK_LANGUAGE,
                CHECK_DEVICE_IP
            )

        fun getUseCaseClass(id: String): String? = useCaseMap[id]

        internal val API_KEYS_REGEX = listOf(
            "[1-9][ 0-9]+-[0-9a-zA-Z]{40}",
            "/(^|[^@\\w])@(\\w{1,15})\\b/",
            "EAACEdEose0cBA[0-9A-Za-z]+",
            "[A-Za-z0-9]{125}",
            "[0-9a-fA-F]{7}\\.[0-9a-fA-F]{32}",
            "(?:@)([A-Za-z0-9_](?:(?:[A-Za-z0-9_]|(?:\\.(?!\\.))){0,28}(?:[A-Za-z0-9_]))?)",
            "(?:#)([A-Za-z0-9_](?:(?:[A-Za-z0-9_]|(?:\\.(?!\\.))){0,28}(?:[A-Za-z0-9_]))?)",
            "AIza[0-9A-Za-z-_]{35}",
            "[0-9a-zA-Z\\-_]{24}",
            "4/[0-9A-Za-z\\-_]+",
            "1/[0-9A-Za-z\\-_]{43}|1/[0-9A-Za-z\\-_]{64}",
            "ya29\\.[0-9A-Za-z\\-_]+",
            "[A-Za-z0-9_]{255}",
            "[0-9a-zA-Z_][5,31]",
            "R_[0-9a-f]{32}",
            "sk_live_[0-9a-z]{32}",
            "sk_live_(0-9a-zA-Z){24}",
            "sk_live_[0-9a-zA-Z]{24}",
            "sqOatp-[0-9A-Za-z\\-_]{22}",
            "q0csp-[ 0-9A-Za-z\\-_]{43}",
            "access_token,production\$[0-9a-z]{161}[0-9a,]{32}",
            "amzn\\.mws\\.[0-9a-f]{8}-[0-9a-f]{4}-10-9a-f1{4}-[0-9a,]{4}-[0-9a-f]{12}",
            "55[0-9a-fA-F]{32}",
            "key-[0-9a-zA-Z]{32}",
            "[0-9a-f]{32}-us[0-9]{1,2}",
            "xoxb-[0-9]{11}-[0-9]{11}-[0-9a-zA-Z]{24}",
            "xoxp-[0-9]{11}-[0-9]{11}-[0-9a-zA-Z]{24}",
            "xoxe.xoxp-1-[0-9a-zA-Z]{166}",
            "xoxe-1-[0-9a-zA-Z]{147}",
            "T[a-zA-Z0-9_]{8}/B[a-zA-Z0-9_]{8}/[a-zA-Z0-9_]{24}",
            "AKIA[0-9A-Z]{16}",
            "[0-9a-zA-Z/+]{40}",
            "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}",
            "[A-Za-z0-9_]{21}--[A-Za-z0-9_]{8}",
            "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}",
            "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}"
        )
    }
}