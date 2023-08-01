package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AnnouncementData
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.AnnouncementsRepository
import io.reactivex.Single

class AnnouncementsRepositoryNetworkImpl(
    private val msspaApi: MSSPAApi
) : AnnouncementsRepository {

    override fun getAnnouncements(): Single<List<AnnouncementData>> {
        val list = msspaApi.getAnnouncements(ApiConstants.NewsApi.TYPE_BANNER)
        return list.map { it.entry }
    }
}
