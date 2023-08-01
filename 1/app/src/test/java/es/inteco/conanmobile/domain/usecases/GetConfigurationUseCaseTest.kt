package es.inteco.conanmobile.domain.usecases

import com.nhaarman.mockitokotlin2.verify
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.data.entities.ConfigurationData
import es.inteco.conanmobile.data.factory.ConfigurationRepositoryFactory
import es.inteco.conanmobile.data.factory.PreferencesRepositoryFactory
import es.inteco.conanmobile.domain.Strategy
import es.inteco.conanmobile.domain.base.scheduler.TrampolineSchedulerProvider
import es.inteco.conanmobile.domain.entities.ConfigurationEntity
import es.inteco.conanmobile.domain.mappers.ConfigurationMapper
import es.inteco.conanmobile.domain.repository.ConfigurationRepository
import es.inteco.conanmobile.domain.repository.PreferencesRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetConfigurationUseCaseTest : BaseTest() {

    @Mock
    lateinit var preferencesRepositoryFactory: PreferencesRepositoryFactory

    @Mock
    lateinit var preferencesRepository: PreferencesRepository

    @Mock
    lateinit var configurationRepositoryFactory: ConfigurationRepositoryFactory

    @Mock
    lateinit var configurationRepository: ConfigurationRepository

    private val params = GetConfigurationUseCase.Params(key = ANY_KEY)

    private val fakeConfigurationData = ConfigurationData()



    @Test
    fun getConfiguration_should_get_configuration_from_network_if_key_is_not_empty() {
        Mockito.`when`(configurationRepositoryFactory.create(Strategy.NETWORK))
            .thenReturn(configurationRepository)
        Mockito.`when`(configurationRepository.getConfiguration(params.key))
            .thenReturn(
                Single.just(fakeConfigurationData)
            )


        val observer = TestObserver<ConfigurationEntity>()
        val useCase = createUseCase()
        useCase.execute(
            params = params,
            observer = observer
        )

        observer.assertComplete()
        observer.assertValue(ConfigurationMapper.convert(fakeConfigurationData))

        verify(configurationRepository).getConfiguration(params.key)
    }

    @Test
    fun getConfiguration_should_get_configuration_from_preferences_if_key_is_empty() {
        Mockito.`when`(preferencesRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(preferencesRepository)
        Mockito.`when`(preferencesRepository.getDeviceRegister())
            .thenReturn(
                Single.just(fakeRegisteredDevice)
            )
        Mockito.`when`(configurationRepositoryFactory.create(Strategy.PREFERENCES))
            .thenReturn(configurationRepository)
        Mockito.`when`(configurationRepository.getConfiguration())
            .thenReturn(
                Single.just(fakeConfiguration)
            )

        val observer = TestObserver<ConfigurationEntity>()
        val useCase = createUseCase()
        useCase.execute(
            params = null,
            observer = observer
        )

        observer.assertComplete()
        observer.assertValue(fakeConfiguration)

        verify(preferencesRepository).getDeviceRegister()
        verify(configurationRepository).getConfiguration()
    }


    private fun createUseCase(): GetConfigurationUseCase =
        GetConfigurationUseCase(
            preferencesRepositoryFactory,
            configurationRepositoryFactory
        ).apply {
            schedulerProvider = TrampolineSchedulerProvider()
        }

}