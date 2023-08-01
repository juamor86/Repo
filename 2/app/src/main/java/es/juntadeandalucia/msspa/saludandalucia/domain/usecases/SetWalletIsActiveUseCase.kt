package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import io.reactivex.Completable

class SetWalletIsActiveUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    CompletableUseCase() {

    private var isWalletActivated: Boolean = false

    fun param(isWalletActivated: Boolean) = this.apply {
        this.isWalletActivated = isWalletActivated
    }

    override fun buildUseCase(): Completable = Completable.fromAction(
        preferencesRepositoryFactory.create(Strategy.PREFERENCES)::setIsWalletActivated.invoke(isWalletActivated)
    )

}