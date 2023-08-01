package es.juntadeandalucia.msspa.saludandalucia.domain.usecases

import es.juntadeandalucia.msspa.saludandalucia.data.factory.PreferencesRepositoryFactory
import es.juntadeandalucia.msspa.saludandalucia.domain.Strategy
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.cache.TrustListCache
import es.juntadeandalucia.msspa.saludandalucia.utils.Consts
import io.reactivex.Single

class GetCovidCertTrustListUseCase(private val preferencesRepositoryFactory: PreferencesRepositoryFactory) :
    SingleUseCase<TrustListCache>() {

    var trustList: TrustListCache? = null

    override fun buildUseCase(): Single<TrustListCache> {
        if (trustList != null && isValidCache(trustList!!.creation)) {
            return Single.just(trustList)
        }
        return preferencesRepositoryFactory.create(Strategy.PREFERENCES).getCovidTrustList()
            .flatMap {
                if (isValidCache(it.creation)) {
                    this.trustList = it
                    Single.just(it)
                } else {
                    preferencesRepositoryFactory.create(Strategy.NETWORK).getCovidTrustList()
                        .flatMap {
                            this.trustList = it
                            preferencesRepositoryFactory.create(Strategy.PREFERENCES)
                                .saveCovidTrustList(it).toSingle { it }
                        }
                }
            }
    }

    private fun isValidCache(creation: Long): Boolean {
        return creation != 0L && System.currentTimeMillis() - creation < Consts.TRUSTLIST_CACHE
    }
}
