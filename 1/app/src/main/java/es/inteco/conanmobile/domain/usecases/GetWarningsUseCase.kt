package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.IncibeRepositoryFactory
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.entities.WarningEntity
import es.inteco.conanmobile.domain.mappers.WarningsMapper.Companion.convert
import es.inteco.conanmobile.utils.Consts
import io.reactivex.rxjava3.core.Single

/**
 * Get warnings use case
 *
 * @property preferencesRepositoryFactory
 * @property incibeRepositoryFactory
 * @constructor Create empty Get warnings use case
 */
class GetWarningsUseCase(
    private val preferencesRepositoryFactory: PreferencesRepositoryFactory,
    private val incibeRepositoryFactory: IncibeRepositoryFactory
) : SingleUseCase<Void, List<WarningEntity>>() {

    override fun buildUseCase(params: Void?): Single<List<WarningEntity>> {
        with(preferencesRepositoryFactory.create(Strategy.PREFERENCES)) {
            return if (existWarnings()) {
                val warningsData = getWarnings().blockingGet()
                if (warningsData.timestamp + Consts.ONE_DAY_MILLIS < System.currentTimeMillis()) {
                    getWarningsByNetwork()
                } else {
                    Single.just(convert(warningsData))
                }
            } else {
                getWarningsByNetwork()
            }
        }
    }

    private fun getWarningsByNetwork(): Single<List<WarningEntity>> {
        val prefRepo = preferencesRepositoryFactory.create(Strategy.PREFERENCES)
        return preferencesRepositoryFactory.create(Strategy.PREFERENCES).getDeviceRegister()
            .flatMap { device ->
                incibeRepositoryFactory.create(Strategy.NETWORK).getWarnings(device.message.key)
                    .map {
                        prefRepo.saveWarnings(it).blockingSubscribe()
                        convert(it)
                    }
            }
    }

}