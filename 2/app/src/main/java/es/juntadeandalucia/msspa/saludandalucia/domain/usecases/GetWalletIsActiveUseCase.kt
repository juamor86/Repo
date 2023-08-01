package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SynchronousUseCase

class GetWalletIsActiveUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    SynchronousUseCase<Boolean>() {
    override fun execute(): Boolean =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).getIsWalletActivated()


}