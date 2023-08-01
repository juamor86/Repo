package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import io.reactivex.Single

class GetHmsGmsTokenUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    SingleUseCase<String>() {

    override fun buildUseCase(): Single<String> =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).getHmsGmsToken()
}