package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicHomeData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicMenuData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicReleasesData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.DynamicScreenData
import io.reactivex.Single

interface DynamicUIRepository {
    fun getMenu(): Single<DynamicMenuData>
    fun getHome(): Single<DynamicHomeData>
    fun getScreen(): Single<DynamicScreenData>
    fun getReleases(): Single<DynamicReleasesData>
}
