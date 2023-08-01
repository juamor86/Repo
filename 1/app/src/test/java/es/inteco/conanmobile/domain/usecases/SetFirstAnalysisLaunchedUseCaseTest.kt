package es.inteco.conanmobile.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.scheduler.TrampolineSchedulerProvider
import es.inteco.conanmobile.domain.repository.PreferencesRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SetFirstAnalysisLaunchedUseCaseTest {

    @Mock
    lateinit var preferencesRepositoryFactory: PreferencesRepositoryFactory

    @Mock
    lateinit var preferencesRepository: PreferencesRepository

    @Test
    fun setIsFirstAnalysisLaunched_should_set_that_first_analysis_has_been_launched_in_preferences() {
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)
        Mockito.`when`(preferencesRepository.setIsFirstAnalysisLaunched())
            .thenReturn(
                Completable.complete()
            )

        val observer = TestObserver<Void>()
        val useCase = createUseCase()
        useCase.execute(observer = observer)

        observer.assertComplete()

        verify(preferencesRepository).setIsFirstAnalysisLaunched()
    }

    @Test
    fun setIsFirstAnalysisLaunched_does_not_complete_when_it_throws_any_error() {
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)
        Mockito.`when`(preferencesRepository.setIsFirstAnalysisLaunched())
            .thenReturn(
                Completable.error(RuntimeException())
            )

        val observer = TestObserver<Void>()
        val useCase = createUseCase()
        useCase.execute(observer = observer)

        observer.assertError(RuntimeException::class.java)
        observer.assertNotComplete()

        verify(preferencesRepository).setIsFirstAnalysisLaunched()
    }

    private fun createUseCase(): SetFirstAnalysisLaunchedUseCase =
        SetFirstAnalysisLaunchedUseCase(preferencesRepositoryFactory).apply {
            schedulerProvider = TrampolineSchedulerProvider()
        }

}