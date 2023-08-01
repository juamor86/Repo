package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.CompletableUseCase
import es.inteco.conanmobile.domain.entities.RegisteredDeviceEntity
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

/**
 * Save device register use case
 *
 * @property preferencesRepositoryFactory
 * @constructor Create empty Save device register use case
 */
class SaveRegisteredDeviceUseCase @Inject constructor(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    CompletableUseCase<SaveRegisteredDeviceUseCase.Params>() {

    override fun buildUseCase(params: Params?): Completable =
        preferencesRepositoryFactory.create(Strategy.PREFERENCES).saveDeviceRegister(params!!.device)

    data class Params(val device: RegisteredDeviceEntity)
}