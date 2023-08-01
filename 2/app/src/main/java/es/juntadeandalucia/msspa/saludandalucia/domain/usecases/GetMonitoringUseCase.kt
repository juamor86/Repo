package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.MonitoringRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.monitoring.MonitoringEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.MonitoringMapper
import io.reactivex.Single

class GetMonitoringUseCase(private val monitoringRepositoryFactory: MonitoringRepositoryFactory) :
    SingleUseCase<MonitoringEntity>() {
    override fun buildUseCase(): Single<MonitoringEntity> =
        monitoringRepositoryFactory.create(Strategy.NETWORK).run {
            getMonitoring().map {
                MonitoringMapper.convert(it)
            }
        }
}
