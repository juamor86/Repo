package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AnnouncementData
import io.reactivex.Single

interface AnnouncementsRepository {

    fun getAnnouncements(): Single<List<AnnouncementData>>
}
