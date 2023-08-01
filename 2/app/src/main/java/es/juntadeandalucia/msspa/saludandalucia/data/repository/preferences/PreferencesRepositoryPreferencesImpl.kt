package es.juntadeandalucia.msspa.saludandalucia.data.repository.preferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import es.juntadeandalucia.msspa.saludandalucia.data.entities.AdviceCatalogTypeData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.QuizUserData
import es.juntadeandalucia.msspa.saludandalucia.data.entities.feedback.LikeItData
import es.juntadeandalucia.msspa.saludandalucia.domain.cache.TrustListCache
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.QuizSession
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.Session
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.dynamic.DynamicScreenEntity
import es.juntadeandalucia.msspa.saludandalucia.domain.repository.PreferencesRepository
import es.juntadeandalucia.msspa.saludandalucia.security.CrytographyManager
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.PREF_LIKE_IT
import io.reactivex.Completable
import io.reactivex.Single
import java.lang.reflect.Type
import javax.crypto.Cipher

class PreferencesRepositoryPreferencesImpl(private val sharedPreferences: SharedPreferences) :
    PreferencesRepository {

    override fun getUserLogged(): Single<Boolean> =
        Single.just(sharedPreferences.getBoolean(Consts.PREF_USER_LOGGED, false))

    override fun getFirstAccess(keyFirstAccess: String): Boolean {
        return sharedPreferences.getBoolean(keyFirstAccess, true)
    }

    override fun setIsFirstAccess(keyFirstAccess: String, value: Boolean): () -> Unit = {
        sharedPreferences.edit().putBoolean(keyFirstAccess, value).apply()
    }

    override fun isAnySavedUser(): Boolean {
        return sharedPreferences.contains(Consts.PREF_SAVED_USERS)
    }

    override fun getSavedUsers(cipher: Cipher): Single<List<QuizUserData>> {
        var savedUsers = mutableListOf<QuizUserData>()

        val savedUsersStrEncrypted = sharedPreferences.getString(Consts.PREF_SAVED_USERS, null)

        var savedUsersStr = CrytographyManager.decryptData(
            savedUsersStrEncrypted!!, cipher
        )

        savedUsersStr.let {
            val type: Type = object : TypeToken<List<QuizUserData>>() {}.type
            savedUsers = Gson().fromJson(savedUsersStr, type)
        }

        return Single.just(savedUsers)
    }

    override fun saveUser(
        user: QuizUserData,
        cipherEncrypt: Cipher,
        cipherDecrypt: Cipher
    ): Completable = Completable.create {

        var auxUser = user

        var savedUsers = mutableListOf<QuizUserData>()
        if (isAnySavedUser()) {
            savedUsers = getSavedUsers(cipherDecrypt).blockingGet() as MutableList<QuizUserData>
        }
        if (savedUsers.contains(auxUser)) {
            val oldUser = savedUsers.get(savedUsers.indexOf(auxUser))
            auxUser = mix(oldUser, auxUser)
            savedUsers.remove(auxUser)
        }

        savedUsers.add(auxUser)

        saveUsers(savedUsers, cipherEncrypt)

        it.onComplete()
    }

    private fun mix(oldUser: QuizUserData, user: QuizUserData): QuizUserData {
        return QuizUserData(
            getProperString(oldUser.name, user.name),
            getProperString(oldUser.nuhsa, user.nuhsa),
            user.idType,
            getProperString(oldUser.identification, user.identification),
            getProperString(oldUser.birthDate, user.birthDate),
            getProperString(oldUser.phone, user.phone),
            getProperString(oldUser.prefixPhone, user.prefixPhone),
            user.isHealthProf,
            user.isSecurityProf,
            user.isSpecialProf
        )
    }

    private fun getProperString(old: String, new: String) =
        if (old.isEmpty() && new.isNotEmpty()) {
            new
        } else {
            old
        }

    private fun saveUsers(savedUsers: MutableList<QuizUserData>, cipherEncrypt: Cipher) {
        if (savedUsers.size == 0) {
            sharedPreferences.edit().remove(Consts.PREF_SAVED_USERS).apply()
            return
        }

        val usersJSONString = Gson().toJson(savedUsers)

        val encryptedUsersString = CrytographyManager.encryptData(
            usersJSONString.toByteArray(Charsets.UTF_8), cipherEncrypt
        )

        sharedPreferences.edit().putString(Consts.PREF_SAVED_USERS, encryptedUsersString).apply()
    }

    override fun removeUser(
        user: QuizUserData,
        cryptoManager: CrytographyManager?
    ): Single<Boolean> =
        Single.create { single ->
            try {
                val cipherDecrypt = cryptoManager?.getCipherForDecryption()
                cipherDecrypt?.let {
                    val savedUsers =
                        getSavedUsers(cipherDecrypt).blockingGet() as MutableList<QuizUserData>
                    if (savedUsers.contains(user)) {
                        savedUsers.remove(user)
                    }

                    saveUsers(savedUsers, cryptoManager.getCipherForEncryption())

                    single.onSuccess(savedUsers.isEmpty())
                }
            } catch (e: Exception) {
            }
        }

    override fun getFirstLoadUserAdvice(): Boolean =
        sharedPreferences.getBoolean(Consts.PREF_FIRST_LOAD_USER_ADVICE, true)

    override fun setFirstLoadUserAdvice(): Completable = Completable.create {
        sharedPreferences.edit().putBoolean(Consts.PREF_FIRST_LOAD_USER_ADVICE, false).apply()
    }

    override fun getFirstSaveUserAdvice(): Boolean =
        sharedPreferences.getBoolean(Consts.PREF_FIRST_SAVE_USER_ADVICE, true)

    override fun setFirstSaveUserAdvice(): Completable = Completable.create {
        sharedPreferences.edit().putBoolean(Consts.PREF_FIRST_SAVE_USER_ADVICE, false).apply()
    }

    override fun saveHmsGmsToken(token: String): Completable = Completable.fromAction {
        sharedPreferences.edit().putString(Consts.PREF_HMS_GMS_TOKEN, token).apply()
    }

    override fun getHmsGmsToken(): Single<String> =
        Single.just(sharedPreferences.getString(Consts.PREF_HMS_GMS_TOKEN, ""))

    override fun getNotificationsPhoneNumber(): Single<String> =
        Single.just(
            sharedPreferences.getString(
                Consts.PREF_NOTIFICATION_SUBSCRIPTION_PHONE_NUMBER,
                ""
            )
        )

    override fun saveNotificationSubscription(phone: String) = Completable.fromAction {
        sharedPreferences.edit()
            .putString(Consts.PREF_NOTIFICATION_SUBSCRIPTION_PHONE_NUMBER, phone).apply()
    }

    override fun removeNotificationSubscription() = Completable.fromAction {
        sharedPreferences.edit().remove(Consts.PREF_NOTIFICATION_SUBSCRIPTION_PHONE_NUMBER).apply()
    }

    override fun saveSession(session: Session?): Completable = Completable.fromAction {
        val sessionJSONString = Gson().toJson(session)
        sharedPreferences.edit().putString(Consts.PREF_USER_SESSION, sessionJSONString).apply()
    }

    override fun saveQuizSession(quizSession: QuizSession?): Completable = Completable.fromAction {
        val quizSessionJSONString = Gson().toJson(quizSession)
        sharedPreferences.edit().putString(Consts.PREF_QUIZ_SESSION, quizSessionJSONString).apply()
    }

    override fun removeSession(): Completable = Completable.fromAction {
        sharedPreferences.edit().remove(Consts.PREF_USER_SESSION).apply()
    }

    override fun getUserSession(): Session? {
        var session: Session?
        val savedUserStr = sharedPreferences.getString(Consts.PREF_USER_SESSION, null)

        savedUserStr?.let {
            val type: Type = object : TypeToken<Session>() {}.type
            session = Gson().fromJson(savedUserStr, type)

            return session
        }

        return null
    }

    override fun getQuizSession(): QuizSession? {
        var quizSession: QuizSession?
        val savedQuizStr = sharedPreferences.getString(Consts.PREF_QUIZ_SESSION, null)

        savedQuizStr?.let {
            val type: Type = object : TypeToken<QuizSession>() {}.type
            quizSession = Gson().fromJson(savedQuizStr, type)
            return quizSession
        }
        return null
    }

    override fun getCovidTrustList(): Single<TrustListCache> {
        return Single.fromCallable {
            val trustListStr = sharedPreferences.getString(Consts.PREF_TRUSTLIST, null)
            if (trustListStr != null) {
                return@fromCallable Gson().fromJson(
                    trustListStr,
                    TrustListCache::class.java
                )
            } else {
                return@fromCallable TrustListCache(emptyArray())
            }
        }
    }

    override fun saveCovidTrustList(trustList: TrustListCache): Completable =
        Completable.fromAction {
            val trustListJson = Gson().toJson(trustList)
            sharedPreferences.edit().putString(Consts.PREF_TRUSTLIST, trustListJson).apply()
        }

    override fun saveSharedData(json: String, key: String): Completable =
        Completable.fromAction {
            sharedPreferences.edit().putString(key, json).apply()
        }

    override fun getSharedData(key: String): DynamicScreenEntity {
        val gsonBuilder = GsonBuilder().serializeNulls()
        val gson = gsonBuilder.create()
        return gson.fromJson(
            sharedPreferences.getString(Consts.ARG_DYNAMIC_ICONS, null)!!,
            DynamicScreenEntity::class.java
        )
    }

    override fun getIsWalletActivated(): Boolean =
        sharedPreferences.getBoolean(Consts.WALLET_DYNAMIC_ACTIVATED, false)


    override fun setIsWalletActivated(isWalletActivated: Boolean): () -> Unit = {
        sharedPreferences.edit().putBoolean(Consts.WALLET_DYNAMIC_ACTIVATED, isWalletActivated)
            .apply()
    }

    override fun isAnySavedAdviceCatalogType(): Boolean {
        return sharedPreferences.contains(Consts.PREF_FIRST_ACCESS_ADVICES_CATALOG_TYPE)
    }

    override fun getAdviceCatalogType(): Single<List<AdviceCatalogTypeData>> {
        var adviceCatalogTypeList = mutableListOf<AdviceCatalogTypeData>()

        val savedAdviceCatalogTypeList = sharedPreferences.getString(Consts.PREF_FIRST_ACCESS_ADVICES_CATALOG_TYPE, null)

        savedAdviceCatalogTypeList.let {
            val type: Type = object : TypeToken<List<AdviceCatalogTypeData>>() {}.type
            adviceCatalogTypeList = Gson().fromJson(savedAdviceCatalogTypeList, type)
        }

        return Single.just(adviceCatalogTypeList)
    }

    override fun saveAdviceCatalogType(adviceCatalogType: List<AdviceCatalogTypeData>): Completable =
        Completable.fromAction {
            if (adviceCatalogType.isNotEmpty()) {
                removeAdviceCatalogType()
                val adviceCatalogTypeJSONString = Gson().toJson(adviceCatalogType)
                sharedPreferences.edit().putString(Consts.PREF_FIRST_ACCESS_ADVICES_CATALOG_TYPE, adviceCatalogTypeJSONString).apply()
            }
        }

    override fun removeAdviceCatalogType(): Completable = Completable.fromAction {
        sharedPreferences.edit().remove(Consts.PREF_FIRST_ACCESS_ADVICES_CATALOG_TYPE).apply()
    }

    override fun saveLastUpdateReleases(lastUpdate: String): Completable = Completable.fromAction {
        sharedPreferences.edit().putString(Consts.PREF_DYNAMIC_RELEASES, lastUpdate).apply()
    }

    override fun getLastUpdateReleases(): Single<String> =
        Single.just(sharedPreferences.getString(Consts.PREF_DYNAMIC_RELEASES, ""))

    override fun getSavedLikeIt(): Single<LikeItData> =
        Single.create { single ->
            val savedInfo = sharedPreferences.getString(PREF_LIKE_IT, null)
            val type: Type = object : TypeToken<LikeItData>() {}.type

            savedInfo?.let { single.onSuccess(Gson().fromJson(savedInfo, type)) }
                ?: single.onError(Throwable())
        }

    override fun saveLikeIt(likeItData: LikeItData): Completable =
        Completable.create { completable ->

            val likeItJSON = Gson().toJson(likeItData)
            sharedPreferences.edit().putString(PREF_LIKE_IT, likeItJSON).apply()
            completable.onComplete()
        }

}
