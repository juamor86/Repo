package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.ConfigurationData
import es.inteco.conanmobile.data.entities.RegisteredDeviceData
import es.inteco.conanmobile.domain.entities.ConfigurationEntity
import es.inteco.conanmobile.domain.entities.RegisteredDeviceEntity

/**
 * Configuration mapper
 *
 * @constructor Create empty Configuration mapper
 */
class ConfigurationMapper {
    companion object {
        fun convert(model: ConfigurationData) = with(model) {
            ConfigurationEntity(
                timestamp = timestamp,
                status = status,
                statusMessage = statusMessage,
                message = MessageMapper.convert(message),
                path = path
            )
        }
    }
}