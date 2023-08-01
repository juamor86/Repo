package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.Evidence
import es.inteco.conanmobile.domain.entities.EvidenceEntity

/**
 * Evidence entity mapper
 *
 * @constructor Create empty Evidence entity mapper
 */
class EvidenceEntityMapper {
    companion object {
        fun convert(evidences: List<Evidence>) =
            evidences.map { convert(it) }

        fun convert(model: Evidence) = EvidenceEntity(
            name = model.name,
            threatCode = model.threatCode,
            operatingSystems = OperatingSystemEntityMapper.convert(model.operatingSystems),
            descriptionURL = if(model.descriptionURL.isNullOrEmpty()) "" else model.descriptionURL,
            timestamp = model.timestamp
        )
    }
}