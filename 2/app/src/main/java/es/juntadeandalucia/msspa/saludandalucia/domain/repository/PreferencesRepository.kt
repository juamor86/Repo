package es.juntadeandalucia.msspa.saludandalucia.domain.repository

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceCatalogTypeData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizUserData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.feedback.LikeItData
import es.juntadeandalucia.msspa.saludandalucia.domain.cache.TrustListCache
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizSession
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.Session
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.security.CrytographyManager
import io.reactivex.Completable
import io.reactivex.Single
import javax.crypto.Cipher

interface PreferencesRepository {

    fun getUserLogged(): Single<Boolean>
    fun getFirstAccess(keyFirstAccess: String): Boolean
    fun setIsFirstAccess(keyFirstAccess: String, value: Boolean): () -> Unit
    fun isAnySavedUser(): Boolean
    fun getSavedUsers(cipher: Cipher): Single<List<QuizUserData>>
    fun saveUser(user: QuizUserData, cipherEncrypt: Cipher, cipherDecrypt: Cipher): Completable
    fun removeUser(user: QuizUserData, cryptoManager: CrytographyManager?): Single<Boolean>
    fun getFirstLoadUserAdvice(): Boolean
    fun setFirstLoadUserAdvice(): Completable
    fun getFirstSaveUserAdvice(): Boolean
    fun setFirstSaveUserAdvice(): Completable
    fun saveHmsGmsToken(token: String): Completable
    fun getHmsGmsToken(): Single<String>
    fun getNotificationsPhoneNumber(): Single<String>
    fun saveNotificationSubscription(phone: String): Completable
    fun removeNotificationSubscription(): Completable
    fun saveSession(session: Session?): Completable
    fun saveQuizSession(quizSession: QuizSession?): Completable
    fun removeSession(): Completable
    fun getUserSession(): Session?
    fun getQuizSession(): QuizSession?
    fun getCovidTrustList(): Single<TrustListCache>
    fun saveCovidTrustList(trustList: TrustListCache): Completable
    fun saveSharedData(json: String, key: String): Completable
    fun getSharedData(key: String): DynamicScreenEntity
    fun getIsWalletActivated(): Boolean
    fun setIsWalletActivated(isWalletActivated: Boolean): () -> Unit
    fun isAnySavedAdviceCatalogType(): Boolean
    fun getAdviceCatalogType(): Single<List<AdviceCatalogTypeData>>
    fun saveAdviceCatalogType(adviceCatalogType: List<AdviceCatalogTypeData>): Completable
    fun removeAdviceCatalogType(): Completable
    fun saveLastUpdateReleases(lastUpdate: String): Completable
    fun getLastUpdateReleases(): Single<String>
    fun getSavedLikeIt(): Single<LikeItData>
    fun saveLikeIt(likeItData: LikeItData): Completable
}
