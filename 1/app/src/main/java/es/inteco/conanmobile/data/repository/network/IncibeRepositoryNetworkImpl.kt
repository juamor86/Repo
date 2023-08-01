package es.inteco.conanmobile.data.repository.network

import es.inteco.conanmobile.data.api.CONANApi
import es.inteco.conanmobile.data.entities.*
import es.inteco.conanmobile.domain.repository.IncibeRepository
import io.reactivex.rxjava3.core.Single

/**
 * Incibe repository network impl
 *
 * @property conanApi
 * @constructor Create empty Incibe repository network impl
 */
class IncibeRepositoryNetworkImpl(private val conanApi: CONANApi) :
    IncibeRepository {

    override fun getIpBotnet(): Single<IpBotnetData> {
        return conanApi.getIpBotnet()
    }

    override fun getWarnings(key: String): Single<WarningsData> {
        return conanApi.getWarnings(key = key)
    }

    override fun getOSITips(key: String): Single<OSIData> {
        return conanApi.getOsiTips(key = key)
    }

    override fun isMaliciousApp(key: String, body : MaliciousAppRequestData): Single<MaliciousAppData> {
        return conanApi.isMaliciousApp(key = key, body = body)
    }

    override fun postAnalysisResult(key: String, body: AnalysisResultRequestData): Single<PostAnalysisResultData> {
        return conanApi.postAnalysisResult(key = key, body = body)
    }

    override fun postMaliciousApk(key: String, body : MaliciousApkRequestData): Single<MaliciousApkData> {
        return conanApi.postMaliciousApk(key = key, body = body)
    }

    override fun getPendingWarnings(key: String): Single<PendingWarningsData> = conanApi.getPendingWarnings(key = key)
}