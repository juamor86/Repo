package es.inteco.conanmobile.presentation.legal

import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import es.inteco.conanmobile.BaseTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LegalPresenterTest : BaseTest() {

    @Mock
    lateinit var view: LegalContract.View

    private lateinit var presenter: LegalPresenter

    @Before
    fun setup() {
        presenter = createPresenter()
    }

    @Test
    fun onCreateView_should_show_legal_text() {
        presenter.onCreateView(fakeConfiguration.message)
        verify(view).showLegalText(fakeConfiguration.message.formattedTermsAndConditions)
        verifyNoMoreInteractions(view)
    }

    private fun createPresenter() = LegalPresenter().also { it.setViewContract(view) }
}

