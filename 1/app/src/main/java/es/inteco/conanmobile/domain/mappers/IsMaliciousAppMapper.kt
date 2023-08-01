package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.MaliciousAppData
import es.inteco.conanmobile.domain.entities.MaliciousAppEntity

/**
 * Is malicious app mapper
 *
 * @constructor Create empty Is malicious app mapper
 */
class IsMaliciousAppMapper {
    companion object {
        fun convert(model: MaliciousAppData) = with(model){
            MaliciousAppEntity(
                timestamp = timestamp,
                status = status,
                statusMessage = statusMessage,
                message = MaliciousAppMessageMapper.convert(message),
                path = path
            )
        }
    }
}