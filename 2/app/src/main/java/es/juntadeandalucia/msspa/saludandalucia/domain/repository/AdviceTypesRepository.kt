package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceTypesData
import io.reactivex.Single

interface AdviceTypesRepository {
    fun getAdviceTypes(): Single<AdviceTypesData>
}
