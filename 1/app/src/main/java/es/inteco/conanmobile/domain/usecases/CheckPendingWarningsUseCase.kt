package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.IncibeRepositoryFactory
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.entities.PendingWarningsEntity
import es.inteco.conanmobile.domain.mappers.PendingWarningsMapper
import io.reactivex.rxjava3.core.Single

/**
 * Check pending warnings use case
 *
 * @property preferencesRepositoryFactory
 * @property incibeRepositoryFactory
 * @constructor Create empty Check pending warnings use case
 */
class CheckPendingWarningsUseCase(
    private val preferencesRepositoryFactory: PreferencesRepositoryFactory,
    private val incibeRepositoryFactory: IncibeRepositoryFactory
) : SingleUseCase<Void, PendingWarningsEntity>() {

    override fun buildUseCase(params: Void?): Single<PendingWarningsEntity> {
        return preferencesRepositoryFactory.create(Strategy.PREFERENCES).getDeviceRegister()
            .flatMap { device ->
                incibeRepositoryFactory.create(Strategy.NETWORK)
                    .getPendingWarnings(device.message.key)
                    .map {
                        PendingWarningsMapper.convert(it)
                    }
            }
    }
}