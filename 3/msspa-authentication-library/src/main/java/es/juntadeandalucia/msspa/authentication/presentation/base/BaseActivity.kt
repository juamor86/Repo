package es.juntadeandalucia.msspa.authentication.presentation.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import es.juntadeandalucia.msspa.authentication.*
import es.juntadeandalucia.msspa.authentication.di.component.AuthLibraryComponent
import es.juntadeandalucia.msspa.authentication.di.component.DaggerAuthLibraryComponent
import es.juntadeandalucia.msspa.authentication.di.module.AuthLibraryModule
import es.juntadeandalucia.msspa.authentication.domain.entities.NavBackPressed
import es.juntadeandalucia.msspa.authentication.presentation.dialog.MsspaAuthCustomDialog
import es.juntadeandalucia.msspa.authentication.security.CrytographyManager
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import es.juntadeandalucia.msspa.authentication.utils.BiometricUtils
import es.juntadeandalucia.msspa.authentication.utils.Utils
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), BaseContract.View {

    internal val authLibraryComponent: AuthLibraryComponent by lazy {
        DaggerAuthLibraryComponent.builder().authLibraryModule(AuthLibraryModule(this)).build()
    }
    var navBackPressed: NavBackPressed = NavBackPressed()
    open val authConfig by lazy { bindConfig() }
    open val authEntity by lazy { bindAuthEntity() }
    open val scope by lazy { bindScope() }
    private val presenter by lazy { bindPresenter() }

    @set:Inject
    var cryptoManager: CrytographyManager? = null

    abstract fun bindConfig(): MsspaAuthenticationConfig

    abstract fun bindAuthEntity(): MsspaAuthenticationEntity?

    abstract fun bindScope(): MsspaAuthenticationManager.Scope

    abstract fun bindPresenter(): BaseContract.Presenter

    abstract fun injectComponent()

    @LayoutRes
    abstract fun bindLayout(): Int

    override fun sendEvent(event: String) {
        val eventDeepLink = authConfig.appKey + ApiConstants.General.EVENT_DEEPLINK + event
        val intent = Intent().apply {
            action = MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_BROADCAST_ACTION
            putExtra(
                MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_BROADCAST_RESULT,
                MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_BROADCAST_RESULT_EVENT
            )
            putExtra(MsspaAuthenticationBroadcastReceiver.MSSPA_AUTHENTICATION_EVENT_ARG, eventDeepLink)
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.secureAgainstScreenshots(window, authConfig.environment)
        setContentView(bindLayout())
        injectComponent()
        presenter.setViewContract(this)
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected fun showErrorDialog(
            @LayoutRes layout: Int? = null,
            @StringRes title: Int? = null,
            @StringRes message: Int? = null
    ) {
        val errorDialog = MsspaAuthCustomDialog(
                context = this,
                layout = layout ?: R.layout.msspa_auth_view_custom_dialog,
                title = title,
                message = message,
                environment = authConfig.environment
        )
        errorDialog.show()
    }

    protected fun showInfoDialog(
            @LayoutRes layout: Int? = null,
            @StringRes title: Int? = null,
            @StringRes message: Int? = null,
            onAccept: (() -> Unit)? = null,
            onCancel: (() -> Unit)? = null
    ) {
        val infoDialog = MsspaAuthCustomDialog(
                context = this,
                layout = layout ?: R.layout.msspa_auth_view_custom_dialog,
                title = title ?: R.string.msspa_auth_dialog_info_title,
                message = message ?: R.string.msspa_auth_dialog_info_message,
                onAccept = onAccept, onCancel = onCancel,
                environment = authConfig.environment
        )
        infoDialog.show()
    }

    protected fun showConfirmDialog(
        @LayoutRes layout: Int? = null,
        @StringRes title: Int? = null,
        @StringRes message: Int? = null,
        onAccept: (() -> Unit)? = null,
        onCancel: (() -> Unit)? = null
    ) {
        val infoDialog = MsspaAuthCustomDialog(
            context = this,
            layout = layout ?: R.layout.msspa_auth_view_custom_dialog_confirm,
            title = title ?: R.string.msspa_auth_dialog_info_title,
            message = message ?: R.string.msspa_auth_dialog_info_message,
            onAccept = onAccept, onCancel = onCancel,
            environment = authConfig.environment
        )
        infoDialog.show()
    }

    override fun showWarning(message:Int, onAccept:(() -> Unit)?){
        showInfoDialog(
            layout = authConfig.warningLayout,
            title = message,
            onAccept = onAccept
        )
    }

    override fun showWarning(title:Int, message:Int, onAccept:(() -> Unit)?, onCancel: (() -> Unit)?){
        showInfoDialog(
            layout = authConfig.warningLayout,
            title = title,
            message = message,
            onAccept = onAccept,
            onCancel = onCancel
        )
    }

    override fun isPhoneSecured(): Boolean = BiometricUtils.isBiometricOrSecured(applicationContext)
}
