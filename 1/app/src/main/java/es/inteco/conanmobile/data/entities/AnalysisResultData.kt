package es.inteco.conanmobile.data.entities

/**
 * Post analysis result data
 *
 * @property timestamp
 * @property status
 * @property statusMessage
 * @property message
 * @property path
 * @constructor Create empty Post analysis result data
 */
data class PostAnalysisResultData (
    val timestamp: String,
    val status: Long,
    val statusMessage: String,
    val message: PostAnalysisResultMessage,
    val path: String
)

/**
 * Post analysis result message
 *
 * @property acknowledge
 * @constructor Create empty Post analysis result message
 */
data class PostAnalysisResultMessage (
    val acknowledge: String
)