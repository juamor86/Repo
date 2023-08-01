package es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice

import es.juntadeandalucia.msspa.saludandalucia.data.factory.AdviceTypesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceCatalogTypeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceTypeEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceTypeResource
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.AdviceTypesMapper

class GetAdviceCatalogTypeNetUseCase(private val adviceTypesRepositoryFactory: AdviceTypesRepositoryFactory) :
    SingleUseCase<List<AdviceCatalogTypeEntity>>() {

    override fun buildUseCase() =
        adviceTypesRepositoryFactory.create(Strategy.NETWORK).getAdviceTypes().map {
            convertAdviceCatalogType(AdviceTypesMapper.convert(it))
        }

    private fun convertAdviceCatalogType(adviceTypeEntity: AdviceTypeEntity): List<AdviceCatalogTypeEntity>{
        val adviceCatalogList : MutableList<AdviceCatalogTypeEntity> = mutableListOf()
        val advicesResources = mutableListOf<AdviceTypeResource>()
        adviceTypeEntity.entry.map { advicesResources.add(it.resource) }
        advicesResources.map { adviceCatalogList.add(AdviceCatalogTypeEntity(id = it.id, text = it.text)) }
        return adviceCatalogList.toList()
    }
}




