package es.inteco.conanmobile.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.OSIData
import es.inteco.conanmobile.data.factory.IncibeRepositoryFactory
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.scheduler.TrampolineSchedulerProvider
import es.inteco.conanmobile.domain.entities.OSIEntity
import es.inteco.conanmobile.domain.repository.IncibeRepository
import es.inteco.conanmobile.domain.repository.PreferencesRepository
import es.inteco.conanmobile.utils.Consts
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetOSITipsUseCaseTest : BaseTest() {

    @Mock
    lateinit var preferencesRepositoryFactory: PreferencesRepositoryFactory

    @Mock
    lateinit var incibeRepositoryFactory: IncibeRepositoryFactory

    @Mock
    lateinit var preferencesRepository: PreferencesRepository

    @Mock
    lateinit var incibeRepository: IncibeRepository

    private val oldTimestamp = System.currentTimeMillis() - 2 * Consts.ONE_DAY_MILLIS
    private val fakeOldOSIData = OSIData(timestamp = oldTimestamp.toString())
    private val fakeValidOSIData = OSIData(timestamp = System.currentTimeMillis().toString())

    @Test
    fun it_should_get_osi_tips_from_network_and_save_them_in_preferences_if_existing_osi_tips_in_preferences_are_not_valid() {
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)
        Mockito.`when`(incibeRepositoryFactory.create(Strategy.NETWORK))
            .thenReturn(incibeRepository)

        Mockito.`when`(preferencesRepository.existOsiTips())
            .thenReturn(
                true
            )
        Mockito.`when`(preferencesRepository.getOsiTips())
            .thenReturn(
                Single.just(fakeOldOSIData)
            )
        Mockito.`when`(preferencesRepository.getDeviceRegister())
            .thenReturn(
                Single.just(fakeRegisteredDevice)
            )
        Mockito.`when`(preferencesRepository.saveOsiTips(fakeOldOSIData))
            .thenReturn(
                Completable.complete()
            )
        Mockito.`when`(incibeRepository.getOSITips(fakeRegisteredDevice.message.key))
            .thenReturn(
                Single.just(fakeOldOSIData)
            )

        val observer = TestObserver<List<OSIEntity>>()
        val useCase = createUseCase()
        useCase.execute(observer = observer)

        observer.assertComplete()

        verify(preferencesRepository).existOsiTips()
        verify(preferencesRepository).getOsiTips()
        verify(preferencesRepository).getDeviceRegister()
        verify(preferencesRepository).saveOsiTips(fakeOldOSIData)
        verify(incibeRepository).getOSITips(fakeRegisteredDevice.message.key)
    }

    @Test
    fun it_should_get_osi_tips_from_preferences_if_existing_osi_tips_in_preferences_are_valid() {
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)
        Mockito.`when`(preferencesRepository.existOsiTips())
            .thenReturn(
                true
            )
        Mockito.`when`(preferencesRepository.getOsiTips())
            .thenReturn(
                Single.just(fakeValidOSIData)
            )

        val observer = TestObserver<List<OSIEntity>>()
        val useCase = createUseCase()
        useCase.execute(observer = observer)

        observer.assertComplete()

        observer.assertValue(useCase.convert(fakeValidOSIData))

        verify(preferencesRepository).existOsiTips()
        verify(preferencesRepository).getOsiTips()
    }

    @Test
    fun it_should_get_osi_tips_from_network_and_save_them_in_preferences_if_does_not_exits_osi_tips_in_preferences() {
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)
        Mockito.`when`(incibeRepositoryFactory.create(Strategy.NETWORK))
            .thenReturn(incibeRepository)

        Mockito.`when`(preferencesRepository.existOsiTips())
            .thenReturn(
                false
            )
        Mockito.`when`(preferencesRepository.getDeviceRegister())
            .thenReturn(
                Single.just(fakeRegisteredDevice)
            )
        Mockito.`when`(preferencesRepository.saveOsiTips(fakeOldOSIData))
            .thenReturn(
                Completable.complete()
            )
        Mockito.`when`(incibeRepository.getOSITips(fakeRegisteredDevice.message.key))
            .thenReturn(
                Single.just(fakeOldOSIData)
            )

        val observer = TestObserver<List<OSIEntity>>()
        val useCase = createUseCase()
        useCase.execute(observer = observer)

        observer.assertComplete()

        verify(preferencesRepository).existOsiTips()
        verify(preferencesRepository).getDeviceRegister()
        verify(preferencesRepository).saveOsiTips(fakeOldOSIData)
        verify(incibeRepository).getOSITips(fakeRegisteredDevice.message.key)
    }

    private fun createUseCase(): GetOSITipsUseCase =
        GetOSITipsUseCase(preferencesRepositoryFactory, incibeRepositoryFactory).apply {
            schedulerProvider = TrampolineSchedulerProvider()
        }

}