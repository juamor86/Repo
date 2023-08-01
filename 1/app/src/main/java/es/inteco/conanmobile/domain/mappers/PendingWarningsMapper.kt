package es.inteco.conanmobile.domain.mappers

import es.inteco.conanmobile.data.entities.PendingWarningsData
import es.inteco.conanmobile.data.entities.PendingWarningsMessageData
import es.inteco.conanmobile.domain.entities.PendingWarningsEntity
import es.inteco.conanmobile.domain.entities.PendingWarningsMessageEntity

/**
 * Pending warnings mapper
 *
 * @constructor Create empty Pending warnings mapper
 */
class PendingWarningsMapper {
    companion object {
        fun convert(model: PendingWarningsData) = with(model) {
            PendingWarningsEntity(
                timestamp = timestamp,
                status = status,
                statusMessage = statusMessage,
                message = PendingWarningsMessageMapper.convert(message),
                path = path
            )
        }
    }

    class PendingWarningsMessageMapper {
        companion object {
            fun convert(model: PendingWarningsMessageData) = with(model) {
                PendingWarningsMessageEntity(haveNotifications)
            }
        }
    }
}