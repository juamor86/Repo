package es.juntadeandalucia.msspa.authentication.domain.usecases

import es.juntadeandalucia.msspa.authentication.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.base.SynchronousUseCase

class GetFirstSaveUserAdviceUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    SynchronousUseCase<Boolean>() {

    override fun execute(): Boolean =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).getFirstSaveUserAdvice()
}
