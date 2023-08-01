package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.PostAnalysisResultMessage
import es.inteco.conanmobile.domain.entities.PostAnalysisResultMessageEntity

/**
 * Post analysis result message mapper
 *
 * @constructor Create empty Post analysis result message mapper
 */
class PostAnalysisResultMessageMapper {
    companion object {
        fun convert(model: PostAnalysisResultMessage) = PostAnalysisResultMessageEntity(
            acknowledge = model.acknowledge
        )
    }
}