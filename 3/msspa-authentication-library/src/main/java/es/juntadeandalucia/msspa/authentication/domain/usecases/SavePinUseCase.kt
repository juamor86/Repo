package es.juntadeandalucia.msspa.authentication.domain.usecases

import es.juntadeandalucia.msspa.authentication.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.base.CompletableUseCase
import io.reactivex.Completable
import javax.crypto.Cipher
import javax.inject.Inject

class SavePinUseCase @Inject constructor(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) : CompletableUseCase() {
    private lateinit var pin: String

    override fun buildUseCase(): Completable =
            preferencesRepositoryFactory.create(Strategy.PREFERENCES).savePin(pin)

    fun params(pin: String, cipherEncrypt: Cipher? = null) =
        this.apply {
            this@SavePinUseCase.pin = pin
        }
}
