package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.OperatingSystem
import es.inteco.conanmobile.domain.entities.OperatingSystemEntity

/**
 * Operating system entity mapper
 *
 * @constructor Create empty Operating system entity mapper
 */
class OperatingSystemEntityMapper {
    companion object{
        fun convert(operatingSystems: List<OperatingSystem>) =
            operatingSystems.map {convert(it)}

        fun convert(model: OperatingSystem) = OperatingSystemEntity(
            operatingSystem = model.operatingSystem,
            disinfectURL = if(model.disinfectURL.isNullOrEmpty()) emptyList() else model.disinfectURL
        )
    }
}