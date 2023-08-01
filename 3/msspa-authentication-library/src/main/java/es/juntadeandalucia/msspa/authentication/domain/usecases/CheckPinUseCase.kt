package es.juntadeandalucia.msspa.authentication.domain.usecases

import es.juntadeandalucia.msspa.authentication.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.base.SingleUseCase
import io.reactivex.Single

class CheckPinUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    SingleUseCase<Boolean>() {

    private lateinit var pin: String

    override fun buildUseCase(): Single<Boolean> =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).isPinMatchedWithSaved(pin)

    fun params(pin: String) {
        this.pin = pin
    }
}