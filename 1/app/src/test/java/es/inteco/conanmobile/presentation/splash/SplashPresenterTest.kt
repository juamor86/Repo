package es.inteco.conanmobile.presentation.splash

import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.domain.base.CompletableUseCase
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.base.SynchronousUseCase
import es.inteco.conanmobile.domain.base.utils.*
import es.inteco.conanmobile.domain.entities.*
import es.inteco.conanmobile.domain.usecases.*
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SplashPresenterTest: BaseTest() {

    @Mock
    lateinit var view: SplashContract.View

    @Mock
    lateinit var getConfigurationUseCase: SingleUseCase<GetConfigurationUseCase.Params, ConfigurationEntity>

    @Mock
    lateinit var setFirstAccessUseCase: CompletableUseCase<Void>

    @Mock
    lateinit var getFirstAccessUseCase: SynchronousUseCase<Void, Boolean>

    @Mock
    lateinit var saveDefaultAnalysisUseCase: CompletableUseCase<SaveDefaultAnalysisUseCase.Params>

    @Mock
    lateinit var getDefaultAnalysisUseCase: SingleUseCase<Void, AnalysisEntity>

    @Mock
    lateinit var registerDeviceUseCase: SingleUseCase<RegisterDeviceUseCase.Params, RegisteredDeviceEntity>

    @Mock
    lateinit var saveRegisteredDeviceUseCase: CompletableUseCase<SaveRegisteredDeviceUseCase.Params>

    @Mock
    lateinit var saveConfigurationUseCase: CompletableUseCase<SaveConfigurationUseCase.Params>

    private lateinit var presenter: SplashPresenter

    @Before
    fun setup() {
        presenter = createPresenter()
    }

    @Test
    fun onCreate_should_check_internet_connectivity() {
        presenter.onCreate(fakeIdDevice)
        assertEquals(presenter.idDevice, fakeIdDevice)
        verify(view).checkInternetConnectivity()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onAvailableInternetConnectivity_should_show_splash_screen() {
        presenter = createPresenter().apply {
            idDevice = fakeIdDevice
        }
        presenter.onAvailableInternetConnectivity()
        assertEquals(presenter.isInternetConnectivityAvailable, true)
        verify(view).showSplashScreen()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onUnavailableInternetConnectivity_should_show_splash_screen() {
        presenter.onUnavailableInternetConnectivity()
        assertEquals(presenter.isInternetConnectivityAvailable, false)
        verify(view).showNoInternetScreen()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onAvailableInternetConnectivity_should_show_no_configuration_screen_when_register_device_fails() {
        presenter = createPresenter(registerDeviceUseCase = FakeErrorSingleUseCase()).apply {
            idDevice = fakeIdDevice
        }
        presenter.onAvailableInternetConnectivity()

        verify(view).showSplashScreen()
        verify(view).showNoConfigurationScreen()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onAvailableInternetConnectivity_should_register_device_and_save_registered_device() {
        presenter = createPresenter(
            registerDeviceUseCase = FakeSuccessSingleUseCase(mock = fakeRegisteredDevice),
            saveRegisteredDeviceUseCase = FakeSuccessCompletableUseCase()
        ).apply {
            idDevice = fakeIdDevice
        }
        presenter.onAvailableInternetConnectivity()
        assertEquals(presenter.isInternetConnectivityAvailable, true)
        verify(view).showSplashScreen()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onAvailableInternetConnectivity_should_register_device_and_does_nothing_when_save_registered_device_fails() {
        presenter = createPresenter(
            registerDeviceUseCase = FakeSuccessSingleUseCase(mock = fakeRegisteredDevice),
            saveRegisteredDeviceUseCase = FakeErrorCompletableUseCase()
        ).apply {
            idDevice = fakeIdDevice
        }
        presenter.onAvailableInternetConnectivity()
        assertEquals(presenter.isInternetConnectivityAvailable, true)
        verify(view).showSplashScreen()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onAvailableInternetConnectivity_should_register_device_and_show_no_configuration_screen_when_get_configuration_fails() {
        presenter = createPresenter(
            registerDeviceUseCase = FakeSuccessSingleUseCase(mock = fakeRegisteredDevice),
            getConfigurationUseCase = FakeErrorSingleUseCase()
        ).apply {
            idDevice = fakeIdDevice
        }
        presenter.onAvailableInternetConnectivity()
        assertEquals(presenter.isInternetConnectivityAvailable, true)
        verify(view).showSplashScreen()
        verify(view).showNoConfigurationScreen()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onAvailableInternetConnectivity_should_register_device_and_get_configuration_and_show_terms_and_conditions_in_first_access() {
        presenter = createPresenter(
            registerDeviceUseCase = FakeSuccessSingleUseCase(mock = fakeRegisteredDevice),
            getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration),
            getFirstAccessUseCase = FakeSynchronousUseCase(mock = true),
            saveConfigurationUseCase = FakeSuccessCompletableUseCase()
        ).apply {
            idDevice = fakeIdDevice
        }
        presenter.onAvailableInternetConnectivity()

        assertEquals(presenter.isInternetConnectivityAvailable, true)
        assertEquals(presenter.configurationEntity, fakeConfiguration)

        verify(view).showSplashScreen()
        verify(view).showTermsAndConditionsScreen()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onAvailableInternetConnectivity_should_register_device_and_get_configuration_and_does_not_show_terms_and_conditions_if_not_first_access() {
        presenter = createPresenter(
            registerDeviceUseCase = FakeSuccessSingleUseCase(mock = fakeRegisteredDevice),
            getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration),
            getFirstAccessUseCase = FakeSynchronousUseCase(mock = false),
            saveConfigurationUseCase = FakeSuccessCompletableUseCase()
        ).apply {
            idDevice = fakeIdDevice
        }
        presenter.onAvailableInternetConnectivity()

        assertEquals(presenter.isInternetConnectivityAvailable, true)
        assertEquals(presenter.configurationEntity, fakeConfiguration)

        verify(view).showSplashScreen()
        verify(view, never()).showTermsAndConditionsScreen()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onAvailableInternetConnectivity_should_register_device_and_get_configuration_and_does_nothing_when_get_default_analysis_fails() {
        presenter = createPresenter(
            registerDeviceUseCase = FakeSuccessSingleUseCase(mock = fakeRegisteredDevice),
            getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration),
            getFirstAccessUseCase = FakeSynchronousUseCase(mock = true),
            getDefaultAnalysisUseCase = FakeErrorSingleUseCase(),
            saveConfigurationUseCase = FakeSuccessCompletableUseCase()
        ).apply {
            idDevice = fakeIdDevice
        }
        presenter.onAvailableInternetConnectivity()

        assertEquals(presenter.isInternetConnectivityAvailable, true)
        assertEquals(presenter.configurationEntity, fakeConfiguration)

        verify(view).showSplashScreen()
        verify(view).showTermsAndConditionsScreen()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onAvailableInternetConnectivity_should_register_device_and_get_configuration_and_get_default_analysis() {
        presenter = createPresenter(
            registerDeviceUseCase = FakeSuccessSingleUseCase(mock = fakeRegisteredDevice),
            getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration),
            getFirstAccessUseCase = FakeSynchronousUseCase(mock = true),
            getDefaultAnalysisUseCase = FakeSuccessSingleUseCase(mock = fakeDefaultAnalysis),
            saveConfigurationUseCase = FakeSuccessCompletableUseCase()
        ).apply {
            idDevice = fakeIdDevice
        }
        presenter.onAvailableInternetConnectivity()

        assertEquals(presenter.isInternetConnectivityAvailable, true)
        assertEquals(presenter.configurationEntity, fakeConfiguration)

        verify(view).showSplashScreen()
        verify(view).showTermsAndConditionsScreen()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onAvailableInternetConnectivity_should_register_device_and_get_configuration_and_get_default_analysis_and_does_nothing_when_save_default_analysis_fails() {
        presenter = createPresenter(
            registerDeviceUseCase = FakeSuccessSingleUseCase(mock = fakeRegisteredDevice),
            getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration),
            getFirstAccessUseCase = FakeSynchronousUseCase(mock = true),
            getDefaultAnalysisUseCase = FakeSuccessSingleUseCase(mock = fakeDefaultAnalysis),
            saveConfigurationUseCase = FakeSuccessCompletableUseCase(),
            saveDefaultAnalysisUseCase = FakeErrorCompletableUseCase()
        ).apply {
            idDevice = fakeIdDevice
        }
        presenter.onAvailableInternetConnectivity()

        assertEquals(presenter.isInternetConnectivityAvailable, true)
        assertEquals(presenter.configurationEntity, fakeConfiguration)

        verify(view).showSplashScreen()
        verify(view).showTermsAndConditionsScreen()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onAvailableInternetConnectivity_should_register_device_and_get_configuration_and_get_default_analysis_and_save_default_analysis() {
        presenter = createPresenter(
            registerDeviceUseCase = FakeSuccessSingleUseCase(mock = fakeRegisteredDevice),
            getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration),
            getFirstAccessUseCase = FakeSynchronousUseCase(mock = true),
            saveConfigurationUseCase = FakeSuccessCompletableUseCase(),
            getDefaultAnalysisUseCase = FakeSuccessSingleUseCase(mock = fakeDefaultAnalysis),
            saveDefaultAnalysisUseCase = FakeSuccessCompletableUseCase()
        ).apply {
            idDevice = fakeIdDevice
        }
        presenter.onAvailableInternetConnectivity()

        assertEquals(presenter.isInternetConnectivityAvailable, true)
        assertEquals(presenter.configurationEntity, fakeConfiguration)

        verify(view).showSplashScreen()
        verify(view).showTermsAndConditionsScreen()
        verifyNoMoreInteractions(view)
    }


    @Test
    fun onAvailableInternetConnectivity_should_show_no_internet_connection_when_there_is_no_internet_connection() {
        presenter = createPresenter(
            registerDeviceUseCase = FakeSuccessSingleUseCase(mock = fakeRegisteredDevice),
            getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration),
            getFirstAccessUseCase = FakeSynchronousUseCase(mock = true),
            saveConfigurationUseCase = FakeSuccessCompletableUseCase(),
            getDefaultAnalysisUseCase = FakeSuccessSingleUseCase(mock = fakeDefaultAnalysis),
            saveDefaultAnalysisUseCase = FakeSuccessCompletableUseCase()
        ).apply {
            idDevice = fakeIdDevice
        }
        presenter.onAvailableInternetConnectivity()

        assertEquals(presenter.isInternetConnectivityAvailable, true)
        assertEquals(presenter.configurationEntity, fakeConfiguration)

        verify(view).showSplashScreen()
        verify(view).showTermsAndConditionsScreen()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onAcceptButtonClicked_should_not_navigate_to_home_screen_when_set_first_access_fails() {
        presenter = createPresenter(
            registerDeviceUseCase = FakeSuccessSingleUseCase(mock = fakeRegisteredDevice),
            getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration),
            getFirstAccessUseCase = FakeSynchronousUseCase(mock = true),
            setFirstAccessUseCase = FakeErrorCompletableUseCase()
        ).apply {
            idDevice = fakeIdDevice
            onAvailableInternetConnectivity()
        }
        presenter.onAcceptButtonClicked()

        verify(view, never()).navigateToHomeScreen(fakeConfiguration.message)
    }

    @Test
    fun onAcceptButtonClicked_should_navigate_to_home_screen_when_it_sets_first_access() {
        presenter = createPresenter(
            registerDeviceUseCase = FakeSuccessSingleUseCase(mock = fakeRegisteredDevice),
            getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration),
            saveConfigurationUseCase = FakeSuccessCompletableUseCase(),
            getFirstAccessUseCase = FakeSynchronousUseCase(mock = true),
            setFirstAccessUseCase = FakeSuccessCompletableUseCase()
        ).apply {
            idDevice = fakeIdDevice
            onAvailableInternetConnectivity()
        }
        presenter.onAcceptButtonClicked()

        verify(view).navigateToHomeScreen(fakeConfiguration.message)
    }

    @Test
    fun onLegalClicked_should_navigate_to_home_screen_when_it_sets_first_access() {
        presenter = createPresenter(
            registerDeviceUseCase = FakeSuccessSingleUseCase(mock = fakeRegisteredDevice),
            getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration),
            saveConfigurationUseCase = FakeSuccessCompletableUseCase(),
            getFirstAccessUseCase = FakeSynchronousUseCase(mock = true),
            setFirstAccessUseCase = FakeSuccessCompletableUseCase()
        ).apply {
            idDevice = fakeIdDevice
            onAvailableInternetConnectivity()
        }
        presenter.onLegalClicked()

        verify(view).navigateToLegalScreen(fakeConfiguration.message)
    }

    @Test
    fun unsubscribe_should_clear_all_use_cases() {
        presenter.unsubscribe()

        verify(getConfigurationUseCase).clear()
        verify(setFirstAccessUseCase).clear()
        verify(saveDefaultAnalysisUseCase).clear()
        verify(getDefaultAnalysisUseCase).clear()
        verify(registerDeviceUseCase).clear()
        verify(saveRegisteredDeviceUseCase).clear()
    }

    private fun createPresenter(
        getConfigurationUseCase: SingleUseCase<GetConfigurationUseCase.Params, ConfigurationEntity> = this.getConfigurationUseCase,
        setFirstAccessUseCase: CompletableUseCase<Void> = this.setFirstAccessUseCase,
        getFirstAccessUseCase: SynchronousUseCase<Void, Boolean> = this.getFirstAccessUseCase,
        saveDefaultAnalysisUseCase: CompletableUseCase<SaveDefaultAnalysisUseCase.Params> = this.saveDefaultAnalysisUseCase,
        getDefaultAnalysisUseCase: SingleUseCase<Void, AnalysisEntity> = this.getDefaultAnalysisUseCase,
        registerDeviceUseCase: SingleUseCase<RegisterDeviceUseCase.Params, RegisteredDeviceEntity> = this.registerDeviceUseCase,
        saveRegisteredDeviceUseCase: CompletableUseCase<SaveRegisteredDeviceUseCase.Params> = this.saveRegisteredDeviceUseCase,
        saveConfigurationUseCase: CompletableUseCase<SaveConfigurationUseCase.Params> = this.saveConfigurationUseCase
    ) = SplashPresenter(
        getConfigurationUseCase = getConfigurationUseCase,
        setFirstAccessUseCase = setFirstAccessUseCase,
        getFirstAccessUseCase = getFirstAccessUseCase,
        saveDefaultAnalysisUseCase = saveDefaultAnalysisUseCase,
        getDefaultAnalysisUseCase = getDefaultAnalysisUseCase,
        registerDeviceUseCase = registerDeviceUseCase,
        saveRegisteredDeviceUseCase = saveRegisteredDeviceUseCase,
        saveConfigurationUseCase = saveConfigurationUseCase
    ).also { it.setViewContract(view) }
}
