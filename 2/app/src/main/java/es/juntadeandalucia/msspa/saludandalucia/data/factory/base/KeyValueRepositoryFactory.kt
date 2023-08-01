package es.juntadeandalucia.msspa.saludandalucia.data.factory.base

import es.juntadeandalucia.msspa.saludandalucia.data.repository.json.KeyValueRepositoryJsonImpl
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.KeyValueRepository

class KeyValueRepositoryFactory(

    private val keyValueRepositoryJsonImpl: KeyValueRepositoryJsonImpl
) : BaseRepositoryFactory<KeyValueRepository>() {

    override fun create(strategy: Strategy): KeyValueRepository =
        when (strategy) {
            Strategy.JSON -> keyValueRepositoryJsonImpl
            else -> keyValueRepositoryJsonImpl
        }
}
