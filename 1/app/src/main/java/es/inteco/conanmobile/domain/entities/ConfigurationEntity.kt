package es.inteco.conanmobile.domain.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import es.inteco.conanmobile.utils.AnalysisConsts
import kotlinx.android.parcel.Parcelize

/**
 * Configuration entity
 *
 * @property timestamp
 * @property status
 * @property statusMessage
 * @property message
 * @property path
 * @constructor Create empty Configuration entity
 */
@Parcelize
data class ConfigurationEntity(
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("status") val status: Long,
    @SerializedName("statusMessage") val statusMessage: String,
    @SerializedName("message") val message: MessageEntity,
    @SerializedName("path") val path: String
) : Parcelable

/**
 * Message entity
 *
 * @property id
 * @property version
 * @property expirationDate
 * @property analysis
 * @property administration
 * @property about
 * @property legal
 * @property maliciousAppsDescription
 * @property permissions
 * @constructor Create empty Message entity
 */
@Parcelize
data class MessageEntity(
    @SerializedName("id") val id: String,
    @SerializedName("version") val version: String,
    @SerializedName("expiration_date") val expirationDate: String,
    @SerializedName("analysis") val analysis: List<AnalysisEntity>,
    @SerializedName("administration") val administration: List<AdministrationEntity>,
    @SerializedName("about") val about: String = "",
    @SerializedName("legal") val legal: String = "",
    @SerializedName("maliciousAppsDescription") val maliciousAppsDescription: String = "",
    @SerializedName("permissions") val permissions: Map<String, PermissionEntity>
) : Parcelable {
    val formattedTermsAndConditions = "${legal}<br><br>${about}"
}

/**
 * Analysis entity
 *
 * @property id
 * @property names
 * @property descriptions
 * @property applicationModules
 * @property deviceModules
 * @property systemModules
 * @constructor Create empty Analysis entity
 */
@Parcelize
data class AnalysisEntity(
    @SerializedName("id") val id: String?,
    @SerializedName("names") var names: List<AdministrationEntity> = listOf(),
    @SerializedName("descriptions") val descriptions: List<AdministrationEntity> = listOf(),
    @SerializedName("application_modules") val applicationModules: List<ModuleEntity> = listOf(),
    @SerializedName("device_modules") val deviceModules: List<ModuleEntity> = listOf(),
    @SerializedName("system_modules") val systemModules: List<ModuleEntity> = listOf(),
) : Parcelable

/**
 * Administration entity
 *
 * @property key
 * @property value
 * @constructor Create empty Administration entity
 */
@Parcelize
data class AdministrationEntity(
    @SerializedName("key") val key: String, @SerializedName("value") val value: String
) : Parcelable

/**
 * Module entity
 *
 * @property names
 * @property descriptions
 * @property valoration
 * @property code
 * @property showResult
 * @property comparable
 * @property assessment
 * @property type
 * @constructor Create empty Module entity
 */
@Parcelize
data class ModuleEntity(
    @SerializedName("names") val names: List<AdministrationEntity>,
    @SerializedName("descriptions") val descriptions: String,
    @SerializedName("valoration") val valoration: String,
    @SerializedName("code") val code: String,
    @SerializedName("show_result") val showResult: Boolean,
    @SerializedName("comparable") val comparable: Boolean,
    @SerializedName("assessment") var assessment: AssessmentEntity,
    @SerializedName("type") val type: AnalysisType
) : Parcelable {

    val shouldLaunchAction = !AnalysisConsts.excludedLaunchActionAnalysis.contains(code)

    companion object {
        fun getType(type: String): AnalysisType = when (type.uppercase()) {
            "APPLICATION" -> AnalysisType.APPLICATION
            "SETTING" -> AnalysisType.SETTING
            "SYSTEM" -> AnalysisType.SYSTEM
            else -> AnalysisType.SETTING
        }
    }

    /**
     * Analysis type
     *
     * @constructor Create empty Analysis type
     */
    enum class AnalysisType {
        /**
         * Application
         *
         * @constructor Create empty Application
         */
        APPLICATION,

        /**
         * System
         *
         * @constructor Create empty System
         */
        SYSTEM,

        /**
         * Setting
         *
         * @constructor Create empty Setting
         */
        SETTING
    }
}

/**
 * Name entity
 *
 * @property key
 * @property value
 * @constructor Create empty Name entity
 */
@Parcelize
data class NameEntity(
    @SerializedName("key") val key: String, @SerializedName("value") val value: String
) : Parcelable

/**
 * Assessment entity
 *
 * @property criticality
 * @property reason
 * @constructor Create empty Assessment entity
 */
@Parcelize
data class AssessmentEntity(
    var criticality: String = "", val reason: List<NameEntity> = listOf()
) : Parcelable

/**
 * Permission entity
 *
 * @property permissionID
 * @property description
 * @property granted
 * @property riskLevel
 * @constructor Create empty Permission entity
 */
@Parcelize
data class PermissionEntity(
    @SerializedName("permissionID") val permissionID: String = "",
    @SerializedName("permissionDescription") val description: String = "",
    @SerializedName("permissionType") var granted : String = "Not granted",
    @SerializedName("permissionRiskLevel") val riskLevel: PermissionRiskLevel = PermissionRiskLevel.OTHERS
) : Parcelable {
    /**
     * Set granted
     *
     */
    fun setGranted() {
        granted = "Granted"
    }

    /**
     * Is granted
     *
     * @return
     */
    fun isGranted(): Boolean = granted == "Granted"
}

/**
 * Permission risk level
 *
 * @property description
 * @constructor Create empty Permission risk level
 */
enum class PermissionRiskLevel(val description: String) {
    /**
     * Low
     *
     * @constructor Create empty Low
     */
    LOW("Bajo"),

    /**
     * Medium
     *
     * @constructor Create empty Medium
     */
    MEDIUM("Medio"),

    /**
     * High
     *
     * @constructor Create empty High
     */
    HIGH("Alto"),

    /**
     * Others
     *
     * @constructor Create empty Others
     */
    OTHERS("Otros");

    override fun toString(): String {
        return this.description
    }
}

