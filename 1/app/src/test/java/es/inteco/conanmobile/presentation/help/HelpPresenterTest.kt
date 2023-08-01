package es.inteco.conanmobile.presentation.help

import com.nhaarman.mockitokotlin2.verify
import es.inteco.conanmobile.BaseTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HelpPresenterTest : BaseTest() {

    @Mock
    lateinit var view: HelpContract.View

    private lateinit var presenter: HelpPresenter

    private var fakePage = 0

    @Before
    fun setup() {
        presenter = createPresenter().apply { currentPage = fakePage }
    }

    @Test
    fun onCreate_should_init_view() {
        presenter.onCreate()

        verify(view).initView()
    }

    @Test
    fun onViewCreated_should_init_view() {
        presenter.onViewCreated()

        verify(view).initButtons()
    }

    @Test
    fun onStart_should_open_pdf() {
        presenter.onStart()

        verify(view).openPdfFromRaw(fakePage)
    }

    @Test
    fun onStop_should_close_render() {
        presenter.onStop()

        verify(view).closeRenderer()
    }

    @Test
    fun onPreviousPageClicked_should_open_previous_page() {
        presenter.onPreviousPageClicked()

        verify(view).openPdfFromRaw(--fakePage)


    }

    @Test
    fun onNextPageClicked_should_open_next_page() {
        presenter.onNextPageClicked()

        verify(view).openPdfFromRaw(++fakePage)
    }

    @Test
    fun onFileError_should_show_no_pdf_dialog() {
        presenter.onFileError()

        verify(view).showNoPDFDialog()
    }

    private fun createPresenter() = HelpPresenter().also { it.setViewContract(view) }
}

