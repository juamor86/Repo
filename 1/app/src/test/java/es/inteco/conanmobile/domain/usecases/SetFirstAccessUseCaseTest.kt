package es.inteco.conanmobile.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.data.repository.mock.PreferencesRepositoryMockImpl
import es.inteco.conanmobile.data.repository.preferences.PreferencesRepositoryPreferencesImpl
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.scheduler.TrampolineSchedulerProvider
import es.inteco.conanmobile.domain.repository.PreferencesRepository
import es.inteco.conanmobile.utils.Consts
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.lang.RuntimeException

@RunWith(MockitoJUnitRunner::class)
class SetFirstAccessUseCaseTest {

    @Mock
    lateinit var preferencesRepositoryFactory: PreferencesRepositoryFactory

    @Mock
    lateinit var preferencesRepository: PreferencesRepository

    @Test
    fun setFirstAccessUseCase_should_set_first_access_in_preferences() {
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)
        Mockito.`when`(preferencesRepository.setIsFirstAccess())
            .thenReturn(
                Completable.complete()
            )

        val observer = TestObserver<Void>()
        val useCase = createUseCase()
        useCase.execute(observer = observer)

        observer.assertComplete()

        verify(preferencesRepository).setIsFirstAccess()
    }

    @Test
    fun setIsFirstAnalysisLaunched_does_not_complete_when_it_throws_any_error() {
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)
        Mockito.`when`(preferencesRepository.setIsFirstAccess())
            .thenReturn(
                Completable.error(RuntimeException())
            )

        val observer = TestObserver<Void>()
        val useCase = createUseCase()
        useCase.execute(observer = observer)

        observer.assertError(RuntimeException::class.java)
        observer.assertNotComplete()

        verify(preferencesRepository).setIsFirstAccess()
    }

    private fun createUseCase(): SetFirstAccessUseCase =
        SetFirstAccessUseCase(preferencesRepositoryFactory).apply {
            schedulerProvider = TrampolineSchedulerProvider()
        }

}