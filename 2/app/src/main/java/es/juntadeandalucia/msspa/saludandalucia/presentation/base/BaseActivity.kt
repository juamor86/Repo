package es.juntadeandalucia.msspa.saludandalucia.presentation.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomDialog
import es.juntadeandalucia.msspa.saludandalucia.utils.Tracker
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils

abstract class BaseActivity : AppCompatActivity(), BaseContract.View {

    private val presenter by lazy { bindPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utils.secureAgainstScreenshots(window)
        setContentView(bindLayout())
        injectComponent()
        presenter.setViewContract(this)
    }

    override fun onDestroy() {
        presenter.unsubscribe()
        hideLoading()
        super.onDestroy()
    }

    override fun sendEvent(event: String, onComplete: () -> Unit) {
        Tracker.track(eventName = event, onComplete = onComplete)
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyBoard() {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    @LayoutRes
    abstract fun bindLayout(): Int

    abstract fun bindPresenter(): BaseContract.Presenter

    abstract fun injectComponent()

    override fun showErrorDialog() {
        showDialog(
            icon = R.drawable.ic_alert_info,
            title = R.string.dialog_error_text,
            type = CustomDialog.TypeDialog.INFO
        )
    }

    override fun showErrorDialogAndBack() {
        showDialog(
            icon = R.drawable.ic_alert_info,
            title = R.string.dialog_error_text,
            onAccept = { findNavController(R.id.nav_host_fragment).navigateUp() },
            type = CustomDialog.TypeDialog.INFO
        )
    }

    override fun showErrorDialog(@StringRes title: Int) {
        showDialog(
            icon = R.drawable.ic_alert_info,
            title = title,
            type = CustomDialog.TypeDialog.INFO
        )
    }

    override fun showConfirmDialog(
        title: Int?,
        message: Int?,
        onAccept: (() -> Unit)?,
        onCancel: (() -> Unit)?
    ) {
        showDialog(
            icon = R.drawable.ic_alert_info,
            title = title,
            message = message,
            onAccept = onAccept,
            onCancel = onCancel,
            type = CustomDialog.TypeDialog.INFO
        )
    }

    protected fun showErrorDialog(onAccept: () -> Unit = {}) {
        showDialog(
            icon = R.drawable.ic_alert_info,
            title = R.string.dialog_error_text,
            onAccept = onAccept,
            type = CustomDialog.TypeDialog.INFO
        )
    }

    protected fun showDialog(
        @DrawableRes icon: Int? = null,
        @StringRes title: Int? = null,
        @StringRes message: Int? = null,
        @StringRes positiveText: Int? = null,
        onAccept: (() -> Unit)? = null,
        @StringRes cancelText: Int? = null,
        onCancel: (() -> Unit)? = null,
        type: CustomDialog.TypeDialog
    ) {
        this.let {
            val errorDialog = CustomDialog(
                context = it,
                icon = icon,
                title = title,
                message = message,
                positiveText = positiveText,
                onAccept = onAccept,
                negativeText = cancelText,
                onCancel = onCancel,
                typeDialog = type
            )
            if (!isFinishing) {
                errorDialog.show()
            }
        }
    }

    override fun showInfoDialog(@StringRes message: Int, onAccept: () -> Unit) {
        showDialog(
            icon = R.drawable.ic_info,
            message = message,
            onAccept = onAccept,
            type = CustomDialog.TypeDialog.INFO
        )
    }

    protected fun showInfoDialog(
        @StringRes message: Int
    ) {
        val errorDialog = CustomDialog(
            this,
            message = message
        )
        if (!isFinishing) {
            errorDialog.show()
        }
    }

    protected fun showInfoDialog(
        @StringRes title: Int,
        @StringRes message: Int,
        onAccept: (() -> Unit)? = {}
    ) {
        val errorDialog = CustomDialog(
            this,
            title = title,
            message = message,
            onAccept = onAccept
        )
        if (!isFinishing) {
            errorDialog.show()
        }
    }

    protected fun showInfoDialog(
        @StringRes message: Int,
        args: Array<String>
    ) {
        val infoDialog = CustomDialog(
            this,
            message = message,
            args = args
        )
        if (!isFinishing) {
            infoDialog.show()
        }
    }

    protected fun showInfoDialog(
        @StringRes message: Int,
        args: Array<String>,
        onAccept: (() -> Unit)? = {}
    ) {
        val infoDialog = CustomDialog(
            this,
            message = message,
            args = args,
            onAccept = onAccept
        )
        infoDialog.show()
    }

    protected fun showErrorDialog(
        @StringRes title: Int? = null,
        @StringRes message: Int = R.string.dialog_error_text,
        @StringRes positiveText: Int? = null,
        onAccept: (() -> Unit)? = {}
    ) {
        val errorDialog = CustomDialog(
            icon = R.drawable.ic_alert_info,
            context = this,
            title = title,
            message = message,
            positiveText = positiveText,
            onAccept = onAccept,
            typeDialog = CustomDialog.TypeDialog.INFO
        )
        if (!isFinishing) {
            errorDialog.show()
        }
    }

    protected fun showWarningDialog(
        @StringRes title: Int? = null,
        @StringRes message: Int? = null,
        @StringRes positiveText: Int? = null,
        onAccept: (() -> Unit)? = {}
    ) {
        val warningDialog = CustomDialog(
            icon = R.drawable.ic_warning,
            context = this,
            title = title,
            message = message,
            positiveText = positiveText,
            onAccept = onAccept,
            typeDialog = CustomDialog.TypeDialog.WARNING
        )
        if (!isFinishing) {
            warningDialog.show()
        }
    }

    protected fun showSuccessDialog(
        @StringRes title: Int? = null,
        @StringRes message: Int? = null,
        @StringRes positiveText: Int? = null,
        args: Array<String> = emptyArray(),
        onAccept: (() -> Unit)? = {}
    ) {
        val successDialog = CustomDialog(
            icon = R.drawable.ic_success,
            context = this,
            title = title,
            message = message,
            args = args,
            positiveText = positiveText,
            onAccept = onAccept,
            typeDialog = CustomDialog.TypeDialog.SUCCESS
        )
        if (!isFinishing) {
            successDialog.show()
        }
    }
}
