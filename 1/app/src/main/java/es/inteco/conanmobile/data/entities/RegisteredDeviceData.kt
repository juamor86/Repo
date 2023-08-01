package es.inteco.conanmobile.data.entities

/**
 * Registered device data
 *
 * @property timestamp
 * @property status
 * @property statusMessage
 * @property message
 * @property path
 * @constructor Create empty Registered device data
 */
data class RegisteredDeviceData(
    val timestamp: Long,
    val status: Long,
    val statusMessage: String,
    val message: MessageKey,
    val path: String
)

/**
 * Message key
 *
 * @property idTerminal
 * @property key
 * @constructor Create empty Message key
 */
data class MessageKey (
    val idTerminal: String,
    val key: String
)