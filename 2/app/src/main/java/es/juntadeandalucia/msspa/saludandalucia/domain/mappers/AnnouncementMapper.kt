package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AnnouncementData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AnnouncementEntity
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts

class AnnouncementMapper {

    companion object {

        fun convert(announcements: List<AnnouncementData>) = announcements.map { convert(it) }

        fun convert(model: AnnouncementData) = AnnouncementEntity(
            title = model.title,
            subtitle = model.subtitle,
            description = model.description,
            imgHeader = model.imgHeader,
            link = if (model.linkMode != null) model.linkMode else Consts.SAS_WEB
        )
    }
}
