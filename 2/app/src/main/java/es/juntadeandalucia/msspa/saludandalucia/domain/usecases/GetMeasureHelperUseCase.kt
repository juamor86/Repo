package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.UserRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.measurements.MeasureHelperEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.measure.MeasureHelperMapper
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import io.reactivex.Single

class GetMeasureHelperUseCase(private val userRepositoryFactory: UserRepositoryFactory) :
    SingleUseCase<List<MeasureHelperEntity>>() {

    override fun buildUseCase(): Single<List<MeasureHelperEntity>> =
        userRepositoryFactory.create(Strategy.NETWORK).getMeasureHelper(Consts.TYPE_HELP).map {
            MeasureHelperMapper.convert(it)
        }
}
