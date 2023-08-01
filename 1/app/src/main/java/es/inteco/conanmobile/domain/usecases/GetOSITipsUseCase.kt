package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.entities.OSIData
import es.inteco.conanmobile.data.factory.IncibeRepositoryFactory
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.entities.OSIEntity
import es.inteco.conanmobile.domain.entities.WarningEntity
import es.inteco.conanmobile.domain.mappers.WarningsMapper
import es.inteco.conanmobile.utils.Consts
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Get o s i tips use case
 *
 * @property preferencesRepositoryFactory
 * @property incibeRepositoryFactory
 * @constructor Create empty Get o s i tips use case
 */
class GetOSITipsUseCase @Inject constructor(
    private val preferencesRepositoryFactory: PreferencesRepositoryFactory,
    private val incibeRepositoryFactory: IncibeRepositoryFactory
) : SingleUseCase<Void, List<OSIEntity>>() {

    override fun buildUseCase(params: Void?): Single<List<OSIEntity>> {
        with(preferencesRepositoryFactory.create(Strategy.PREFERENCES)) {
            return if (existOsiTips()) {
                val osiData = getOsiTips().blockingGet()
                if (osiData.timestamp.toLong() + Consts.ONE_DAY_MILLIS < System.currentTimeMillis()) {
                    getOSITipsFromNetwork()
                } else {
                    Single.just(convert(osiData))
                }
            } else {
                getOSITipsFromNetwork()
            }
        }
    }

    private fun getOSITipsFromNetwork(): Single<List<OSIEntity>> {
        val prefRepo = preferencesRepositoryFactory.create(Strategy.PREFERENCES)
        return preferencesRepositoryFactory.create(Strategy.PREFERENCES).getDeviceRegister()
            .flatMap { device ->
                incibeRepositoryFactory.create(Strategy.NETWORK).getOSITips(device.message.key)
                    .map {
                        prefRepo.saveOsiTips(it).blockingSubscribe()
                        convert(it)
                    }
            }
    }

    /**
     * Convert
     *
     * @param data
     * @return
     */
    fun convert(data: OSIData): List<OSIEntity> {
        return data.message.osiTips.map { tip ->
            OSIEntity(tip.id, tip.title.associate {
                it.key.lowercase() to it.value
            }, tip.description.associate {
                it.key.lowercase() to it.value
            })
        }
    }
}
