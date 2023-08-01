package es.inteco.conanmobile.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.factory.AnalysisRepositoryFactory
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.scheduler.TrampolineSchedulerProvider
import es.inteco.conanmobile.domain.repository.AnalysisRepository
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
class ExistLastAnalysisUseCaseTest: BaseTest() {

    @Mock
    lateinit var analysisRepositoryFactory: AnalysisRepositoryFactory

    @Mock
    lateinit var analysisRepository: AnalysisRepository

    @Test
    fun isLastAnalysis_should_check_if_is_last_analysis_in_preferences() {
        Mockito.`when`(analysisRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(analysisRepository)
        Mockito.`when`(analysisRepository.isLastAnalysis())
            .thenReturn(
                Single.just(true)
            )

        val observer = TestObserver<Boolean>()
        val useCase = createUseCase()
        useCase.execute(observer = observer)

        observer.assertComplete()
        observer.assertValue(true)

        verify(analysisRepository).isLastAnalysis()
    }

    @Test
    fun isLastAnalysis_does_not_complete_when_it_throws_any_error() {
        Mockito.`when`(analysisRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(analysisRepository)
        Mockito.`when`(analysisRepository.isLastAnalysis())
            .thenReturn(
                Single.error(RuntimeException())
            )

        val observer = TestObserver<Boolean>()
        val useCase = createUseCase()
        useCase.execute( observer = observer)

        observer.assertError(RuntimeException::class.java)
        observer.assertNotComplete()

        verify(analysisRepository).isLastAnalysis()
    }

    private fun createUseCase(): ExistLastAnalysisUseCase =
        ExistLastAnalysisUseCase(analysisRepositoryFactory).apply {
            schedulerProvider = TrampolineSchedulerProvider()
        }

}