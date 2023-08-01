package es.inteco.conanmobile.presentation.analysis.results.settings

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.domain.entities.ApplicationEntity
import es.inteco.conanmobile.domain.entities.AssessmentEntity
import es.inteco.conanmobile.domain.entities.ModuleEntity
import es.inteco.conanmobile.domain.entities.ModuleResultEntity
import es.inteco.conanmobile.presentation.analysis.AnalysisController
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SettingsPresenterTest : BaseTest() {

    @Mock
    lateinit var view: SettingsContract.View

    @Mock
    lateinit var analysisController: AnalysisController

    private lateinit var presenter: SettingsPresenter

    private val fakeApplication = ApplicationEntity()

    @Before
    fun setup() {
        presenter = createPresenter()
    }

    @Test
    fun onCreateView_should_show_settings_when_analysis_type_is_settings() {
        presenter.onCreateView(fakeAnalysisResult,fakeAnalysisResult, ModuleEntity.AnalysisType.SETTING)

        assertEquals(presenter.result, fakeAnalysisResult)
        verify(view).setToolbarTitleSettings()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onCreateView_should_show_nor_incidences_nor_attentions_when_all_is_ok() {
        presenter.onCreateView(fakeAnalysisResult.apply {
            deviceItems = mutableListOf(fakeModuleResult)
        }, fakeAnalysisResult, ModuleEntity.AnalysisType.SETTING)

        assertEquals(presenter.result, fakeAnalysisResult)
        assert(presenter.incidencesList.isEmpty())
        assert(presenter.attentionList.isEmpty())

        verify(view).setToolbarTitleSettings()
        verify(view).hideOkScreen()
        verify(view).refillRecyclerView(presenter.incidencesList, presenter.attentionList)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onCreateView_should_show_incidences_when_one_critical_module_is_not_ok() {
        presenter.onCreateView(fakeAnalysisResult.apply {
            deviceItems = mutableListOf(
                fakeModuleResult.apply {
                    notOk = true
                    item.assessment.criticality = "CRITICAL"
                }
            )
        }, fakeAnalysisResult, ModuleEntity.AnalysisType.SETTING)

        assertEquals(presenter.result, fakeAnalysisResult)
        assert(presenter.incidencesList.isNotEmpty())
        assert(presenter.attentionList.isEmpty())

        verify(view).setToolbarTitleSettings()
        verify(view).hideOkScreen()
        verify(view).refillRecyclerView(presenter.incidencesList, presenter.attentionList)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onCreateView_should_show_incidences_when_one_module_is_not_ok() {
        presenter.onCreateView(fakeAnalysisResult.apply {
            deviceItems = mutableListOf(
                fakeModuleResult.apply {
                    notOk = true
                    item.assessment.criticality = ""
                }
            )
        }, fakeAnalysisResult, ModuleEntity.AnalysisType.SETTING)

        assertEquals(presenter.result, fakeAnalysisResult)
        assert(presenter.incidencesList.isEmpty())
        assert(presenter.attentionList.isNotEmpty())

        verify(view).setToolbarTitleSettings()
        verify(view).hideOkScreen()
        verify(view).refillRecyclerView(presenter.incidencesList, presenter.attentionList)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onCreateView_should_show_apps_when_analysis_type_is_application() {
        presenter.onCreateView(fakeAnalysisResult,fakeAnalysisResult, ModuleEntity.AnalysisType.APPLICATION)

        assertEquals(presenter.result, fakeAnalysisResult)

        verify(view).setTitleApplications()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onCreateView_should_clear_when_analysis_type_is_application() {
        presenter.onCreateView(fakeAnalysisResult.apply {
            appsItems = mutableListOf(fakeApplication)
        }, fakeAnalysisResult, ModuleEntity.AnalysisType.APPLICATION)

        assertEquals(presenter.result, fakeAnalysisResult)
        assert(presenter.incidencesList.isEmpty())
        assert(presenter.attentionList.isEmpty())

        verify(view).hideOkScreen()
        verify(view).setTitleApplications()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onCreateView_should_show_system_settings_when_analysis_type_is_system() {
        presenter.onCreateView(fakeAnalysisResult, fakeAnalysisResult, ModuleEntity.AnalysisType.SYSTEM)

        assertEquals(presenter.result, fakeAnalysisResult)

        verify(view).setTitleSystem()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onCreateView_should_clear_when_analysis_type_is_system() {
        presenter.onCreateView(fakeAnalysisResult.apply {
            systemItems = mutableListOf(fakeModuleResult)
        }, fakeAnalysisResult, ModuleEntity.AnalysisType.SYSTEM)

        assertEquals(presenter.result, fakeAnalysisResult)
        assert(presenter.incidencesList.isEmpty())
        assert(presenter.attentionList.isEmpty())

        verify(view).hideOkScreen()
        verify(view).refillRecyclerView(presenter.incidencesList, presenter.attentionList)
        verify(view).setTitleSystem()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onModuleClicked_should_module_description() {
        presenter.onModuleClicked(fakeModuleResult)
        verify(view).showDescription(fakeModuleResult)
        verifyNoMoreInteractions(view)
    }


    private fun createPresenter(analysisController: AnalysisController = this.analysisController) =
        SettingsPresenter(analysisController).also { it.setViewContract(view) }
}

