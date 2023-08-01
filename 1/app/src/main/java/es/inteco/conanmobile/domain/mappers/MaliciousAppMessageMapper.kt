package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.MaliciousAppMessage
import es.inteco.conanmobile.data.entities.Service
import es.inteco.conanmobile.domain.entities.MaliciousAppMessageEntity
import es.inteco.conanmobile.domain.entities.ServiceEntity

/**
 * Malicious app message mapper
 *
 * @constructor Create empty Malicious app message mapper
 */
class MaliciousAppMessageMapper {
    companion object {
        fun convert(model: MaliciousAppMessage) = MaliciousAppMessageEntity(
            hash = model.hash,
            services = model.services.map { convert(it) }
        )

        fun convert(model:Service) = ServiceEntity(model.service, model.result, model.analysisDate)
    }
}