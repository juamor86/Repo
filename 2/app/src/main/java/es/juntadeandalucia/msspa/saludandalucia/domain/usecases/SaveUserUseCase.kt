package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizUserEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.QuizUserMapper
import io.reactivex.Completable
import javax.crypto.Cipher

class SaveUserUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) : CompletableUseCase() {

    private lateinit var user: QuizUserEntity
    private lateinit var cipherEncrypt: Cipher
    private lateinit var cipherDecrypt: Cipher

    override fun buildUseCase(): Completable =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).saveUser(QuizUserMapper.convert(user), cipherEncrypt, cipherDecrypt)

    fun params(user: QuizUserEntity, cipherEncrypt: Cipher, cipherDecrypt: Cipher) =
        this.apply {
            this@SaveUserUseCase.user = user
            this@SaveUserUseCase.cipherEncrypt = cipherEncrypt
            this@SaveUserUseCase.cipherDecrypt = cipherDecrypt
        }
}
