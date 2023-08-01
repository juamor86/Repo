package es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice

import es.juntadeandalucia.msspa.saludandalucia.data.factory.AdviceTypesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceTypeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.AdviceTypesMapper

class GetAdviceTypesUseCase(private val adviceTypesRepositoryFactory: AdviceTypesRepositoryFactory) :
        SingleUseCase<AdviceTypeEntity>() {

    override fun buildUseCase() = adviceTypesRepositoryFactory.create(Strategy.NETWORK).run {
        getAdviceTypes().map {
            AdviceTypesMapper.convert(it)
        }
    }
}