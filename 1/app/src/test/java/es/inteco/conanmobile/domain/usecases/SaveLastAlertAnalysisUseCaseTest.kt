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
import kotlin.random.Random

@RunWith(MockitoJUnitRunner::class)
class SaveLastAlertAnalysisUseCaseTest {

    @Mock
    lateinit var preferencesRepositoryFactory: PreferencesRepositoryFactory

    @Mock
    lateinit var preferencesRepository: PreferencesRepository

    private val anyTime = Random.nextLong()

    @Test
    fun saveLastAlertAnalysis_should_save_last_alert_analysis_in_preferences() {
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)

        val useCase = createUseCase()
        useCase.execute(anyTime)

        verify(preferencesRepository).saveLastAlertAnalysis(anyTime)
    }

    private fun createUseCase(): SaveLastAlertAnalysisUseCase =
        SaveLastAlertAnalysisUseCase(preferencesRepositoryFactory)

}