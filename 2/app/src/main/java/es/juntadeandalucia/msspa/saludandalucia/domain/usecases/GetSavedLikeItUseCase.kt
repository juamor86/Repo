package es.juntadeandalucia.msspa.saludandalucia.domain.usecases


import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LikeItEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.LikeItMapper
import io.reactivex.Single

class GetSavedLikeItUseCase(private val preferenceFactory: PreferencesRepositoryFactory) :
    SingleUseCase<LikeItEntity>() {

    override fun buildUseCase(): Single<LikeItEntity> =
        preferenceFactory.create(Strategy.PREFERENCES).run {
            getSavedLikeIt().map { LikeItMapper.convert(it) }
        }

}