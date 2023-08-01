package es.inteco.conanmobile.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
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

@RunWith(MockitoJUnitRunner::class)
class SetNextAnalysisDateTimeUseCaseTest {

    @Mock
    lateinit var preferencesRepositoryFactory: PreferencesRepositoryFactory

    @Mock
    lateinit var preferencesRepository: PreferencesRepository

    private val params =
        SetNextAnalysisDateTimeUseCase.Params(nextDatetime = 2 * Consts.ONE_DAY_MILLIS)

    @Test
    fun setNextAvailableAnalysisDateTime_should_set_next_available_analysis_date_time_in_preferences() {
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)
        Mockito.`when`(preferencesRepository.setNextAvailableAnalysisDateTime(params.nextDatetime))
            .thenReturn(
                Completable.complete()
            )

        val observer = TestObserver<Void>()
        val useCase = createUseCase()
        useCase.execute(params, observer)

        observer.assertComplete()

        verify(preferencesRepository).setNextAvailableAnalysisDateTime(params.nextDatetime)
    }

    @Test
    fun setNextAvailableAnalysisDateTime_does_not_complete_when_it_throws_any_error() {
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)
        Mockito.`when`(preferencesRepository.setNextAvailableAnalysisDateTime(params.nextDatetime))
            .thenReturn(
                Completable.error(RuntimeException())
            )

        val observer = TestObserver<Void>()
        val useCase = createUseCase()
        useCase.execute(params, observer)

        observer.assertError(RuntimeException::class.java)
        observer.assertNotComplete()

        verify(preferencesRepository).setNextAvailableAnalysisDateTime(params.nextDatetime)
    }

    private fun createUseCase(): SetNextAnalysisDateTimeUseCase =
        SetNextAnalysisDateTimeUseCase(preferencesRepositoryFactory).apply {
            schedulerProvider = TrampolineSchedulerProvider()
        }

}