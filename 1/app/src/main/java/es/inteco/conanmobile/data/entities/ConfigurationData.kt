package es.inteco.conanmobile.data.entities

import com.google.gson.annotations.SerializedName
import es.inteco.conanmobile.domain.entities.AssessmentEntity
import java.time.LocalDateTime

/**
 * Configuration data
 *
 * @property timestamp
 * @property status
 * @property statusMessage
 * @property message
 * @property path
 * @constructor Create empty Configuration data
 */
data class ConfigurationData(
    @SerializedName("timestamp") var timestamp: String,
    @SerializedName("status") var status: Long,
    @SerializedName("statusMessage") var statusMessage: String,
    @SerializedName("message") var message: MessageItem,
    @SerializedName("path") var path: String
) {
    constructor() : this(
        LocalDateTime.parse(LocalDateTime.now().minusDays(100).toString()).toString(),
        0,
        "",
        MessageItem(),
        ""
    )
}

/**
 * Message item
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
 * @constructor Create empty Message item
 */
data class MessageItem(
    @SerializedName("id") var id: String,
    @SerializedName("version") var version: String,
    @SerializedName("expiration_date") var expirationDate: String,
    @SerializedName("analysis") var analysis: List<AnalysisItem>,
    @SerializedName("administration") var administration: List<AdministrationItem>,
    @SerializedName("about") var about: List<AdministrationItem>,
    @SerializedName("legal") var legal: List<AdministrationItem>,
    @SerializedName("maliciousAppsDescription") val maliciousAppsDescription: List<AdministrationItem>,
    @SerializedName("permissions") var permissions: List<PermissionsItem>
) {
    constructor() : this("", "", "", emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList())
}

/**
 * Analysis item
 *
 * @property id
 * @property names
 * @property descriptions
 * @property applicationModules
 * @property deviceModules
 * @property systemModules
 * @constructor Create empty Analysis item
 */
data class AnalysisItem(
    @SerializedName("id") var id: String,
    @SerializedName("names") var names: List<AdministrationItem>,
    @SerializedName("descriptions") var descriptions: List<AdministrationItem>,
    @SerializedName("application_modules") var applicationModules: List<ModuleItem>,
    @SerializedName("device_modules") var deviceModules: List<ModuleItem>,
    @SerializedName("system_modules") var systemModules: List<ModuleItem>
) {
    constructor() : this("", emptyList(), emptyList(), emptyList(), emptyList(), emptyList())
}

/**
 * Administration item
 *
 * @property key
 * @property value
 * @constructor Create empty Administration item
 */
data class AdministrationItem(
    @SerializedName("key") var key: String, @SerializedName("value") var value: String
) {
    constructor() : this("", "")
}

/**
 * Module item
 *
 * @property names
 * @property descriptions
 * @property valoration
 * @property code
 * @property showResult
 * @property comparable
 * @property assessment
 * @property type
 * @constructor Create empty Module item
 */
data class ModuleItem(
    @SerializedName("names") var names: List<AdministrationItem>,
    @SerializedName("descriptions") var descriptions: List<AdministrationItem>,
    @SerializedName("valoration") var valoration: String,
    @SerializedName("code") var code: String,
    @SerializedName("show_result") var showResult: Boolean,
    @SerializedName("comparable") var comparable: Boolean,
    @SerializedName("assessment") var assessment: AssessmentEntity,
    @SerializedName("type") var type: String
) {
    constructor() : this(
        emptyList(), emptyList(), "", "", false, false, AssessmentEntity("NEUTRAL"), ""
    )
}

/**
 * Permissions item
 *
 * @property permissionID
 * @property permissionDescriptions
 * @property permissionRiskLevels
 * @constructor Create empty Permissions item
 */
data class PermissionsItem(
    @SerializedName("permissionID") var permissionID: String,
    @SerializedName("permissionDescription") var permissionDescriptions: List<AdministrationItem>,
    @SerializedName("permissionRiskLevel") var permissionRiskLevels: String
) {
    constructor() : this("", emptyList(), "")
}
