package es.inteco.conanmobile.domain.usecases.analisys

import es.inteco.conanmobile.data.entities.Data
import es.inteco.conanmobile.data.entities.MaliciousApkRequestData
import es.inteco.conanmobile.data.factory.IncibeRepositoryFactory
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.entities.MaliciousApkEntity
import es.inteco.conanmobile.domain.mappers.MaliciousApkMapper
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Get malicious a p k use case
 *
 * @property preferencesRepositoryFactory
 * @property incibeRepositoryFactory
 * @constructor Create empty Get malicious a p k use case
 */
class GetMaliciousAPKUseCase @Inject constructor(
    private val preferencesRepositoryFactory: PreferencesRepositoryFactory,
    private val incibeRepositoryFactory: IncibeRepositoryFactory
) :
    SingleUseCase<GetMaliciousAPKUseCase.Params, MaliciousApkEntity>() {

    override fun buildUseCase(params: Params?): Single<MaliciousApkEntity> {
        return preferencesRepositoryFactory.create(Strategy.PREFERENCES).getDeviceRegister()
            .flatMap { device ->
                incibeRepositoryFactory.create(Strategy.NETWORK)
                    .postMaliciousApk(device.message.key, MaliciousApkRequestData(params!!.data)).map {
                        MaliciousApkMapper.convert(it)
                    }
            }
    }

    data class Params(val data: List<Data>)

}