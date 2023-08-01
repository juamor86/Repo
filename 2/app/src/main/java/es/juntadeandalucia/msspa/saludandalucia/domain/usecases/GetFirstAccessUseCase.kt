package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SynchronousUseCase

class GetFirstAccessUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    SynchronousUseCase<Boolean>() {

    private lateinit var key: String

    fun param( key: String) = this.apply {
        this.key = key
    }


    override fun execute(): Boolean =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).getFirstAccess(key)
}
