package es.juntadeandalucia.msspa.authentication.domain.usecases

import es.juntadeandalucia.msspa.authentication.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.authentication.domain.Strategy
import es.juntadeandalucia.msspa.authentication.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.authentication.domain.entities.MsspaAuthenticationUserEntity
import es.juntadeandalucia.msspa.authentication.domain.mappers.UserMapper
import io.reactivex.Completable
import javax.crypto.Cipher

class SaveUserUseCase (private val preferencesRepositoryFactory: PreferencesRepositoryFactory) : CompletableUseCase() {
    private lateinit var msspaAuthenticationUser: MsspaAuthenticationUserEntity
    private lateinit var cipherEncrypt: Cipher
    private lateinit var cipherDecrypt: Cipher

    override fun buildUseCase(): Completable =
            preferencesRepositoryFactory.create(Strategy.PREFERENCES).saveUser(UserMapper.convert(msspaAuthenticationUser), cipherEncrypt, cipherDecrypt)

    fun params(msspaAuthenticationUser: MsspaAuthenticationUserEntity, cipherEncrypt: Cipher, cipherDecrypt: Cipher) =
            this.apply {
                this@SaveUserUseCase.msspaAuthenticationUser = msspaAuthenticationUser
                this@SaveUserUseCase.cipherEncrypt = cipherEncrypt
                this@SaveUserUseCase.cipherDecrypt = cipherDecrypt
            }
}
