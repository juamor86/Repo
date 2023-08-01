package es.inteco.conanmobile.presentation.analysis

import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.domain.base.BehaviorSubjectUseCase
import es.inteco.conanmobile.domain.base.CompletableUseCase
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.base.bus.LifecycleBus
import es.inteco.conanmobile.domain.base.utils.FakeSuccessCompletableUseCase
import es.inteco.conanmobile.domain.base.utils.FakeSuccessSingleUseCase
import es.inteco.conanmobile.domain.entities.*
import es.inteco.conanmobile.domain.usecases.GetConfigurationUseCase
import es.inteco.conanmobile.domain.usecases.SaveAnalysisUseCase
import es.inteco.conanmobile.domain.usecases.analisys.PostAnalysisResultUseCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AnalysisPresenterTest : BaseTest() {

    @Mock
    lateinit var view: AnalysisContract.View

    @Mock
    private lateinit var controller: AnalysisController

    @Mock
    private lateinit var getConfigurationUseCase: SingleUseCase<GetConfigurationUseCase.Params, ConfigurationEntity>

    @Mock
    private lateinit var getDefaultAnalysisUseCase: SingleUseCase<Void, AnalysisEntity>

    @Mock
    private lateinit var postAnalysisResultUseCase: SingleUseCase<PostAnalysisResultUseCase.Params, PostAnalysisResultEntity>

    @Mock
    private lateinit var saveAnalysisUseCase: CompletableUseCase<SaveAnalysisUseCase.Params>

    @Mock
    private lateinit var existLastAnalysisUseCase: SingleUseCase<Void, Boolean>

    @Mock
    private lateinit var getLastAnalysisUseCase: SingleUseCase<Void, AnalysisResultEntity>

    @Mock
    private lateinit var lifecycleBus: BehaviorSubjectUseCase<LifecycleBus.LifecycleState>

    private val fakeAnalysisEntity =
        AnalysisEntity("").apply { names = listOf(AdministrationEntity("", "")) }
    private val fakeAnalysisResultEntity = AnalysisResultEntity(fakeAnalysisEntity)
    private val fakePostAnalysis =
        PostAnalysisResultEntity("", 0, "", PostAnalysisResultMessageEntity(""), "")

    private lateinit var presenter: AnalysisPresenter

    @Before
    fun setup() {
        presenter = createPresenter()
    }

    @Test
    fun onViewCreated_should_check_bluetooth_permission() {
        presenter = createPresenter(
            getDefaultAnalysisUseCase = FakeSuccessSingleUseCase(fakeAnalysisEntity),
            existLastAnalysisUseCase = FakeSuccessSingleUseCase(mock = true),
            getLastAnalysisUseCase = FakeSuccessSingleUseCase(fakeAnalysisResultEntity)
        )
        presenter.onViewCreated()
        verify(view).checkBluetoothPermissions()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onPermissionGranted_should_start_analysis() {
        presenter = createPresenter(
            getDefaultAnalysisUseCase = FakeSuccessSingleUseCase(fakeAnalysisEntity),
            existLastAnalysisUseCase = FakeSuccessSingleUseCase(mock = true),
            getLastAnalysisUseCase = FakeSuccessSingleUseCase(fakeAnalysisResultEntity)
        )
        presenter.onPermissionsGranted()
        verify(controller).start(view, fakeAnalysisEntity, presenter::onFinishAnalysis)
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onPermissionNotGranted_should_request_permission_again() {
        presenter = createPresenter(
            getDefaultAnalysisUseCase = FakeSuccessSingleUseCase(fakeAnalysisEntity),
            existLastAnalysisUseCase = FakeSuccessSingleUseCase(mock = true),
            getLastAnalysisUseCase = FakeSuccessSingleUseCase(fakeAnalysisResultEntity)
        )
        presenter.onPermissionsNotGranted()
        verify(view).checkBluetoothPermissions()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onPermissionNotGranted_should_navigate_back_after_3_times() {
        presenter = createPresenter(
            getDefaultAnalysisUseCase = FakeSuccessSingleUseCase(fakeAnalysisEntity),
            existLastAnalysisUseCase = FakeSuccessSingleUseCase(mock = true),
            getLastAnalysisUseCase = FakeSuccessSingleUseCase(fakeAnalysisResultEntity)
        ).apply {
            bluetoothRequestTimes = 2
        }
        presenter.onPermissionsNotGranted()
        verify(view).navigateBack()
        verifyNoMoreInteractions(view)
    }


    @Test
    fun onFinishAnalysis_should_navigate_to_results() {
        presenter = createPresenter(
            existLastAnalysisUseCase = FakeSuccessSingleUseCase(mock = true),
            getLastAnalysisUseCase = FakeSuccessSingleUseCase(fakeAnalysisResultEntity),
            saveAnalysisUseCase = FakeSuccessCompletableUseCase(),
            getConfigurationUseCase = FakeSuccessSingleUseCase(fakeConfiguration),
            getDefaultAnalysisUseCase = FakeSuccessSingleUseCase(fakeAnalysisEntity),
            postAnalysisResultUseCase = FakeSuccessSingleUseCase(fakePostAnalysis)
        ).apply {
            onViewCreated()
        }
        presenter.onFinishAnalysis(fakeAnalysisResultEntity)

        verify(view).navigateToResults(fakeAnalysisResultEntity, fakeAnalysisResultEntity)
        verify(view, never()).sendNotification()
    }

    @Test
    fun onFinishAnalysis_should_navigate_to_results_and_send_notification_if_app_is_in_background() {
        presenter = createPresenter(
            existLastAnalysisUseCase = FakeSuccessSingleUseCase(mock = true),
            getLastAnalysisUseCase = FakeSuccessSingleUseCase(fakeAnalysisResultEntity),
            saveAnalysisUseCase = FakeSuccessCompletableUseCase()
        ).apply {
            onViewCreated()
            isBackground = true
        }

        presenter.onFinishAnalysis(fakeAnalysisResultEntity)

        verify(view).navigateToResults(fakeAnalysisResultEntity, fakeAnalysisResultEntity)
        verify(view).sendNotification()
    }

    @Test
    fun onDestroy_should_stop_analysis() {
        presenter.onDestroy()
        verify(controller).stopAnalysis()
    }

    private fun createPresenter(
        controller: AnalysisController = this.controller,
        getConfigurationUseCase: SingleUseCase<GetConfigurationUseCase.Params, ConfigurationEntity> = this.getConfigurationUseCase,
        getDefaultAnalysisUseCase: SingleUseCase<Void, AnalysisEntity> = this.getDefaultAnalysisUseCase,
        postAnalysisResultUseCase: SingleUseCase<PostAnalysisResultUseCase.Params, PostAnalysisResultEntity> = this.postAnalysisResultUseCase,
        saveAnalysisUseCase: CompletableUseCase<SaveAnalysisUseCase.Params> = this.saveAnalysisUseCase,
        existLastAnalysisUseCase: SingleUseCase<Void, Boolean> = this.existLastAnalysisUseCase,
        getLastAnalysisUseCase: SingleUseCase<Void, AnalysisResultEntity> = this.getLastAnalysisUseCase,
        lifecycleBus: BehaviorSubjectUseCase<LifecycleBus.LifecycleState> = this.lifecycleBus
    ) = AnalysisPresenter(
        controller = controller,
        getConfigurationUseCase = getConfigurationUseCase,
        getDefaultAnalysisUseCase = getDefaultAnalysisUseCase,
        postAnalysisResultUseCase = postAnalysisResultUseCase,
        saveAnalysisUseCase = saveAnalysisUseCase,
        existLastAnalysisUseCase = existLastAnalysisUseCase,
        getLastAnalysisUseCase = getLastAnalysisUseCase,
        lifecycleBus = lifecycleBus
    ).also { it.setViewContract(view) }

}
