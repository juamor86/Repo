package es.juntadeandalucia.msspa.saludandalucia.data.repository.network

import es.juntadeandalucia.msspa.saludandalucia.data.api.MSSPAApi
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceCatalogTypeData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizUserData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.feedback.LikeItData
import es.juntadeandalucia.msspa.saludandalucia.domain.cache.TrustListCache
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizSession
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.Session
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.mappers.GreenPassMapper
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.PreferencesRepository
import es.juntadeandalucia.msspa.saludandalucia.security.CrytographyManager
import io.reactivex.Completable
import io.reactivex.Single
import javax.crypto.Cipher

class PreferencesRepositoryNetworkImpl(private val msspaApi: MSSPAApi) :
    PreferencesRepository {
    override fun getUserLogged(): Single<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getFirstAccess(keyFirstAccess: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun setIsFirstAccess(keyFirstAccess: String, value: Boolean): () -> Unit {
        TODO("Not yet implemented")
    }

    override fun isAnySavedUser(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getSavedUsers(cipher: Cipher): Single<List<QuizUserData>> {
        TODO("Not yet implemented")
    }

    override fun saveUser(
        user: QuizUserData,
        cipherEncrypt: Cipher,
        cipherDecrypt: Cipher
    ): Completable {
        TODO("Not yet implemented")
    }

    override fun removeUser(
        user: QuizUserData,
        cryptoManager: CrytographyManager?
    ): Single<Boolean> {
        TODO("Not yet implemented")
    }

    override fun getFirstLoadUserAdvice(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setFirstLoadUserAdvice(): Completable {
        TODO("Not yet implemented")
    }

    override fun getFirstSaveUserAdvice(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setFirstSaveUserAdvice(): Completable {
        TODO("Not yet implemented")
    }

    override fun saveHmsGmsToken(token: String): Completable {
        TODO("Not yet implemented")
    }

    override fun getHmsGmsToken(): Single<String> {
        TODO("Not yet implemented")
    }

    override fun getNotificationsPhoneNumber(): Single<String> {
        TODO("Not yet implemented")
    }

    override fun saveNotificationSubscription(phone: String): Completable {
        TODO("Not yet implemented")
    }

    override fun removeNotificationSubscription(): Completable {
        TODO("Not yet implemented")
    }

    override fun saveSession(session: Session?): Completable {
        TODO("Not yet implemented")
    }

    override fun saveQuizSession(session: QuizSession?): Completable {
        TODO("Not yet implemented")
    }

    override fun removeSession(): Completable {
        TODO("Not yet implemented")
    }

    override fun getUserSession(): Session? {
        TODO("Not yet implemented")
    }

    override fun getQuizSession(): QuizSession? {
        TODO("Not yet implemented")
    }

    override fun getCovidTrustList(): Single<TrustListCache> {
        return msspaApi.getTrustList().map {
            GreenPassMapper.convert(it)
        }
    }

    override fun saveCovidTrustList(trustList: TrustListCache): Completable {
        TODO("Not yet implemented")
    }

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

    override fun getLastUpdateReleases(): Single<String> {
        TODO("Not yet implemented")
    }

    override fun saveSharedData(json: String, key: String): Completable {
        TODO("Not yet implemented")
    }

    override fun getSavedLikeIt(): Single<LikeItData> {
        TODO("Not yet implemented")
    }

    override fun saveLikeIt(likeItData: LikeItData): Completable {
        TODO("Not yet implemented")
    }
}
