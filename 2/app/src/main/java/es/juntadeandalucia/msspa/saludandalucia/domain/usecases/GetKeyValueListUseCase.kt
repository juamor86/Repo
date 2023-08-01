package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.base.KeyValueRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.KeyValueEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.KeyValueMapper

class GetKeyValueListUseCase(private val keyValueRepositoryFactory: KeyValueRepositoryFactory) :
    SingleUseCase<List<KeyValueEntity>>() {

    private var file: Int = 0

    override fun buildUseCase() = keyValueRepositoryFactory.create(Strategy.JSON).run {
        getKeyValueList(file).map {
            KeyValueMapper.convert(it)
        }
    }

    fun params(file: Int) = this.apply {
        this.file = file
    }
}
