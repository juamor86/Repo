package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import io.reactivex.Completable

class SaveLastUpdateDynReleasesUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    CompletableUseCase() {

    private lateinit var lastUpdate: String

    override fun buildUseCase(): Completable =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).saveLastUpdateReleases(lastUpdate)

    fun params(lastUpdate: String) =
        this.apply {
            this@SaveLastUpdateDynReleasesUseCase.lastUpdate = lastUpdate
        }
}
