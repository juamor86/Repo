package es.inteco.conanmobile.domain.usecases.analisys

import es.inteco.conanmobile.data.entities.MaliciousAppRequestData
import es.inteco.conanmobile.data.factory.IncibeRepositoryFactory
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.entities.MaliciousAppEntity
import es.inteco.conanmobile.domain.mappers.IsMaliciousAppMapper
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Get malicious app use case
 *
 * @property preferencesRepositoryFactory
 * @property incibeRepositoryFactory
 * @constructor Create empty Get malicious app use case
 */
class GetMaliciousAppUseCase @Inject constructor(
    private val preferencesRepositoryFactory: PreferencesRepositoryFactory,
    private val incibeRepositoryFactory: IncibeRepositoryFactory
) :
    SingleUseCase<GetMaliciousAppUseCase.Params, MaliciousAppEntity>() {

    override fun buildUseCase(params: Params?): Single<MaliciousAppEntity> {
        return preferencesRepositoryFactory.create(Strategy.PREFERENCES).getDeviceRegister()
            .flatMap { device ->
                incibeRepositoryFactory.create(Strategy.NETWORK)
                    .isMaliciousApp(device.message.key, MaliciousAppRequestData(params!!.hash)).map {
                    IsMaliciousAppMapper.convert(it)
                }
            }
    }

    data class Params(val hash: String)
}