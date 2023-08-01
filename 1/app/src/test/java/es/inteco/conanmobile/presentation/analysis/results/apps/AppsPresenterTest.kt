package es.inteco.conanmobile.presentation.analysis.results.apps

import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.base.utils.FakeSuccessSingleUseCase
import es.inteco.conanmobile.domain.entities.ApplicationEntity
import es.inteco.conanmobile.domain.entities.ConfigurationEntity
import es.inteco.conanmobile.domain.usecases.GetConfigurationUseCase
import es.inteco.conanmobile.presentation.analysis.AnalysisController
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AppsPresenterTest : BaseTest() {

    @Mock
    private lateinit var view: AppsContract.View

    @Mock
    private lateinit var analysisController: AnalysisController

    @Mock
    private lateinit var getConfigurationUseCase: SingleUseCase<GetConfigurationUseCase.Params, ConfigurationEntity>

    private val fakeMaliciousApplication = ApplicationEntity(
        isMalicious = 1,
        criticalPermissions = listOf(fakePermissionEntity)
    )

    private lateinit var presenter: AppsPresenter

    @Before
    fun setup() {

    }

    @Test
    fun onViewCreated_should_init_screen() {
        Mockito.`when`(analysisController.result).thenReturn(fakeAnalysisResult)
        presenter = createPresenter(getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration)).apply {
            onViewCreated()
        }

        assertEquals(presenter.configurationEntity, fakeConfiguration)

        verify(view).showLoading()
        verify(view).hideLoading()
        verify(view).setDotColorGreen()
        verify(view).showEmptyView()
        verify(view).setTitleApplications()
        verify(view).setAppsAnalyzed(0)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onViewCreated_should_malicious_apps() {
        Mockito.`when`(analysisController.result).thenReturn(fakeAnalysisResult.apply { appsItems = mutableListOf(fakeMaliciousApplication) })
        presenter = createPresenter(getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration)).apply {
            onViewCreated()
        }

        assertEquals(presenter.configurationEntity, fakeConfiguration)

        verify(view).showLoading()
        verify(view).hideLoading()
        verify(view).setTitleApplications()
        verify(view).setSubtitle(fakeConfiguration.message.maliciousAppsDescription)
        verify(view).setAppsAnalyzed(1)
        verify(view).setDotColorRed()
        verify(view).hideOk()
        verify(view).fillMaliciousList(mutableListOf(fakeMaliciousApplication))
        verify(view).showMaliciousButtonPressed()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onMaliciousClicked_should_show_malicious_list() {
        Mockito.`when`(analysisController.result).thenReturn(fakeAnalysisResult.apply { appsItems = mutableListOf(fakeMaliciousApplication) })
        presenter = createPresenter(getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration)).apply {
            onViewCreated()
        }

        reset(view)

        presenter.onMaliciousClicked()

        verify(view).hideOk()
        verify(view).showMaliciousButtonPressed()
        verify(view).setSubtitle(fakeConfiguration.message.maliciousAppsDescription)
        verify(view).fillMaliciousList(mutableListOf(fakeMaliciousApplication))
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onPermissionsClicked_should_show_permissions_list() {
        Mockito.`when`(analysisController.result).thenReturn(fakeAnalysisResult.apply { appsItems = mutableListOf(fakeMaliciousApplication) })
        presenter = createPresenter(getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration)).apply {
            onViewCreated()
        }

        reset(view)

        presenter.onPermissionClicked()

        verify(view).hideOk()
        verify(view).showAttentionButtonPressed()
        verify(view).hideSubtitle()
        verify(view).fillPermissionList(mutableListOf(fakeMaliciousApplication))
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onPermissionsAppClicked_should_show_app() {
        Mockito.`when`(analysisController.result).thenReturn(fakeAnalysisResult.apply { appsItems = mutableListOf(fakeMaliciousApplication) })
        presenter = createPresenter(getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration)).apply {
            onViewCreated()
        }

        reset(view)

        presenter.onPermissionAppClicked(fakeMaliciousApplication)

        verify(view).showApp(fakeMaliciousApplication)
        verifyNoMoreInteractions(view)
    }



    private fun createPresenter(
        analysisController: AnalysisController = this.analysisController,
        getConfigurationUseCase: SingleUseCase<GetConfigurationUseCase.Params, ConfigurationEntity> = this.getConfigurationUseCase
    ) =
        AppsPresenter(
            controller = analysisController,
            getConfigurationUseCase = getConfigurationUseCase
        ).also { it.setViewContract(view) }
}

