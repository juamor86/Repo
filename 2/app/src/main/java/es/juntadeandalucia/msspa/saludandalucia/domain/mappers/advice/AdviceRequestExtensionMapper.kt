package es.juntadeandalucia.msspa.saludandalucia.domain.mappers.advice

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceRequestData
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.EntryExtensionEntity

class AdviceRequestExtensionMapper {
    companion object {

        fun convert(extensionEntity: EntryExtensionEntity): AdviceRequestData.Entry.Resource.Extension =
            with(extensionEntity) {
                AdviceRequestData.Entry.Resource.Extension(
                    url = url,
                    AdviceRequestData.Entry.Resource.Extension.ValueReference(
                        type = valueReference.type,
                        id = valueReference.id,
                        display = valueReference.display
                    )
                )
            }

        fun convertList(extensionList: List<EntryExtensionEntity>) = extensionList.map {
            convert(it)
        }

    }

}