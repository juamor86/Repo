package es.juntadeandalucia.msspa.authentication.data.factory.repository.mock

import es.juntadeandalucia.msspa.authentication.data.factory.entities.UserData
import es.juntadeandalucia.msspa.authentication.domain.repository.PreferencesRepository
import es.juntadeandalucia.msspa.authentication.security.CrytographyManager
import io.reactivex.Completable
import io.reactivex.Single
import javax.crypto.Cipher

class PreferencesRepositoryMockImpl : PreferencesRepository {
    override fun isAnySavedUser(): Boolean = true

    override fun getSavedUsers(cipher: Cipher): Single<List<UserData>> = Single.just(emptyList())

    override fun saveUser(user: UserData, cipherEncrypt: Cipher, cipherDecrypt: Cipher): Completable =
            Completable.complete()

    override fun removeUser(
        user: UserData,
        cryptoManager: CrytographyManager?
    ): Single<Boolean> {
        return Single.just(true)
    }

    override fun removeAllUser(cipherEncrypt: Cipher, cipherDecrypt: Cipher): Completable = Completable.complete()

    override fun removeAllUser(): Completable = Completable.complete()

    override fun setFirstLoadUserAdvice(): Completable = Completable.complete()

    override fun getFirstLoadUserAdvice(): Boolean = true

    override fun getFirstSaveUserAdvice(): Boolean = true

    override fun setFirstSaveUserAdvice(): Completable = Completable.complete()

    override fun getLoggingQRAttempts(): Boolean = true

    override fun setLoggingQRAttempts(value: Boolean): Completable = Completable.complete()

    override fun savePin(pin: String): Completable = Completable.complete()

    override fun isPinMatchedWithSaved(pin: String): Single<Boolean> = Single.just(true)

    override fun removePin(): Completable = Completable.complete()
}