package es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice

import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceCatalogTypeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.AdviceCatalogTypeMapper

class GetAdviceCatalogTypePrefUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    SingleUseCase<List<AdviceCatalogTypeEntity>>() {

    override fun buildUseCase() =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).getAdviceCatalogType().map {
            AdviceCatalogTypeMapper.convertListEntity(it)
        }
}

