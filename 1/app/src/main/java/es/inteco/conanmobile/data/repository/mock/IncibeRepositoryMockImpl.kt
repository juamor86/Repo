package es.inteco.conanmobile.data.repository.mock

import android.content.Context
import es.inteco.conanmobile.data.entities.*
import es.inteco.conanmobile.domain.repository.IncibeRepository
import io.reactivex.rxjava3.core.Single

/**
 * Incibe repository mock impl
 *
 * @constructor
 *
 * @param context
 */
class IncibeRepositoryMockImpl(context: Context) : IncibeRepository {
    override fun getIpBotnet(): Single<IpBotnetData> =
        Single.just(IpBotnetData("", "", emptyList()))

    override fun getWarnings(key: String): Single<WarningsData> = Single.just(WarningsData())

    override fun isMaliciousApp(
        key: String,
        body: MaliciousAppRequestData
    ): Single<MaliciousAppData> =
        Single.just(MaliciousAppData("", 0L, "", MaliciousAppMessage("", emptyList()), ""))

    override fun postMaliciousApk(
        key: String,
        body: MaliciousApkRequestData
    ): Single<MaliciousApkData> =
        Single.just(MaliciousApkData("", 0L, "", MaliciousApkMessage(emptyList()), ""))

    override fun postAnalysisResult(
        key: String,
        body: AnalysisResultRequestData
    ): Single<PostAnalysisResultData> =
        Single.just(PostAnalysisResultData("", 0L, "", PostAnalysisResultMessage(""), ""))

    override fun getOSITips(key: String): Single<OSIData> = Single.just(OSIData())

    override fun getPendingWarnings(key: String): Single<PendingWarningsData> = Single.just(
        PendingWarningsData("", 0L, "", PendingWarningsMessageData(false), "")
    )
}