package es.inteco.conanmobile.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.factory.AnalysisRepositoryFactory
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.scheduler.TrampolineSchedulerProvider
import es.inteco.conanmobile.domain.entities.AnalysisEntity
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
class GetDefaultAnalysisUseCaseTest : BaseTest() {

    @Mock
    lateinit var preferencesRepositoryFactory: PreferencesRepositoryFactory

    @Mock
    lateinit var preferencesRepository: PreferencesRepository

    @Test
    fun getDefaultAnalysis_should_get_default_analysis_from_preferences() {
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)
        Mockito.`when`(preferencesRepository.getDefaultAnalysis())
            .thenReturn(
                Single.just(fakeAnalysis)
            )

        val observer = TestObserver<AnalysisEntity>()
        val useCase = createUseCase()
        useCase.execute(observer = observer)

        observer.assertComplete()

        verify(preferencesRepository).getDefaultAnalysis()
    }

    private fun createUseCase(): GetDefaultAnalysisUseCase =
        GetDefaultAnalysisUseCase(preferencesRepositoryFactory).apply {
            schedulerProvider = TrampolineSchedulerProvider()
        }

}