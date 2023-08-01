package es.inteco.conanmobile.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.factory.AnalysisRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.scheduler.TrampolineSchedulerProvider
import es.inteco.conanmobile.domain.repository.AnalysisRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SaveAnalysisUseCaseTest : BaseTest() {

    @Mock
    lateinit var analysisRepositoryFactory: AnalysisRepositoryFactory

    @Mock
    lateinit var analysisRepository: AnalysisRepository

    private val params = SaveAnalysisUseCase.Params(fakeAnalysisResult)

    @Test
    fun saveAnalysis_should_save_analysis_in_preferences() {
        Mockito.`when`(analysisRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(analysisRepository)
        Mockito.`when`(analysisRepository.saveAnalysis(params.analysisResult))
            .thenReturn(
                Completable.complete()
            )

        val observer = TestObserver<Void>()
        val useCase = createUseCase()
        useCase.execute(params = params, observer = observer)

        observer.assertComplete()

        verify(analysisRepository).saveAnalysis(params.analysisResult)
    }

    @Test
    fun saveDefaultAnalysis_does_not_complete_when_it_throws_any_error() {
        Mockito.`when`(analysisRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(analysisRepository)
        Mockito.`when`(analysisRepository.saveAnalysis(params.analysisResult))
            .thenReturn(
                Completable.error(RuntimeException())
            )

        val observer = TestObserver<Void>()
        val useCase = createUseCase()
        useCase.execute(params = params, observer = observer)

        observer.assertError(RuntimeException::class.java)
        observer.assertNotComplete()

        verify(analysisRepository).saveAnalysis(params.analysisResult)
    }

    private fun createUseCase(): SaveAnalysisUseCase =
        SaveAnalysisUseCase(analysisRepositoryFactory).apply {
            schedulerProvider = TrampolineSchedulerProvider()
        }

}