package es.inteco.conanmobile.data.entities

import com.google.gson.annotations.SerializedName
import es.inteco.conanmobile.domain.entities.PermissionEntity

/**
 * Analysis result request data
 *
 * @property configurationVersion
 * @property result
 * @constructor Create empty Analysis result request data
 */
data class AnalysisResultRequestData(
    @SerializedName("configuration_version") val configurationVersion: Long,

    val result: Result
)

/**
 * Result
 *
 * @property date
 * @property name
 * @property id
 * @property device
 * @property system
 * @property applications
 * @constructor Create empty Result
 */
data class Result(
    val date: String,
    val name: String,
    val id: String,
    val device: List<Device>,
    val system: SystemClass,
    val applications: List<Application>
)

data class Application(
    val hash: String,
    val name: String,
    @SerializedName("name_hash")
    val nameHash: String,
    val version: String,
    val origin: String,
    val fingerprint: String,
    val isPrivileged: String,
    val isAllowedUnknownSources: String,
    val isSystemApplication: String,
    val isNotificationAccessEnabled: String,
    @SerializedName("packageId")
    val packageID: String,
    val safetynet: String,
    val permissions: List<String>,
    val permissionsAPK: List<PermissionEntity>,
    @SerializedName("hash_analysis")
    val hashAnalysis: ServiceList,
    @SerializedName("domains_analysis")
    val domainsAnalysis: List<OtherApkData>,
    @SerializedName("ips_analysis")
    val ipsAnalysis: List<OtherApkData>,
    @SerializedName("urls_analysis")
    val urlsAnalysis: List<OtherApkData>
)

data class ServiceList(val services: List<Service>)

data class Service(
    val service: String, val result: String, val analysisDate: String
)

data class Device(
    val code: String, val result: String
)

data class SystemClass(
    val id: String,
    val brand: String,
    val manufacturer: String,
    val model: String,
    val version: Version,
    val language: String,
    val product: String,
    val isIPBotnet: String
)

data class Version(
    @SerializedName("base_operating_system")
    val baseOperatingSystem: String,
    val api: String,
    @SerializedName("last_security_fix")
    val lastSecurityFix: String
)