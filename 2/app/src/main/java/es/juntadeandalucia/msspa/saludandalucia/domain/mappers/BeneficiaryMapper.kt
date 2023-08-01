package es.juntadeandalucia.msspa.saludandalucia.domain.mappers

import es.juntadeandalucia.msspa.saludandalucia.data.entities.BeneficiaryListData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.Entry
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.BeneficiaryEntity

class BeneficiaryMapper {
    companion object {
        fun convert(model: BeneficiaryListData) =
            model.entry.map { convert(it) }

        fun convert(model: Entry) = BeneficiaryEntity(
            nuhsa = model.resource.id,
            fullName = model.resource.patient.display,
            token = model.resource.identifier[0].value
        )
    }
}
