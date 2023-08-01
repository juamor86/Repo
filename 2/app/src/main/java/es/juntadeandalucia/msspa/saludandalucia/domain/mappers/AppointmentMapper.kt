package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AppointmentData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppointmentEntity

class AppointmentMapper {

    companion object {
        fun convert(model: AppointmentData) = AppointmentEntity(
            id = model.id,
            center = model.center.plus(", ").plus(model.location),
            date = model.date.plus(" ").plus(model.hour),
            topic = model.topic,
            task = model.task.plus(" - ").plus(model.activity)
        )
    }
}
