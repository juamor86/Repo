package es.inteco.conanmobile.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.WarningsData
import es.inteco.conanmobile.data.factory.IncibeRepositoryFactory
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.scheduler.TrampolineSchedulerProvider
import es.inteco.conanmobile.domain.entities.WarningEntity
import es.inteco.conanmobile.domain.mappers.WarningsMapper
import es.inteco.conanmobile.domain.repository.IncibeRepository
import es.inteco.conanmobile.domain.repository.PreferencesRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetWarningsUseCaseTest : BaseTest() {

    @Mock
    lateinit var preferencesRepositoryFactory: PreferencesRepositoryFactory

    @Mock
    lateinit var incibeRepositoryFactory: IncibeRepositoryFactory

    @Mock
    lateinit var preferencesRepository: PreferencesRepository

    @Mock
    lateinit var incibeRepository: IncibeRepository

    private val fakeOldWarningData = WarningsData()
    private val fakeValidWarningData = WarningsData(timestamp = System.currentTimeMillis())

    @Test
    fun it_should_get_warnings_from_network_and_save_them_in_preferences_if_existing_warnings_in_preferences_are_not_valid() {
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)
        Mockito.`when`(incibeRepositoryFactory.create(Strategy.NETWORK))
            .thenReturn(incibeRepository)

        Mockito.`when`(preferencesRepository.existWarnings())
            .thenReturn(
                true
            )
        Mockito.`when`(preferencesRepository.getWarnings())
            .thenReturn(
                Single.just(fakeOldWarningData)
            )
        Mockito.`when`(preferencesRepository.getDeviceRegister())
            .thenReturn(
                Single.just(fakeRegisteredDevice)
            )
        Mockito.`when`(preferencesRepository.saveWarnings(fakeOldWarningData))
            .thenReturn(
                Completable.complete()
            )
        Mockito.`when`(incibeRepository.getWarnings(fakeRegisteredDevice.message.key))
            .thenReturn(
                Single.just(fakeOldWarningData)
            )

        val observer = TestObserver<List<WarningEntity>>()
        val useCase = createUseCase()
        useCase.execute(observer = observer)

        observer.assertComplete()

        verify(preferencesRepository).existWarnings()
        verify(preferencesRepository).getWarnings()
        verify(preferencesRepository).getDeviceRegister()
        verify(preferencesRepository).saveWarnings(fakeOldWarningData)
        verify(incibeRepository).getWarnings(fakeRegisteredDevice.message.key)
    }

    @Test
    fun it_should_get_warnings_from_preferences_if_existing_warnings_in_preferences_are_valid() {
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)
        Mockito.`when`(preferencesRepository.existWarnings())
            .thenReturn(
                true
            )
        Mockito.`when`(preferencesRepository.getWarnings())
            .thenReturn(
                Single.just(fakeValidWarningData)
            )

        val observer = TestObserver<List<WarningEntity>>()
        val useCase = createUseCase()
        useCase.execute(observer = observer)

        observer.assertComplete()

        observer.assertValue(WarningsMapper.convert(fakeValidWarningData))

        verify(preferencesRepository).existWarnings()
        verify(preferencesRepository).getWarnings()
    }

    @Test
    fun it_should_get_warnings_from_network_and_save_them_in_preferences_if_does_not_exits_warnings_in_preferences() {
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)
        Mockito.`when`(incibeRepositoryFactory.create(Strategy.NETWORK))
            .thenReturn(incibeRepository)

        Mockito.`when`(preferencesRepository.existWarnings())
            .thenReturn(
                false
            )
        Mockito.`when`(preferencesRepository.getDeviceRegister())
            .thenReturn(
                Single.just(fakeRegisteredDevice)
            )
        Mockito.`when`(preferencesRepository.saveWarnings(fakeOldWarningData))
            .thenReturn(
                Completable.complete()
            )
        Mockito.`when`(incibeRepository.getWarnings(fakeRegisteredDevice.message.key))
            .thenReturn(
                Single.just(fakeOldWarningData)
            )

        val observer = TestObserver<List<WarningEntity>>()
        val useCase = createUseCase()
        useCase.execute(observer = observer)

        observer.assertComplete()

        verify(preferencesRepository).existWarnings()
        verify(preferencesRepository).getDeviceRegister()
        verify(preferencesRepository).saveWarnings(fakeOldWarningData)
        verify(incibeRepository).getWarnings(fakeRegisteredDevice.message.key)
    }

    private fun createUseCase(): GetWarningsUseCase =
        GetWarningsUseCase(preferencesRepositoryFactory, incibeRepositoryFactory).apply {
            schedulerProvider = TrampolineSchedulerProvider()
        }

}