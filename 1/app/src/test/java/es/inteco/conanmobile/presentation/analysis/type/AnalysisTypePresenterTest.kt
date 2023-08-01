package es.inteco.conanmobile.presentation.analysis.type

import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.domain.base.CompletableUseCase
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.base.utils.FakeErrorCompletableUseCase
import es.inteco.conanmobile.domain.base.utils.FakeErrorSingleUseCase
import es.inteco.conanmobile.domain.base.utils.FakeSuccessCompletableUseCase
import es.inteco.conanmobile.domain.base.utils.FakeSuccessSingleUseCase
import es.inteco.conanmobile.domain.entities.AnalysisEntity
import es.inteco.conanmobile.domain.entities.ConfigurationEntity
import es.inteco.conanmobile.domain.usecases.GetConfigurationUseCase
import es.inteco.conanmobile.domain.usecases.SaveDefaultAnalysisUseCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AnalysisTypePresenterTest : BaseTest() {

    @Mock
    private lateinit var getConfigurationUseCase: SingleUseCase<GetConfigurationUseCase.Params, ConfigurationEntity>

    @Mock
    private lateinit var saveDefaultAnalysisUseCase: CompletableUseCase<SaveDefaultAnalysisUseCase.Params>

    @Mock
    private lateinit var getDefaultAnalysisUseCase: SingleUseCase<Void, AnalysisEntity>

    @Mock
    lateinit var view: AnalysisTypeContract.View

    private lateinit var presenter: AnalysisTypePresenter

    @Before
    fun setup() {
        presenter = createPresenter()
    }

    @Test
    fun onCreateView_should_refill_recycler_view_with_default_analysis() {
        presenter =
            createPresenter(
                getDefaultAnalysisUseCase = FakeSuccessSingleUseCase(mock = fakeDefaultAnalysis),
                getConfigurationUseCase = FakeSuccessSingleUseCase(mock = fakeConfiguration)
            ).apply {
                onCreateView()
            }

        verify(view).refillRecyclerView(fakeConfiguration.message.analysis, fakeDefaultAnalysis)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onCreateView_does_nothing_when_default_analysis_fails() {
        presenter =
            createPresenter(
                getDefaultAnalysisUseCase = FakeErrorSingleUseCase()
            ).apply {
                onCreateView()
            }

        verify(view, never()).refillRecyclerView(
            fakeConfiguration.message.analysis,
            fakeDefaultAnalysis
        )
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onCreateView_does_nothing_when_get_configuration_fails() {
        presenter =
            createPresenter(
                getDefaultAnalysisUseCase = FakeSuccessSingleUseCase(mock = fakeDefaultAnalysis),
                getConfigurationUseCase = FakeErrorSingleUseCase()
            ).apply {
                onCreateView()
            }

        verify(view, never()).refillRecyclerView(
            fakeConfiguration.message.analysis,
            fakeDefaultAnalysis
        )
        verifyNoMoreInteractions(view)
    }

    @Test
    fun saveDefaultAnalysis_should_save_selected_analysis() {
        presenter =
            createPresenter(
                saveDefaultAnalysisUseCase = FakeSuccessCompletableUseCase()
            ).apply {
                saveDefaultAnalysis(fakeAnalysis)
            }

        verifyNoMoreInteractions(view)
    }

    @Test
    fun saveDefaultAnalysis_does_nothing_when_save_analysis_fails() {
        presenter =
            createPresenter(
                saveDefaultAnalysisUseCase = FakeErrorCompletableUseCase()
            ).apply {
                saveDefaultAnalysis(fakeAnalysis)
            }

        verifyNoMoreInteractions(view)
    }

    @Test
    fun unsubscribe_should_clear_all_use_cases() {
        presenter.unsubscribe()

        verify(getConfigurationUseCase).clear()
        verify(saveDefaultAnalysisUseCase).clear()
        verify(getDefaultAnalysisUseCase).clear()
    }

    private fun createPresenter(
        getConfigurationUseCase: SingleUseCase<GetConfigurationUseCase.Params, ConfigurationEntity> = this.getConfigurationUseCase,
        saveDefaultAnalysisUseCase: CompletableUseCase<SaveDefaultAnalysisUseCase.Params> = this.saveDefaultAnalysisUseCase,
        getDefaultAnalysisUseCase: SingleUseCase<Void, AnalysisEntity> = this.getDefaultAnalysisUseCase
    ) = AnalysisTypePresenter(
        getConfigurationUseCase = getConfigurationUseCase,
        saveDefaultAnalysisUseCase = saveDefaultAnalysisUseCase,
        getDefaultAnalysisUseCase = getDefaultAnalysisUseCase
    ).also {
        it.setViewContract(view)
    }
}

