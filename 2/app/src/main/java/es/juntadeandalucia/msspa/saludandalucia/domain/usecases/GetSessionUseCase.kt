package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SynchronousUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.Session

class GetSessionUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    SynchronousUseCase<Session?>() {

    override fun execute(): Session? =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).getUserSession()
}
