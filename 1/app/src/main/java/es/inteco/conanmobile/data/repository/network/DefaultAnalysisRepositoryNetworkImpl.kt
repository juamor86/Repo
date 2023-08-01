package es.inteco.conanmobile.data.repository.network

import es.inteco.conanmobile.data.api.CONANApi
import es.inteco.conanmobile.data.entities.DefaultAnalysisListData
import es.inteco.conanmobile.domain.repository.DefaultAnalysisRepository
import io.reactivex.rxjava3.core.Single


/**
 * Default analysis repository network impl
 *
 * @property msspaApi
 * @constructor Create empty Default analysis repository network impl
 */
class DefaultAnalysisRepositoryNetworkImpl(private val msspaApi: CONANApi) :
    DefaultAnalysisRepository {

    override fun getAnalysis(): Single<DefaultAnalysisListData> {
        TODO("Not yet implemented")
    }
}