package es.inteco.conanmobile.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.factory.AnalysisRepositoryFactory
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.scheduler.TrampolineSchedulerProvider
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.OSIEntity
import es.inteco.conanmobile.domain.repository.AnalysisRepository
import es.inteco.conanmobile.domain.repository.PreferencesRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetLastAnalysisUseCaseTest : BaseTest() {

    @Mock
    lateinit var analysisRepositoryFactory: AnalysisRepositoryFactory

    @Mock
    lateinit var analysisRepository: AnalysisRepository

    @Test
    fun getLastAnalysis_should_get_last_analysis_from_preferences() {
        Mockito.`when`(analysisRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(analysisRepository)
        Mockito.`when`(analysisRepository.getLastAnalysis())
            .thenReturn(
                Single.just(fakeAnalysisResult)
            )

        val observer = TestObserver<AnalysisResultEntity>()
        val useCase = createUseCase()
        useCase.execute(observer = observer)

        observer.assertComplete()

        verify(analysisRepository).getLastAnalysis()
    }

    private fun createUseCase(): GetLastAnalysisUseCase =
        GetLastAnalysisUseCase(analysisRepositoryFactory).apply {
            schedulerProvider = TrampolineSchedulerProvider()
        }

}