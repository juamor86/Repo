package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.AppsRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.data.utils.ApiConstants
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.AppMapper

class GetAppsUseCase(private val appsRepositoryFactory: AppsRepositoryFactory) :
    SingleUseCase<List<AppEntity>>() {

    private var isHuawei: Boolean = false

    override fun buildUseCase() = appsRepositoryFactory.create(Strategy.NETWORK).run {
        val device = if (isHuawei) ApiConstants.AppsApi.TYPE_HUAWEI else ApiConstants.AppsApi.TYPE_ANDROID
        getApps(device).map {
            AppMapper.convert(it)
        }
    }

    fun param(isHuawei: Boolean) = this.apply {
        this.isHuawei = isHuawei
    }
}
