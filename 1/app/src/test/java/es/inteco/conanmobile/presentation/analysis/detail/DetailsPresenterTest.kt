package es.inteco.conanmobile.presentation.analysis.detail

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import es.inteco.conanmobile.BaseTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailsPresenterTest : BaseTest() {

    @Mock
    lateinit var view: DetailContract.View

    private val fakeTitle = "Details"

    private lateinit var presenter: DetailPresenter

    @Before
    fun setup() {
        presenter = createPresenter()
    }

    @Test
    fun onCreateView_should_init_screen() {
        presenter.onCreateView(fakeTitle, mutableListOf())

        verify(view).fillToolbarTittle(fakeTitle)
        verify(view).fillSubTitle(fakeTitle)
        verify(view).initButtons()
        verify(view).refillReciclerView(mutableListOf())
        verifyNoMoreInteractions(view)
    }

    @Test
    fun goToDeviceConfiguration_should_navigate_to_settings() {
        presenter.goToDeviceConfiguration()

        verify(view).navitateToWifiSettings()
        verifyNoMoreInteractions(view)
    }

    @Test
    fun onNavigateToOSITipsClicked_should_navigate_to_OSI_tips() {
        presenter.onNavigateToOSITipsClicked()

        verify(view).navigateToOSITips()
        verifyNoMoreInteractions(view)
    }



    private fun createPresenter() = DetailPresenter().also { it.setViewContract(view) }
}

