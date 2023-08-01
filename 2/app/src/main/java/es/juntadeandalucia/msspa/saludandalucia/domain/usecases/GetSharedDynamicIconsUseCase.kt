package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SynchronousUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity

class GetSharedDynamicIconsUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    SynchronousUseCase<DynamicScreenEntity>() {

    private lateinit var key : String

    fun param( key: String) = this.apply {
        this.key = key
    }

    override fun execute(): DynamicScreenEntity =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).getSharedData(key)
}