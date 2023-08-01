package es.inteco.conanmobile.data.repository.mock

import es.inteco.conanmobile.data.entities.OSIData
import es.inteco.conanmobile.data.entities.WarningsData
import es.inteco.conanmobile.domain.entities.AnalysisEntity
import es.inteco.conanmobile.domain.entities.RegisteredDeviceEntity
import es.inteco.conanmobile.domain.repository.PreferencesRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class PreferencesRepositoryMockImpl : PreferencesRepository {

    override fun getFirstAccess(): Boolean = true
    override fun setIsFirstAccess(): Completable = Completable.complete()

    override fun saveDefaultAnalysis(defaultAnalysis: AnalysisEntity): Completable =
        Completable.complete()

    override fun getDefaultAnalysis(): Single<AnalysisEntity> = Single.just(
        AnalysisEntity(
            "", listOf(), listOf(), listOf(), listOf(), listOf()
        )
    )

    override fun setDefaultAnalysisLaunched(launched: Boolean): Completable = Completable.complete()
    override fun getDefaultAnalysisLaunched(): Boolean = false
    override fun setNextAvailableAnalysisDateTime(datetime: Long): Completable =
        Completable.complete()

    override fun getNextAvailableAnalysisDateTime(): Long = -1
    override fun getFirstAnalysisLaunched(): Boolean = false
    override fun setIsFirstAnalysisLaunched(): Completable = Completable.complete()
    override fun saveDeviceRegister(device: RegisteredDeviceEntity): Completable =
        Completable.complete()

    override fun getDeviceRegister(): Single<RegisteredDeviceEntity> = Single.just(null)
    override fun saveOsiTips(osiTips: OSIData): Completable = Completable.complete()
    override fun getOsiTips(): Single<OSIData> = Single.just(null)
    override fun existOsiTips(): Boolean = false
    override fun saveWarnings(warnings: WarningsData): Completable = Completable.complete()
    override fun getWarnings(): Single<WarningsData> = Single.just(null)
    override fun existWarnings(): Boolean = false
    override fun getLastAlertAnalysis(): Long = 0
    override fun saveLastAlertAnalysis(time: Long) {
    }
}