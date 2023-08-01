package es.inteco.conanmobile.data.entities

/**
 * Warnings data
 *
 * @property message
 * @property path
 * @property status
 * @property statusMessage
 * @property timestamp
 * @constructor Create empty Warnings data
 */
data class WarningsData(
    val message: List<MessageData> = listOf(),
    val path: String = "",
    val status: Int = 0,
    val statusMessage: String = "",
    val timestamp: Long = 0
)

/**
 * Message data
 *
 * @property creationDate
 * @property description
 * @property id
 * @property importance
 * @property title
 * @constructor Create empty Message data
 */
data class MessageData(
    val creationDate: Long = 0,
    val description: List<KeyValueData> = emptyList<KeyValueData>(),
    val id: String = "",
    val importance: String = "",
    val title: List<KeyValueData> = emptyList<KeyValueData>()
)