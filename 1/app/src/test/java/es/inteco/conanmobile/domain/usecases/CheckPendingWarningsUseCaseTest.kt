package es.inteco.conanmobile.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.PendingWarningsData
import es.inteco.conanmobile.data.entities.PendingWarningsMessageData
import es.inteco.conanmobile.data.factory.IncibeRepositoryFactory
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.scheduler.TrampolineSchedulerProvider
import es.inteco.conanmobile.domain.entities.PendingWarningsEntity
import es.inteco.conanmobile.domain.repository.IncibeRepository
import es.inteco.conanmobile.domain.repository.PreferencesRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CheckPendingWarningsUseCaseTest : BaseTest() {

    @Mock
    lateinit var preferencesRepositoryFactory: PreferencesRepositoryFactory
    @Mock
    lateinit var incibeRepositoryFactory: IncibeRepositoryFactory
    @Mock
    lateinit var preferencesRepository: PreferencesRepository
    @Mock
    lateinit var incibeRepository: IncibeRepository

    @Test
    fun getPendinWarnings_should_get_pending_warnings() {
        val responseData = PendingWarningsData(
            timestamp = ANY_TIMESTAMP_STR,
            status = ANY_STATUS,
            statusMessage = ANY_STATUS_MESSAGE,
            message = PendingWarningsMessageData(ANY_BOOLEAN),
            path = ANY_PATH
        )
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)

        Mockito.`when`(preferencesRepository.getDeviceRegister())
            .thenReturn(
                Single.just(fakeRegisteredDevice)
            )

        Mockito.`when`(incibeRepositoryFactory.create(Strategy.NETWORK))
            .thenReturn(incibeRepository)

        Mockito.`when`(incibeRepository.getPendingWarnings(ANY_KEY))
            .thenReturn(
                Single.just(responseData)
            )

        val observer = TestObserver<PendingWarningsEntity>()
        val useCase = createUseCase()
        useCase.execute(observer = observer)

        observer.assertComplete()
        observer.assertValue { entity ->
            with(entity) {
                timestamp == ANY_TIMESTAMP_STR
                        && status == ANY_STATUS
                        && statusMessage == ANY_STATUS_MESSAGE
                        && message.haveNotifications == ANY_BOOLEAN
                        && path == ANY_PATH
            }
        }

        verify(incibeRepository).getPendingWarnings(ANY_KEY)
    }

    @Test
    fun checkPendingWarnings_does_not_complete_when_it_throws_any_error() {
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)
        Mockito.`when`(preferencesRepository.getDeviceRegister())
            .thenReturn(
                Single.just(fakeRegisteredDevice)
            )
        Mockito.`when`(incibeRepositoryFactory.create(Strategy.NETWORK))
            .thenReturn(incibeRepository)
        Mockito.`when`(incibeRepository.getPendingWarnings(ANY_KEY))
            .thenReturn(
                Single.error(RuntimeException())
            )

        val observer = TestObserver<PendingWarningsEntity>()
        val useCase = createUseCase()
        useCase.execute( observer = observer)

        observer.assertError(RuntimeException::class.java)
        observer.assertNotComplete()

        verify(incibeRepository).getPendingWarnings(ANY_KEY)
    }

    private fun createUseCase(): CheckPendingWarningsUseCase =
        CheckPendingWarningsUseCase(preferencesRepositoryFactory, incibeRepositoryFactory).apply {
            schedulerProvider = TrampolineSchedulerProvider()
        }

}