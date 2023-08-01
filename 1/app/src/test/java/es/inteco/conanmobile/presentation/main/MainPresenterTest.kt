package es.inteco.conanmobile.presentation.main

import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.base.SynchronousUseCase
import es.inteco.conanmobile.domain.base.utils.FakeErrorSingleUseCase
import es.inteco.conanmobile.domain.base.utils.FakeSuccessSingleUseCase
import es.inteco.conanmobile.domain.base.utils.FakeSynchronousUseCase
import es.inteco.conanmobile.domain.entities.AnalysisEntity
import es.inteco.conanmobile.domain.entities.AnalysisResultEntity
import es.inteco.conanmobile.domain.entities.ConfigurationEntity
import es.inteco.conanmobile.domain.entities.PendingWarningsEntity
import es.inteco.conanmobile.domain.usecases.GetConfigurationUseCase
import es.inteco.conanmobile.presentation.analysis.AnalysisController
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest : BaseTest() {

    @Mock
    lateinit var view: MainContract.View

    @Mock
    lateinit var getConfigurationUseCase: SingleUseCase<GetConfigurationUseCase.Params, ConfigurationEntity>

    @Mock
    lateinit var getDefaultAnalysisUseCase: SingleUseCase<Void, AnalysisEntity>

    @Mock
    lateinit var existLastAnalysisUseCase: SingleUseCase<Void, Boolean>

    @Mock
    lateinit var getLastAnalysisUseCase: SingleUseCase<Void, AnalysisResultEntity>

    @Mock
    lateinit var analysisController: AnalysisController

    @Mock
    lateinit var checkPendingWarningsUseCase: SingleUseCase<Void, PendingWarningsEntity>

    @Mock
    lateinit var getLastAlertAnalysisUseCase: SynchronousUseCase<Void, Long>

    @Mock
    lateinit var saveLastAlertAnalysisUseCase: SynchronousUseCase<Long, Unit>

    private lateinit var presenter: MainPresenter

    @Before
    fun setup() {
        presenter = createPresenter()
    }

    private fun createPresenter(
        getConfigurationUseCase: SingleUseCase<GetConfigurationUseCase.Params, ConfigurationEntity> = this.getConfigurationUseCase,
        getDefaultAnalysisUseCase: SingleUseCase<Void, AnalysisEntity> = this.getDefaultAnalysisUseCase,
        existLastAnalysisUseCase: SingleUseCase<Void, Boolean> = this.existLastAnalysisUseCase,
        getLastAnalysisUseCase: SingleUseCase<Void, AnalysisResultEntity> = this.getLastAnalysisUseCase,
        analysisController: AnalysisController = this.analysisController,
        checkPendingWarningsUseCase: SingleUseCase<Void, PendingWarningsEntity> = this.checkPendingWarningsUseCase,
        getLastAlertAnalysisUseCase: SynchronousUseCase<Void, Long> = this.getLastAlertAnalysisUseCase,
        saveLastAlertAnalysisUseCase: SynchronousUseCase<Long, Unit> = this.saveLastAlertAnalysisUseCase
    ) = MainPresenter(
        getConfigurationUseCase = getConfigurationUseCase,
        getDefaultAnalysisUseCase = getDefaultAnalysisUseCase,
        existLastAnalysisUseCase = existLastAnalysisUseCase,
        getLastAnalysisUseCase = getLastAnalysisUseCase,
        analysisController = analysisController,
        checkPendingWarningsUseCase = checkPendingWarningsUseCase,
        getLastAlertAnalysisUseCase = getLastAlertAnalysisUseCase,
        saveLastAlertAnalysisUseCase = saveLastAlertAnalysisUseCase
    ).also { it.setViewContract(view) }

    @Test
    fun onCreate_should_init_controls_with_default_analysis() {
        presenter =
            createPresenter(getDefaultAnalysisUseCase = FakeSuccessSingleUseCase(fakeDefaultAnalysis)).apply {
                onCreate()
            }

        assertEquals(presenter.defaultAnalysis, fakeDefaultAnalysis)
        verify(view).initControlsWithDefaultAnalysis(fakeDefaultAnalysis)
    }

    @Test
    fun onCreate_does_nothing_when_default_analysis_fails() {
        presenter = createPresenter(getDefaultAnalysisUseCase = FakeErrorSingleUseCase()).apply {
            onCreate()
        }

        verify(view, never()).initControlsWithDefaultAnalysis(fakeDefaultAnalysis)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onCreate_should_get_configuration() {
        presenter =
            createPresenter(getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration)).apply {
                onCreate()
            }

        assertEquals(presenter.message, fakeConfiguration.message)
    }

    @Test
    fun onCreate_does_nothing_when_get_configuration_fails() {
        presenter = createPresenter(getConfigurationUseCase = FakeErrorSingleUseCase()).apply {
            onCreate()
        }

        verifyNoMoreInteractions(view)
    }

    @Test
    fun onCreate_should_get_pending_warnings() {
        presenter =
            createPresenter(checkPendingWarningsUseCase = FakeSuccessSingleUseCase(mock = fakePendingWarnings)).apply {
                onCreate()
            }

        verify(view).showPendingWarnings(fakePendingWarnings.message.haveNotifications)
    }

    @Test
    fun onCreate_does_nothing_when_get_pending_warnings_fails() {
        presenter = createPresenter(checkPendingWarningsUseCase = FakeErrorSingleUseCase()).apply {
            onCreate()
        }

        verifyNoMoreInteractions(view)
    }

    @Test
    fun onCreate_should_show_last_analysis_if_exists() {
        presenter = createPresenter(
            getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration),
            existLastAnalysisUseCase = FakeSuccessSingleUseCase(true)
        ).apply {
            onCreate()
        }
        verify(view).showViewLastAnalysis(true)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onCreate_should_hide_last_analysis_if_does_not_exist() {
        presenter = createPresenter(
            getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration),
            existLastAnalysisUseCase = FakeSuccessSingleUseCase(false)
        ).apply {
            onCreate()
        }
        verify(view).showViewLastAnalysis(false)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onCreate_should_hide_last_analysis_when_get_last_analysis_fails() {
        presenter = createPresenter(
            getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration),
            existLastAnalysisUseCase = FakeErrorSingleUseCase()
        ).apply {
            onCreate()
        }
        verify(view).showViewLastAnalysis(false)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onCreate_should_get_last_analysis_and_show_recommended_analysis_alert_if_time_between_analysis_exceed() {
        presenter = createPresenter(
            getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration),
            existLastAnalysisUseCase = FakeSuccessSingleUseCase(true),
            getLastAnalysisUseCase = FakeSuccessSingleUseCase(mock = fakeAnalysisResult),
            getLastAlertAnalysisUseCase = FakeSynchronousUseCase(mock = 0L)
        ).apply {
            onCreate()
        }

        assertEquals(presenter.lastAnalysis, fakeAnalysisResult)

        verify(view).showViewLastAnalysis(true)
        verify(view).showAlertRecommendedAnalysis()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onLastAnalysisClicked_should_navigate_to_last_analysis() {
        presenter = createPresenter(
            getLastAnalysisUseCase = FakeSuccessSingleUseCase(fakeAnalysisResult),
        ).apply {
            onLastAnalysisClicked()
        }
        verify(view).navigateToResults(fakeAnalysisResult)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onLastAnalysisClicked_should_show_error_when_get_last_analysis_fails() {
        presenter = createPresenter(
            getLastAnalysisUseCase = FakeErrorSingleUseCase()
        ).apply {
            onLastAnalysisClicked()
        }
        verify(view).showErrorDialog()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onStartAnalysisClicked_should_check_location_enabled() {
        presenter.onStartAnalysisClicked()
        verify(view).isLocationPermitted()
    }

    @Test
    fun onLocationPermissionGranted_should_navigate_to_analysis() {
        presenter = createPresenter(
            getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration),
            getDefaultAnalysisUseCase = FakeSuccessSingleUseCase(fakeDefaultAnalysis),
            existLastAnalysisUseCase = FakeSuccessSingleUseCase(true),
            getLastAnalysisUseCase = FakeSuccessSingleUseCase(mock = fakeAnalysisResult)
        ).apply {
            onCreate()
            onLocationPermissionGranted()
        }

        verify(view).navigateToAnalysis(fakeDefaultAnalysis)
    }

    @Test
    fun onLegalClicked_should_navigate_to_legal_screen() {
        presenter = createPresenter(
            getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration)
        ).apply {
            onCreate()
            onLegalClicked()
        }

        verify(view).navigateToLegalScreen(fakeConfiguration.message)
    }

    @Test
    fun onHelpClicked_should_navigate_to_help_screen() {
        presenter.onHelpClicked()

        verify(view).navigateToHelpScreen()
    }


    @Test
    fun unsubscribe_should_clear_all_use_cases() {
        presenter.unsubscribe()

        verify(getConfigurationUseCase).clear()
    }
}
