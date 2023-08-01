package es.inteco.conanmobile.data.entities

/**
 * Malicious app data
 *
 * @property timestamp
 * @property status
 * @property statusMessage
 * @property message
 * @property path
 * @constructor Create empty Malicious app data
 */
data class MaliciousAppData (
    val timestamp: String,
    val status: Long,
    val statusMessage: String,
    val message: MaliciousAppMessage,
    val path: String
)

/**
 * Malicious app message
 *
 * @property hash
 * @property services
 * @constructor Create empty Malicious app message
 */
data class MaliciousAppMessage (
    val hash: String,
    val services:List<Service>
)