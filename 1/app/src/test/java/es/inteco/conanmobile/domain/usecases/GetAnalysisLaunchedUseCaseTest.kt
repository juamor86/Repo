package es.inteco.conanmobile.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.repository.PreferencesRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetAnalysisLaunchedUseCaseTest {

    @Mock
    lateinit var preferencesRepositoryFactory: PreferencesRepositoryFactory

    @Mock
    lateinit var preferencesRepository: PreferencesRepository

    @Test
    fun getFirstAnalysisLaunched_should_set_first_analysis_launched_access_in_preferences() {
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)
        Mockito.`when`(preferencesRepository.getDefaultAnalysisLaunched())
            .thenReturn(
                Mockito.anyBoolean()
            )

        val useCase = createUseCase()
        useCase.execute()

        verify(preferencesRepository).getDefaultAnalysisLaunched()
    }

    private fun createUseCase(): GetAnalysisLaunchedUseCase =
        GetAnalysisLaunchedUseCase(preferencesRepositoryFactory)

}