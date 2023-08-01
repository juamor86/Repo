package es.juntadeandalucia.msspa.authentication.domain.usecases

import es.juntadeandalucia.msspa.authentication.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.base.CompletableUseCase
import io.reactivex.Completable
import javax.crypto.Cipher
import javax.inject.Inject

class SetLoggingQRAttemptsUseCase @Inject constructor(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) : CompletableUseCase() {
    private var value: Boolean = false

    override fun buildUseCase(): Completable =
            preferencesRepositoryFactory.create(Strategy.PREFERENCES).setLoggingQRAttempts(value)

    fun params(isValue: Boolean) =
        this.apply {
            this@SetLoggingQRAttemptsUseCase.value = isValue
        }
}

