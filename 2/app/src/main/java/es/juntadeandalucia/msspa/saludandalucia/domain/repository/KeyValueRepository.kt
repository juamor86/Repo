package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.KeyValueData
import io.reactivex.Single

interface KeyValueRepository {

    fun getKeyValueList(file: Int): Single<List<KeyValueData>>
}
