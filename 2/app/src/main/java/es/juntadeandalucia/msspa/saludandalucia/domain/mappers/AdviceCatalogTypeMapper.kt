package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceCatalogTypeData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AdviceCatalogTypeEntity

class AdviceCatalogTypeMapper {

    companion object {

        fun convertListEntity(adviceCatalogTypeList: List<AdviceCatalogTypeData>) = adviceCatalogTypeList.map { convert(it) }

        fun convertListData(adviceCatalogTypeList: List<AdviceCatalogTypeEntity>) = adviceCatalogTypeList.map { convert(it) }

        fun convert(model: AdviceCatalogTypeData) = AdviceCatalogTypeEntity(
            id = model.id,
            text = model.text
        )

        fun convert(model: AdviceCatalogTypeEntity) = AdviceCatalogTypeData(
            id = model.id,
            text = model.text
        )
    }
}
