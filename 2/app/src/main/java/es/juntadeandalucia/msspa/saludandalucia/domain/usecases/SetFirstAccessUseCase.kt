package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import io.reactivex.Completable

class SetFirstAccessUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    CompletableUseCase() {

    private lateinit var key: String
    private var value: Boolean = false

    fun param(key: String, value: Boolean = false) = this.apply {
        this.key = key
        this.value = value
    }

    override fun buildUseCase(): Completable =
        Completable.fromAction(
            preferencesRepositoryFactory.create(Strategy.PREFERENCES)::setIsFirstAccess.invoke(
                key, value
            )
        )
}
