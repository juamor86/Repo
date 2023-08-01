package es.juntadeandalucia.msspa.saludandalucia.domain.usecases.advice

import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SynchronousUseCase

class GetFirstSaveUserAdviceUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    SynchronousUseCase<Boolean>() {

    override fun execute(): Boolean =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).getFirstSaveUserAdvice()
}
