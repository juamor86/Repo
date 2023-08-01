package es.juntadeandalucia.msspa.saludandalucia.data.repository.mock

import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceCatalogTypeData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizUserData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.feedback.LikeItData
import es.juntadeandalucia.msspa.saludandalucia.domain.cache.TrustListCache
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizSession
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.Session
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.PreferencesRepository
import es.juntadeandalucia.msspa.saludandalucia.security.CrytographyManager
import io.reactivex.Completable
import io.reactivex.Single
import javax.crypto.Cipher

class PreferencesRepositoryMockImpl : PreferencesRepository {

    override fun getUserLogged(): Single<Boolean> = Single.just(false)

    override fun getFirstAccess(keyFirstAccess: String): Boolean = true

    override fun setIsFirstAccess(keyFirstAccess: String, value: Boolean): () -> Unit = {}

    override fun isAnySavedUser(): Boolean = true

    override fun saveUser(
        user: QuizUserData,
        cipherEncrypt: Cipher,
        cipherDecrypt: Cipher
    ): Completable =
        Completable.complete()

    override fun removeUser(
        user: QuizUserData,
        cryptoManager: CrytographyManager?
    ): Single<Boolean> {
        return Single.just(true)
    }

    override fun getFirstLoadUserAdvice(): Boolean = true

    override fun setFirstLoadUserAdvice(): Completable = Completable.complete()

    override fun getFirstSaveUserAdvice(): Boolean = true

    override fun setFirstSaveUserAdvice(): Completable = Completable.complete()

    override fun getSavedUsers(cipher: Cipher): Single<List<QuizUserData>> =
        Single.just(emptyList())

    override fun saveHmsGmsToken(token: String) = Completable.complete()

    override fun getHmsGmsToken(): Single<String> = Single.just("")

    override fun getNotificationsPhoneNumber(): Single<String> = Single.just("123456789")

    override fun saveNotificationSubscription(phone: String) = Completable.complete()

    override fun removeNotificationSubscription() = Completable.complete()

    override fun saveSession(session: Session?) = Completable.complete()

    override fun saveQuizSession(quizSession: QuizSession?) = Completable.complete()

    override fun removeSession(): Completable = Completable.complete()

    override fun getUserSession(): Session? = null

    override fun getQuizSession(): QuizSession? = null

    override fun getCovidTrustList(): Single<TrustListCache> {
        TODO("Not yet implemented")
    }

    override fun saveCovidTrustList(trustList: TrustListCache): Completable {
        TODO("Not yet implemented")
    }

    override fun saveSharedData(json: String, key: String)= Completable.complete()

    override fun getSharedData(key: String): DynamicScreenEntity {
        TODO("Not yet implemented")
    }

    override fun getIsWalletActivated(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setIsWalletActivated(isWalletActivated: Boolean): () -> Unit {
        TODO("Not yet implemented")
    }

    override fun isAnySavedAdviceCatalogType(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getAdviceCatalogType(): Single<List<AdviceCatalogTypeData>> {
        TODO("Not yet implemented")
    }

    override fun saveAdviceCatalogType(adviceCatalogType: List<AdviceCatalogTypeData>): Completable {
        TODO("Not yet implemented")
    }

    override fun removeAdviceCatalogType(): Completable {
        TODO("Not yet implemented")
    }

    override fun saveLastUpdateReleases(lastUpdate: String): Completable {
        TODO("Not yet implemented")
    }

    override fun getLastUpdateReleases():Single<String> = Single.just("")

    override fun getSavedLikeIt(): Single<LikeItData> {
        TODO("Not yet implemented")
    }

    override fun saveLikeIt(likeItData: LikeItData): Completable {
        TODO("Not yet implemented")
    }
}
