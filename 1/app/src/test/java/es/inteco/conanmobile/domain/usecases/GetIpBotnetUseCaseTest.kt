package es.inteco.conanmobile.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.factory.IncibeRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.scheduler.TrampolineSchedulerProvider
import es.inteco.conanmobile.domain.entities.IpBotnetEntity
import es.inteco.conanmobile.domain.repository.IncibeRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetIpBotnetUseCaseTest : BaseTest() {

    @Mock
    lateinit var incibeRepositoryFactory: IncibeRepositoryFactory

    @Mock
    lateinit var incibeRepository: IncibeRepository

    @Test
    fun gGetIpBotnet_should_get_ipbotnet() {
        val responseData = fakeIpBotnetData

        Mockito.`when`(incibeRepositoryFactory.create(Strategy.NETWORK))
            .thenReturn(incibeRepository)

        Mockito.`when`(incibeRepository.getIpBotnet())
            .thenReturn(
                Single.just(responseData)
            )

        val observer = TestObserver<IpBotnetEntity>()
        val useCase = createUseCase()
        useCase.execute(observer = observer)

        observer.assertComplete()
        observer.assertValue { entity ->
            with(entity) {
                ip == fakeIpBotnetEntity.ip &&
                error == fakeIpBotnetEntity.error
            }
        }

        verify(incibeRepository).getIpBotnet()
    }

    @Test
    fun registerDevice_does_not_complete_when_it_throws_any_error() {
        Mockito.`when`(incibeRepositoryFactory.create(Strategy.NETWORK))
            .thenReturn(incibeRepository)
        Mockito.`when`(incibeRepository.getIpBotnet())
            .thenReturn(
                Single.error(RuntimeException())
            )

        val observer = TestObserver<IpBotnetEntity>()
        val useCase = createUseCase()
        useCase.execute( observer = observer)

        observer.assertError(RuntimeException::class.java)
        observer.assertNotComplete()

        verify(incibeRepository).getIpBotnet()
    }

    private fun createUseCase(): GetIpBotnetUseCase =
        GetIpBotnetUseCase(incibeRepositoryFactory).apply {
            schedulerProvider = TrampolineSchedulerProvider()
        }

}