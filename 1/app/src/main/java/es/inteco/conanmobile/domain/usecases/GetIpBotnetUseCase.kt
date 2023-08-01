package es.inteco.conanmobile.domain.usecases

import es.inteco.conanmobile.data.factory.IncibeRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.entities.IpBotnetEntity
import es.inteco.conanmobile.domain.mappers.IpBotnetMapper
import io.reactivex.rxjava3.core.Single
/**
 * Get ip botnet use case
 *
 * @property incibeRepositoryFactory
 * @constructor Create empty Get ip botnet use case
 */

class GetIpBotnetUseCase(
    private val incibeRepositoryFactory: IncibeRepositoryFactory
) : SingleUseCase<Void, IpBotnetEntity>() {

    override fun buildUseCase(params: Void?): Single<IpBotnetEntity> =
        incibeRepositoryFactory.create(Strategy.NETWORK).run {
            getIpBotnet().map {
                IpBotnetMapper.convert(it)
            }
        }
}