package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.ConfigurationData
import es.inteco.conanmobile.data.entities.RegisteredDeviceData
import es.inteco.conanmobile.domain.entities.ConfigurationEntity
import es.inteco.conanmobile.domain.entities.RegisteredDeviceEntity

class RegisteredDeviceMapper {
    companion object {
        fun convert(model: RegisteredDeviceData) = with(model) {
            RegisteredDeviceEntity(
                timestamp = timestamp,
                status = status,
                statusMessage = statusMessage,
                message = MessageMapper.convert(message),
                path = path
            )
        }
    }
}