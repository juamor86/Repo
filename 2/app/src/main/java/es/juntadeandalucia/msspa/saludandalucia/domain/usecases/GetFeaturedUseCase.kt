package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.FeaturedRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.FeaturedEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.FeaturedMapper

class GetFeaturedUseCase(private val featuredRepositoryFactory: FeaturedRepositoryFactory) :
    SingleUseCase<List<FeaturedEntity>>() {

    override fun buildUseCase() = featuredRepositoryFactory.create(Strategy.NETWORK).run {
        getFeatured().map {
            FeaturedMapper.convert(it)
        }
    }
}
