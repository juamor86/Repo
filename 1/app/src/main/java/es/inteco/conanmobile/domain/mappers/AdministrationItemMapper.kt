package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.AdministrationItem
import es.inteco.conanmobile.domain.entities.AdministrationEntity

/**
 * Administration item mapper
 *
 * @constructor Create empty Administration item mapper
 */
class AdministrationItemMapper {
    companion object {
        fun convert(administrations: List<AdministrationItem>) = administrations.map { convert(it) }

        fun convertToString(administrations: List<AdministrationItem>): String =
            if (administrations.isNotEmpty()) administrations[0].value else ""

        fun convert(model: AdministrationItem) = AdministrationEntity(
            key = model.key, value = model.value
        )
    }
}