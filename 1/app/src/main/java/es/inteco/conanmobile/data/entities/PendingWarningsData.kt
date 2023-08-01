package es.inteco.conanmobile.data.entities

/**
 * Pending warnings data
 *
 * @property timestamp
 * @property status
 * @property statusMessage
 * @property message
 * @property path
 * @constructor Create empty Pending warnings data
 */
data class PendingWarningsData(
    val timestamp: String,
    val status: Long,
    val statusMessage: String,
    val message: PendingWarningsMessageData,
    val path: String
)

/**
 * Pending warnings message data
 *
 * @property haveNotifications
 * @constructor Create empty Pending warnings message data
 */
data class PendingWarningsMessageData(
    val haveNotifications: Boolean,
)