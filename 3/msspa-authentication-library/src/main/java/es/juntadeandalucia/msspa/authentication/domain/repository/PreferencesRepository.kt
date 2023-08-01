package es.juntadeandalucia.msspa.authentication.domain.repository

import es.juntadeandalucia.msspa.authentication.data.factory.entities.UserData
import es.juntadeandalucia.msspa.authentication.security.CrytographyManager
import io.reactivex.Completable
import io.reactivex.Single
import javax.crypto.Cipher

interface PreferencesRepository {
    fun isAnySavedUser(): Boolean
    fun getSavedUsers(cipher: Cipher): Single<List<UserData>>
    fun savePin(pin: String): Completable
    fun isPinMatchedWithSaved(pin: String): Single<Boolean>
    fun removePin(): Completable
    fun saveUser(user: UserData, cipherEncrypt: Cipher, cipherDecrypt: Cipher): Completable
    fun removeUser(user: UserData, cryptoManager: CrytographyManager?): Single<Boolean>
    fun removeAllUser(cipherEncrypt: Cipher, cipherDecrypt: Cipher): Completable
    fun removeAllUser(): Completable
    fun setFirstLoadUserAdvice(): Completable
    fun getFirstLoadUserAdvice(): Boolean
    fun getFirstSaveUserAdvice(): Boolean
    fun setFirstSaveUserAdvice(): Completable
    fun getLoggingQRAttempts(): Boolean
    fun setLoggingQRAttempts(value: Boolean): Completable
}