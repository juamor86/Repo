package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.NewsRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.NewsEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.NewsMapper

class GetNewsUseCase(private val newsRepositoryFactory: NewsRepositoryFactory) :
    SingleUseCase<List<NewsEntity>>() {

    override fun buildUseCase() = newsRepositoryFactory.create(Strategy.NETWORK).run {
        getNews().map {
            NewsMapper.convert(it)
        }
    }
}
