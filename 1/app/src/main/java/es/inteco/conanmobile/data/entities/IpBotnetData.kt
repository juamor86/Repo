package es.inteco.conanmobile.data.entities

data class IpBotnetData (
    val ip: String,
    val error: String,
    val evidences: List<Evidence>
)

data class Evidence (
    val name: String,
    val threatCode: String,
    val operatingSystems: List<OperatingSystem>,
    val descriptionURL: String,
    val timestamp: String
)

data class OperatingSystem (
    val operatingSystem: String,
    val disinfectURL: List<String>
)