package es.juntadeandalucia.msspa.authentication.data.factory.repository.persistence

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.juntadeandalucia.msspa.authentication.data.factory.entities.UserData
import es.juntadeandalucia.msspa.authentication.domain.repository.PreferencesRepository
import es.juntadeandalucia.msspa.authentication.presentation.MsspaAuthConsts
import es.juntadeandalucia.msspa.authentication.security.CrytographyManager
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import io.reactivex.Completable
import io.reactivex.Single
import java.lang.reflect.Type
import javax.crypto.Cipher

class PreferencesRepositoryPreferencesImpl(private val sharedPreferences: SharedPreferences) :
    PreferencesRepository {
    override fun isAnySavedUser(): Boolean {
        return sharedPreferences.contains(ApiConstants.Preferences.PREF_SAVED_USERS)
    }

    override fun getSavedUsers(cipher: Cipher): Single<List<UserData>> {
        var savedUsers: MutableList<UserData>

        val savedUsersStrEncrypted =
            sharedPreferences.getString(ApiConstants.Preferences.PREF_SAVED_USERS, null)

        val savedUsersStr = CrytographyManager.decryptData(
            savedUsersStrEncrypted!!, cipher
        )

        savedUsersStr.let {
            val type: Type = object : TypeToken<List<UserData>>() {}.type
            savedUsers = Gson().fromJson(savedUsersStr, type)
        }

        return Single.just(savedUsers)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun saveUser(
        user: UserData,
        cipherEncrypt: Cipher,
        cipherDecrypt: Cipher
    ): Completable = Completable.create {
        var auxUser = user
        var savedUsers = mutableListOf<UserData>()

        if (isAnySavedUser()) {
            savedUsers = getSavedUsers(cipherDecrypt).blockingGet() as MutableList<UserData>
        }

        if (savedUsers.contains(auxUser)) {
            val oldUser = savedUsers.get(savedUsers.indexOf(auxUser))

            if (auxUser.identification == MsspaAuthConsts.DEFAULT_DNI_UNDER_AGE) {
                auxUser = checkIfUserUnderAgeExist(auxUser, oldUser, savedUsers)
            } else {
                auxUser = mix(oldUser, auxUser)
                savedUsers.remove(auxUser)
            }
        }

        savedUsers.add(auxUser)

        saveUsers(savedUsers, cipherEncrypt)

        it.onComplete()
    }

    private fun checkIfUserUnderAgeExist(newUser: UserData, oldUser: UserData, userList: MutableList<UserData>) : UserData {
        return if (newUser.name == oldUser.name) {
           val auxUser = mix(oldUser, newUser)
            userList.remove(oldUser)
            auxUser
        } else {
            newUser
        }
    }

    private fun mix(oldUser: UserData, user: UserData): UserData {
        return UserData(
            getProperString(oldUser.name, user.name),
            getProperString(oldUser.nuhsa, user.nuhsa),
            getProperString(oldUser.nuss, user.nuss),
            user.idType,
            getProperString(oldUser.identification, user.identification),
            getProperString(oldUser.birthDate, user.birthDate),
            getProperString(oldUser.phone, user.phone)
        )
    }


    private fun getProperString(old: String, new: String) =
        if (old.isEmpty() && new.isNotEmpty()) {
            new
        } else {
            old
        }

    private fun saveUsers(savedUsers: MutableList<UserData>, cipherEncrypt: Cipher) {
        if (savedUsers.size == 0) {
            sharedPreferences.edit().remove(ApiConstants.Preferences.PREF_SAVED_USERS).apply()
            return
        }

        val usersJSONString = Gson().toJson(savedUsers)

        val encryptedUsersString = CrytographyManager.encryptData(
            usersJSONString.toByteArray(Charsets.UTF_8), cipherEncrypt
        )

        sharedPreferences.edit()
            .putString(ApiConstants.Preferences.PREF_SAVED_USERS, encryptedUsersString).apply()
    }

    override fun removeUser(user: UserData, cryptoManager: CrytographyManager?): Single<Boolean> =
        Single.create { single ->
            try {
                val cipherDecrypt = cryptoManager?.getCipherForDecryption()
                cipherDecrypt?.let {
                    val savedUsers =
                        getSavedUsers(cipherDecrypt).blockingGet() as MutableList<UserData>
                    if (savedUsers.contains(user)) {
                        savedUsers.remove(user)
                    }

                    saveUsers(savedUsers, cryptoManager.getCipherForEncryption())

                    single.onSuccess(savedUsers.isEmpty())
                }
            } catch (e: Exception) {
            }
        }

    override fun removeAllUser(cipherEncrypt: Cipher, cipherDecrypt: Cipher): Completable =
        Completable.create {
            val savedUsers = getSavedUsers(cipherDecrypt).blockingGet() as MutableList<UserData>
            savedUsers.clear()
            val usersJSONString = Gson().toJson(savedUsers)
            val encryptedUsersString = CrytographyManager.encryptData(
                usersJSONString.toByteArray(Charsets.UTF_8),
                cipherEncrypt
            )
            sharedPreferences.edit().putString(ApiConstants.Preferences.PREF_SAVED_USERS, encryptedUsersString).apply()
            sharedPreferences.edit().remove(ApiConstants.Preferences.PREF_SAVED_USERS).apply()
            it.onComplete()
        }

    override fun removeAllUser(): Completable =  Completable.create {
        sharedPreferences.edit().remove(ApiConstants.Preferences.PREF_SAVED_USERS).apply()
        it.onComplete()
    }

    override fun getFirstLoadUserAdvice(): Boolean =
        sharedPreferences.getBoolean(ApiConstants.Preferences.PREF_FIRST_LOAD_USER_ADVICE, true)

    override fun setFirstLoadUserAdvice(): Completable = Completable.create {
        sharedPreferences.edit()
            .putBoolean(ApiConstants.Preferences.PREF_FIRST_LOAD_USER_ADVICE, false).apply()
    }

    override fun getFirstSaveUserAdvice(): Boolean =
        sharedPreferences.getBoolean(ApiConstants.Preferences.PREF_FIRST_SAVE_USER_ADVICE, true)

    override fun setFirstSaveUserAdvice(): Completable = Completable.create {
        sharedPreferences.edit()
            .putBoolean(ApiConstants.Preferences.PREF_FIRST_SAVE_USER_ADVICE, false).apply()
    }

    override fun savePin(pin: String): Completable =
        Completable.create {
            sharedPreferences.edit().putString(ApiConstants.Preferences.PREF_PIN, pin).apply()
            it.onComplete()
        }

    override fun isPinMatchedWithSaved(pin: String): Single<Boolean> =
        Single.create { single ->
            val pinSaved = sharedPreferences.getString(ApiConstants.Preferences.PREF_PIN, null)
            single.onSuccess(pinSaved!!.trim() == pin.trim())
        }

    override fun removePin(): Completable = Completable.fromAction {
        sharedPreferences.edit().remove(ApiConstants.Preferences.PREF_PIN).apply()
    }

    override fun getLoggingQRAttempts(): Boolean =
        sharedPreferences.getBoolean(ApiConstants.Preferences.PREF_FIRST_LOGGING_QR_ATTEMPTS, true)

    override fun setLoggingQRAttempts(value: Boolean): Completable = Completable.create {
        sharedPreferences.edit()
            .putBoolean(ApiConstants.Preferences.PREF_FIRST_LOGGING_QR_ATTEMPTS, value).apply()
        it.onComplete()
    }
}