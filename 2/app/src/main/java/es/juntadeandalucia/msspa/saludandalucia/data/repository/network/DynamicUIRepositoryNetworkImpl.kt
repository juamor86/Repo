package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicHomeData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicMenuData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicReleasesData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicScreenData
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.DynamicUIRepository
import io.reactivex.Single
import javax.inject.Inject

class DynamicUIRepositoryNetworkImpl @Inject constructor(private val msspaApi: MSSPAApi) :
    DynamicUIRepository {

    override fun getMenu(): Single<DynamicMenuData> =
        msspaApi.getDynamicMenu()

    override fun getHome(): Single<DynamicHomeData> =
        msspaApi.getDynamicLayout()

    override fun getScreen(): Single<DynamicScreenData> =
        msspaApi.getDynamicScreen()

    override fun getReleases(): Single<DynamicReleasesData> =
        msspaApi.getDynamicReleases()
}
