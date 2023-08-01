package es.juntadeandalucia.msspa.saludandalucia.domain.usecases


import es.juntadeandalucia.msspa.saludandalucia.data.factory.FeedbackRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LikeItEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.LikeItMapper
import io.reactivex.Single

class GetServiceEventsUseCase(private val factory: FeedbackRepositoryFactory) :
    SingleUseCase<LikeItEntity>() {

    override fun buildUseCase(): Single<LikeItEntity> =
        factory.create(Strategy.NETWORK).getEvents().map { LikeItMapper.convert(it) }

}