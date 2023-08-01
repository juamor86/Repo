package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.AnnouncementsRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AnnouncementEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.AnnouncementMapper

class GetAnnouncementsUseCase(private val announcementsRepositoryFactory: AnnouncementsRepositoryFactory) :
    SingleUseCase<List<AnnouncementEntity>>() {

    override fun buildUseCase() = announcementsRepositoryFactory.create(Strategy.NETWORK).run {
        getAnnouncements().map {
            AnnouncementMapper.convert(it)
        }
    }
}
