package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.MonitoringRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuestionnaireEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.NewMonitoringMapper
import io.reactivex.Single

class GetNewMonitoringUseCase(private val monitoringRepositoryFactory: MonitoringRepositoryFactory) :
    SingleUseCase<QuestionnaireEntity>() {

    private lateinit var id: String
    private var userId: String = ""

    override fun buildUseCase(): Single<QuestionnaireEntity> =
        monitoringRepositoryFactory.create(Strategy.NETWORK).run {
            getNewMonitoring(id).map {
                NewMonitoringMapper.convert(it)
            }
        }

    fun setId(id: String): GetNewMonitoringUseCase {
        this.id = id
        return this
    }
}
