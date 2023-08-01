package es.inteco.conanmobile.presentation.warnings

import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.base.utils.FakeErrorSingleUseCase
import es.inteco.conanmobile.domain.base.utils.FakeSuccessSingleUseCase
import es.inteco.conanmobile.domain.entities.WarningEntity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WarningsPresenterTest : BaseTest() {

    @Mock
    lateinit var view: WarningsContract.View

    @Mock
    lateinit var getWarningsUseCase: SingleUseCase<Void, List<WarningEntity>>

    private lateinit var presenter: WarningsPresenter

    private val fakeWarning = WarningEntity(id = "", title = "", description = "", date = 0L, importance = WarningEntity.Importance.LOW)

    @Before
    fun setup() {
        presenter = createPresenter()
    }

    private fun createPresenter(
        getWarningsUseCase: SingleUseCase<Void, List<WarningEntity>> = this.getWarningsUseCase,
    ) = WarningsPresenter(
        getWarningsUseCase = getWarningsUseCase
    ).also { it.setViewContract(view) }


    @Test
    fun onCreate_should_init_screen() {
        presenter.onCreate()
        verify(view).initScreen()
    }

    @Test
    fun onViewCreate_should_show_empty_warnings_if_there_are_no_warnings() {
        presenter =
            createPresenter(getWarningsUseCase = FakeSuccessSingleUseCase(emptyList())).apply { onViewCreated() }
        verify(view).showLoading()
        verify(view).showEmptyView()
        verify(view).hideLoading()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onViewCreate_should_show_warnings() {
        presenter =
            createPresenter(getWarningsUseCase = FakeSuccessSingleUseCase(listOf(fakeWarning))).apply { onViewCreated() }
        verify(view).showLoading()
        verify(view).showWarnings(listOf(fakeWarning))
        verify(view).hideLoading()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onViewCreated_should_show_error_view_if_get_warnings_fails() {
        presenter =
            createPresenter(getWarningsUseCase = FakeErrorSingleUseCase()).apply { onViewCreated() }

        verify(view).showLoading()
        verify(view).hideLoading()
        verify(view).showWarningsError()
        verify(view).navigateUp()
        verifyNoMoreInteractions(view)
    }

}
