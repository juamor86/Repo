package es.juntadeandalucia.msspa.saludandalucia.presentation.preferences

import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import es.juntadeandalucia.msspa.saludandalucia.domain.base.CompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.base.SingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.base.utils.FakeErrorCompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.base.utils.FakeErrorSingleUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.base.utils.FakeSuccessCompletableUseCase
import es.juntadeandalucia.msspa.saludandalucia.domain.base.utils.FakeSuccessSingleUseCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PreferencesPresenterTest {

    @Mock
    lateinit var view: PreferencesContract.View

    @Mock
    lateinit var getNotificationsPhoneNumberUseCase: SingleUseCase<String>

    @Mock
    lateinit var clearNotificationsSubscriptionUseCase: CompletableUseCase

    private lateinit var presenter: PreferencesPresenter

    private val fakePhoneNumber = "123456789"
    private val emptyPhoneNumber = ""

    @Before
    fun setup() {
        presenter = createPresenter()
    }

    @Test
    fun onResume_should_enable_notifications_switch_and_show_phone_number_if_there_is_associated_phone_number() {
        presenter = createPresenter(phoneNumberUseCase = FakeSuccessSingleUseCase(fakePhoneNumber)).apply {
            onResume()
        }
        verify(view).enableNotificationsSwitch()
        verify(view).showNotificationsPhone(fakePhoneNumber)
        verify(view).setupSwitch()
        verify(view, never()).disableNotificationsSwitch()
    }

    @Test
    fun onResume_should_enable_notifications_switch_and_show_phone_number_if_there_is_not_associated_phone_number() {
        presenter = createPresenter(phoneNumberUseCase = FakeSuccessSingleUseCase(emptyPhoneNumber)).apply {
            onResume()
        }
        verify(view).disableNotificationsSwitch()
        verify(view).setupSwitch()
        verify(view, never()).showNotificationsPhone(fakePhoneNumber)
    }

    @Test
    fun onResume_should_setup_switch_if_error_getting_associated_phone_number() {
        presenter = createPresenter(phoneNumberUseCase = FakeErrorSingleUseCase()).apply {
            onResume()
        }
        verify(view).setupSwitch()
    }

    @Test
    fun onNotificationsEnabled_should_navigate_to_validate_phone_number() {
        presenter.onNotificationsEnabled()
        verify(view).navigateToVerification()
    }

    @Test
    fun onNotificationsDisabled_should_show_confirm_dialog() {
        presenter.onNotificationsDisabled()
        verify(view).showConfirmDialog()
    }

    @Test
    fun onDisableNotifications_should_hide_notifications_phone_number_when_it_disables_the_notifications() {
        presenter = createPresenter(clearUseCase = FakeSuccessCompletableUseCase()).apply {
            disableNotifications()
        }
        inOrder(view) {
            verify(view).showLoading()
            verify(view).hideLoading()
        }
        verify(view).hideNotificationsPhone()
        verify(view, never()).enableNotificationsSwitch()
    }

    @Test
    fun onDisableNotifications_should_enable_notifications_if_there_is_an_error_disabling_the_notifications() {
        presenter = createPresenter(clearUseCase = FakeErrorCompletableUseCase()).apply {
            disableNotifications()
        }
        inOrder(view) {
            verify(view).showLoading()
            verify(view).hideLoading()
        }
        verify(view).enableNotificationsSwitch()
        verify(view, never()).hideNotificationsPhone()
    }

    @Test
    fun onCancelDisableNotifications_should_enable_notifications_switch() {
        presenter.onCancelDisableNotifications()
        verify(view).enableNotificationsSwitch()
    }

    @Test
    fun unsubscribe_should_clear_all_use_cases() {
        presenter.unsubscribe()
        verify(getNotificationsPhoneNumberUseCase).clear()
        verify(clearNotificationsSubscriptionUseCase).clear()
    }

    private fun createPresenter(
        phoneNumberUseCase: SingleUseCase<String> = getNotificationsPhoneNumberUseCase,
        clearUseCase: CompletableUseCase = clearNotificationsSubscriptionUseCase
    ) = PreferencesPresenter(phoneNumberUseCase, clearUseCase).also { it.setViewContract(view) }
}
