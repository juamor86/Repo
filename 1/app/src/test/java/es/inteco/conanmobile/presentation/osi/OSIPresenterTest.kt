package es.inteco.conanmobile.presentation.osi

import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import es.inteco.conanmobile.BaseTest
import es.inteco.conanmobile.domain.base.SingleUseCase
import es.inteco.conanmobile.domain.base.utils.FakeErrorSingleUseCase
import es.inteco.conanmobile.domain.base.utils.FakeSuccessSingleUseCase
import es.inteco.conanmobile.domain.entities.OSIEntity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OSIPresenterTest : BaseTest() {

    @Mock
    lateinit var view: OSIContract.View

    @Mock
    lateinit var getOSITipsUseCase: SingleUseCase<Void, List<OSIEntity>>

    private lateinit var presenter: OSIPresenter

    private val fakeOSITips =
        listOf(OSIEntity(id = "", title = emptyMap(), description = emptyMap()))

    @Before
    fun setup() {
        presenter = createPresenter()
    }

    private fun createPresenter(
        getOSITipsUseCase: SingleUseCase<Void, List<OSIEntity>> = this.getOSITipsUseCase,
    ) = OSIPresenter(
        getOSITipsUseCase = getOSITipsUseCase
    ).also { it.setViewContract(view) }


    @Test
    fun onCreate_should_init_screen() {
        presenter.onCreate()
        verify(view).initScreen()
    }

    @Test
    fun onViewCreate_should_show_osi_tips() {
        presenter =
            createPresenter(getOSITipsUseCase = FakeSuccessSingleUseCase(mock = fakeOSITips)).apply {
                onViewCreated()
            }
        verify(view).showLoading()
        verify(view).showOSITips(fakeOSITips)
        verify(view).hideLoading()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onViewCreate_should_show_error_when_osi_tips_fails() {
        presenter =
            createPresenter(getOSITipsUseCase = FakeErrorSingleUseCase()).apply { onViewCreated() }
        verify(view).showLoading()
        verify(view).showWarningsError()
        verify(view).hideLoading()
        verify(view).navigateUp()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun unsubscribe_should_clear_all_use_cases() {
        presenter.unsubscribe()

        verify(getOSITipsUseCase).clear()
    }

}
