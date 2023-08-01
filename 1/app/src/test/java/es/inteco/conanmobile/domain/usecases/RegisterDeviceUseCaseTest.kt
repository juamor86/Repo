package es.inteco.conanmobile.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.MessageKey
import es.inteco.conanmobile.data.entities.RegisterDeviceRequestData
import es.inteco.conanmobile.data.entities.RegisteredDeviceData
import es.inteco.conanmobile.data.factory.ConfigurationRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.scheduler.TrampolineSchedulerProvider
import es.inteco.conanmobile.domain.entities.RegisteredDeviceEntity
import es.inteco.conanmobile.domain.repository.ConfigurationRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterDeviceUseCaseTest : BaseTest() {

    @Mock
    lateinit var configurationRepositoryFactory: ConfigurationRepositoryFactory

    @Mock
    lateinit var configurationRepository: ConfigurationRepository

    private val params: RegisterDeviceUseCase.Params =
        RegisterDeviceUseCase.Params(idDevice = fakeIdDevice)

    private val registerDeviceRequestData = RegisterDeviceRequestData(params.idDevice)


    @Test
    fun registerDevice_should_register_device() {
        val responseData = RegisteredDeviceData(
            timestamp = ANY_TIMESTAMP,
            status = ANY_STATUS,
            statusMessage = ANY_STATUS_MESSAGE,
            message = MessageKey(idTerminal = ANY_ID_TERMINAL, key = ANY_KEY),
            path = ANY_PATH
        )
        Mockito.`when`(configurationRepositoryFactory.create(Strategy.NETWORK))
            .thenReturn(configurationRepository)

        Mockito.`when`(
            configurationRepository.registerDevice(registerDeviceRequestData)
        ).thenReturn(
                Single.just(responseData)
            )

        val observer = TestObserver<RegisteredDeviceEntity>()
        val useCase = createUseCase()
        useCase.execute(params = params,  observer = observer)

        observer.assertComplete()
        observer.assertValue { entity ->
            with(entity) {
                timestamp == ANY_TIMESTAMP && status == ANY_STATUS && statusMessage == ANY_STATUS_MESSAGE && message.idTerminal == ANY_ID_TERMINAL && message.key == ANY_KEY && path == ANY_PATH
            }
        }

        verify(configurationRepository).registerDevice(RegisterDeviceRequestData(params.idDevice))
    }

    @Test
    fun registerDevice_does_not_complete_when_it_throws_any_error() {
        Mockito.`when`(configurationRepositoryFactory.create(Strategy.NETWORK))
            .thenReturn(configurationRepository)
        Mockito.`when`(configurationRepository.registerDevice(RegisterDeviceRequestData(params!!.idDevice)))
            .thenReturn(
                Single.error(RuntimeException())
            )

        val observer = TestObserver<RegisteredDeviceEntity>()
        val useCase = createUseCase()
        useCase.execute(params = params, observer = observer)

        observer.assertError(RuntimeException::class.java)
        observer.assertNotComplete()

        verify(configurationRepository).registerDevice(RegisterDeviceRequestData(params.idDevice))
    }

    private fun createUseCase(): RegisterDeviceUseCase =
        RegisterDeviceUseCase(configurationRepositoryFactory).apply {
            schedulerProvider = TrampolineSchedulerProvider()
        }

}