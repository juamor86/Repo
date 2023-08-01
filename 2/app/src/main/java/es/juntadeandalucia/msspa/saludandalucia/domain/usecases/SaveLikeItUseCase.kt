package es.juntadeandalucia.msspa.saludandalucia.domain.usecases



import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.LikeItEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.LikeItMapper
import io.reactivex.Completable

class SaveLikeItUseCase(private val preferenceFactory: PreferencesRepositoryFactory) : CompletableUseCase() {

    private lateinit var likeEntity: LikeItEntity

    override fun buildUseCase(): Completable =
        preferenceFactory.create(Strategy.PREFERENCES).run {
            saveLikeIt(LikeItMapper.convert(likeEntity))
        }

    fun params(likeEntity: LikeItEntity): SaveLikeItUseCase =
        this@SaveLikeItUseCase.apply {
            this.likeEntity = likeEntity
        }

}