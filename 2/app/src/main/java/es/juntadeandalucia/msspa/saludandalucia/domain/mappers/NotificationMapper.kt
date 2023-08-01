package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.NotificationData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NotificationEntity

class NotificationMapper {

    companion object {

        fun convert(notifications: List<NotificationData>) = notifications.map { convert(it) }

        fun convert(model: NotificationData) = NotificationEntity(
            id = model.id,
            title = model.title,
            description = model.description,
            date = model.date,
            readed = model.readed
        )

        fun convert(model: NotificationEntity) = NotificationData(
            id = model.id,
            title = model.title,
            description = model.description,
            date = model.date,
            readed = model.readed
        )
    }
}
