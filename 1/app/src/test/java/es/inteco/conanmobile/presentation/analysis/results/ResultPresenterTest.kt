package es.inteco.conanmobile.presentation.analysis.results

import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.domain.entities.ModuleEntity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ResultPresenterTest : BaseTest() {

    @Mock
    lateinit var view: ResultsContract.View

    private lateinit var presenter: ResultsPresenter

    @Before
    fun setup() {
        presenter = createPresenter()
    }

    @Test
    fun onViewCreated_should_init_screen_buttons_and_toolbar() {
        presenter.onViewCreated(fakeAnalysisResult, fakeAnalysisResult)
        verify(view).toolbarTittle()
        verify(view).initButtons()
        verify(view).loadResults(0, 0, 0)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onDeviceClicked_should_show_detail_result() {
        presenter.apply {
            result = fakeAnalysisResult
            previousAnalysis = fakeAnalysisResult
        }
        presenter.onDeviceClicked()
        verify(view).navigateDetailResult(
            fakeAnalysisResult,
            fakeAnalysisResult,
            ModuleEntity.AnalysisType.SETTING
        )
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onAppsClicked_should_show_apps_screen() {
        presenter.onAppsClicked()
        verify(view).navigateToApps()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onPermissionClicked_should_show_permission_screen() {
        presenter.apply {
            previousAnalysis = fakeAnalysisResult
            result = fakeAnalysisResult
        }
        presenter.onPermissionClicked()
        verify(view).navigateDetailResult(fakeAnalysisResult, fakeAnalysisResult, ModuleEntity.AnalysisType.SYSTEM)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onNavigateToOSIClicked_should_navigate_to_osi_tips() {
        presenter.onNavigateToOSIClicked()
        verify(view).navigateOSITips()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onNavigateToWhatsapp_should_navigate_to_whatsapp() {
        presenter.onNavigateToWhatsapp()
        verify(view).navigateToWhatsapp()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun whatsappNotInstalled_should_show_warning() {
        presenter.whatsappNotInstalled()
        verify(view).showWarningIntentWhatsapp()
        verifyNoMoreInteractions(view)
    }

    private fun createPresenter() = ResultsPresenter().also { it.setViewContract(view) }
}

