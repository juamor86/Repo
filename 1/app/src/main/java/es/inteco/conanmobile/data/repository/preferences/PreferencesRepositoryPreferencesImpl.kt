package es.inteco.conanmobile.data.repository.preferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.inteco.conanmobile.data.entities.OSIData
import es.inteco.conanmobile.data.entities.WarningsData
import es.inteco.conanmobile.domain.entities.AnalysisEntity
import es.inteco.conanmobile.domain.entities.RegisteredDeviceEntity
import es.inteco.conanmobile.domain.repository.PreferencesRepository
import es.inteco.conanmobile.utils.Consts
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Preferences repository preferences impl
 *
 * @property sharedPreferences
 * @constructor Create empty Preferences repository preferences impl
 */
class PreferencesRepositoryPreferencesImpl(private val sharedPreferences: SharedPreferences) :
    PreferencesRepository {

    override fun getFirstAccess(): Boolean {
        return sharedPreferences.getBoolean(Consts.PREF_FIRST_ACCESS, true)
    }

    override fun setIsFirstAccess(): Completable = Completable.fromAction {
        sharedPreferences.edit().putBoolean(Consts.PREF_FIRST_ACCESS, false).apply()
    }

    override fun saveDefaultAnalysis(defaultAnalysis: AnalysisEntity): Completable =
        Completable.fromAction {

            sharedPreferences.edit()
                .putString(Consts.PREF_DEFAULT_ANALYSIS_ID, Gson().toJson(defaultAnalysis)).apply()
        }

    override fun getDefaultAnalysis(): Single<AnalysisEntity> {
        sharedPreferences.getString(Consts.PREF_DEFAULT_ANALYSIS_ID, null)?.let {
            val type = object : TypeToken<AnalysisEntity>() {}.type
            return Single.just(Gson().fromJson(it, type))
        }
        return Single.just(AnalysisEntity(null))
    }

    override fun setDefaultAnalysisLaunched(launched: Boolean): Completable =
        Completable.fromAction {
            sharedPreferences.edit().putBoolean(Consts.PREF_ANALYSIS_LAUNCHED, launched).apply()
        }

    override fun getDefaultAnalysisLaunched(): Boolean =
        sharedPreferences.getBoolean(Consts.PREF_ANALYSIS_LAUNCHED, false)

    override fun setNextAvailableAnalysisDateTime(datetime: Long): Completable =
        Completable.fromAction {
            sharedPreferences.edit().putLong(Consts.PREF_NEXT_DATETIME_ANALYIS, datetime).apply()
        }

    override fun getNextAvailableAnalysisDateTime(): Long =
        sharedPreferences.getLong(Consts.PREF_NEXT_DATETIME_ANALYIS, -1)

    override fun getFirstAnalysisLaunched(): Boolean {
        return sharedPreferences.getBoolean(Consts.PREF_FIRST_ANALYSIS, true)
    }

    override fun setIsFirstAnalysisLaunched(): Completable = Completable.fromAction {
        sharedPreferences.edit().putBoolean(Consts.PREF_FIRST_ANALYSIS, false).apply()
    }

    override fun saveDeviceRegister(device: RegisteredDeviceEntity): Completable =
        Completable.fromAction {
            sharedPreferences.edit().putString(Consts.PREF_REGISTERED_DEVICE, Gson().toJson(device))
                .apply()
        }

    override fun getDeviceRegister(): Single<RegisteredDeviceEntity> {
        return Single.just(
            Gson().fromJson(
                sharedPreferences.getString(
                    Consts.PREF_REGISTERED_DEVICE, null
                ), object : TypeToken<RegisteredDeviceEntity>() {}.type
            )
        )
    }

    override fun existOsiTips(): Boolean = sharedPreferences.contains(Consts.PREF_OSI_TIPS)

    override fun saveOsiTips(osiTips: OSIData): Completable = Completable.fromAction {
        sharedPreferences.edit().putString(Consts.PREF_OSI_TIPS, Gson().toJson(osiTips)).apply()
    }

    override fun getOsiTips(): Single<OSIData> {
        return Single.just(
            Gson().fromJson(
                sharedPreferences.getString(
                    Consts.PREF_OSI_TIPS, null
                ), object : TypeToken<OSIData>() {}.type
            )
        )
    }

    override fun existWarnings(): Boolean = sharedPreferences.contains(Consts.PREF_WARNINGS)

    override fun saveWarnings(warnings: WarningsData): Completable = Completable.fromAction {
        sharedPreferences.edit().putString(Consts.PREF_WARNINGS, Gson().toJson(warnings)).apply()
    }

    override fun getWarnings(): Single<WarningsData> {
        return Single.just(
            Gson().fromJson(
                sharedPreferences.getString(
                    Consts.PREF_WARNINGS, null
                ), object : TypeToken<WarningsData>() {}.type
            )
        )
    }

    override fun getLastAlertAnalysis(): Long = sharedPreferences.getLong(
        Consts.LAST_TIME_ALERT, 0
    )

    override fun saveLastAlertAnalysis(time: Long) {
        sharedPreferences.edit().putLong(
            Consts.LAST_TIME_ALERT, time
        ).apply()
    }
}