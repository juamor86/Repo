package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.PostAnalysisResultData
import es.inteco.conanmobile.domain.entities.PostAnalysisResultEntity

/**
 * Post analysis result mapper
 *
 * @constructor Create empty Post analysis result mapper
 */
class PostAnalysisResultMapper {
    companion object {
        fun convert(model: PostAnalysisResultData) = with(model){
            PostAnalysisResultEntity(
                timestamp = timestamp,
                status = status,
                statusMessage = statusMessage,
                message = PostAnalysisResultMessageMapper.convert(message),
                path = path
            )
        }
    }
}