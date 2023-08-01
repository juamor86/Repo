package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.UserRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.measurements.MeasurementSectionEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.measure.MeasurementMapper
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.measure.MeasurementSectionMapper
import io.reactivex.Single

class GetMeasureSectionUseCase(private val userRepositoryFactory: UserRepositoryFactory) :
    SingleUseCase<List<MeasurementSectionEntity>>() {

    private var page: Int = 1

    override fun buildUseCase(): Single<List<MeasurementSectionEntity>> =
        userRepositoryFactory.create(Strategy.NETWORK).getMeasuresData(page).map { data ->
            MeasurementMapper.getPages(data.link)
            val intermediateEntity = MeasurementMapper.convert(data.entry[0].resources)
            MeasurementSectionMapper.convert(intermediateEntity)
        }

    fun params(page: Int) {
        this.page = page
    }
}
