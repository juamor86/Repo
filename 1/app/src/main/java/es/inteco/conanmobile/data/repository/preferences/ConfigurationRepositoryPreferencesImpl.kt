package es.inteco.conanmobile.data.repository.preferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.inteco.conanmobile.data.entities.ConfigurationData
import es.inteco.conanmobile.data.entities.RegisterDeviceRequestData
import es.inteco.conanmobile.data.entities.RegisteredDeviceData
import es.inteco.conanmobile.domain.entities.ConfigurationEntity
import es.inteco.conanmobile.domain.repository.ConfigurationRepository
import es.inteco.conanmobile.utils.Consts
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Configuration repository preferences impl
 *
 * @property sharedPreferences
 * @constructor Create empty Configuration repository preferences impl
 */
class ConfigurationRepositoryPreferencesImpl(private val sharedPreferences: SharedPreferences) :
    ConfigurationRepository {

    override fun saveConfiguration(configuration: ConfigurationEntity): Completable =
        Completable.fromAction {
            sharedPreferences.edit()
                .putString(Consts.PREF_CONFIGURATION_SERVICE, Gson().toJson(configuration)).apply()
        }

    override fun getConfiguration(): Single<ConfigurationEntity> {
        return Single.just(
            Gson().fromJson(
                sharedPreferences.getString(
                    Consts.PREF_CONFIGURATION_SERVICE, null
                ), object : TypeToken<ConfigurationEntity>() {}.type
            )
        )
    }

    override fun registerDevice(body: RegisterDeviceRequestData): Single<RegisteredDeviceData> {
        TODO("Not yet implemented")
    }

    override fun getConfiguration(key: String): Single<ConfigurationData> {
        TODO("Not yet implemented")
    }
}