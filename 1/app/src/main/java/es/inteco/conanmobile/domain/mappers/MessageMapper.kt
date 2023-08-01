package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.MessageItem
import es.inteco.conanmobile.data.entities.MessageKey
import es.inteco.conanmobile.domain.entities.MessageEntity
import es.inteco.conanmobile.domain.entities.MessageImeiEntity

/**
 * Message mapper
 *
 * @constructor Create empty Message mapper
 */
class MessageMapper {
    companion object {
        fun convert(model: MessageItem) = with(model) {
            MessageEntity(
                id = id,
                version = version,
                expirationDate = expirationDate,
                administration = AdministrationItemMapper.convert(administration),
                analysis = AnalysisItemMapper.convert(analysis),
                about = AdministrationItemMapper.convertToString(about),
                legal = AdministrationItemMapper.convertToString(legal),
                maliciousAppsDescription = AdministrationItemMapper.convertToString(maliciousAppsDescription),
                permissions = PermissionsItemMapper.convert(permissions)
            )
        }

        fun convert(model: MessageKey) = with(model) {
            MessageImeiEntity(
                idTerminal = idTerminal,
                key = key,
            )
        }
    }
}