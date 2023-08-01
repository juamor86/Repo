package es.inteco.conanmobile.presentation.base

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import es.inteco.conanmobile.presentation.dialog.CustomDialog
import java.util.*

/**
 * Base activity
 *
 * @constructor Create empty Base activity
 */
abstract class BaseActivity : AppCompatActivity(), BaseContract.View {

    private val presenter by lazy { bindPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindLayout())
        injectComponent()
        presenter.setViewContract(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    /**
     * Hide keyboard
     *
     * @param view
     */
    fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Bind layout
     *
     * @return
     */
    @LayoutRes
    abstract fun bindLayout(): Int

    /**
     * Bind presenter
     *
     * @return
     */
    abstract fun bindPresenter(): BaseContract.Presenter

    /**
     * Inject component
     *
     */
    abstract fun injectComponent()

    override fun showErrorDialog() {
        // Empty default implementation
    }

    override fun showErrorDialog(@StringRes title: Int) {
        // Empty default implementation
    }

    override fun showConfirmDialog(
        title: Int?,
        message: Int,
        positiveText: Int?,
        cancelText: Int?,
        onAccept: (() -> Unit)?,
        onCancel: (() -> Unit)?
    ) {
        this.let {
            val errorDialog = CustomDialog(
                context = it,
                title = title,
                message = message,
                positiveText = positiveText,
                onAccept = onAccept,
                negativeText = cancelText,
                onCancel = onCancel,
            )
            errorDialog.show()
        }
    }

    /**
     * Show info dialog
     *
     * @param message
     */
    protected fun showInfoDialog(@StringRes message: Int) {
        val errorDialog = CustomDialog(this, message = message)
        errorDialog.show()
    }

    /**
     * Show info dialog
     *
     * @param message
     */
    protected fun showInfoDialog(message: String) {
        val infoDialog = CustomDialog(this, messageText = message)
        infoDialog.show()
    }

    /**
     * Show warning dialog
     *
     * @param title
     * @param message
     * @param positiveText
     * @param messageText
     * @param onAccept
     */
    protected fun showWarningDialog(
        @StringRes title: Int? = null,
        @StringRes message: Int? = null,
        @StringRes positiveText: Int? = null,
        messageText: String? = null,
        onAccept: (() -> Unit)? = {}
    ) {
        val warningDialog = CustomDialog(
            context = this,
            title = title,
            message = message,
            positiveText = positiveText,
            onAccept = onAccept,
            messageText = messageText
        )
        warningDialog.show()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(updateBaseContextLocale(base))
    }

    private fun updateBaseContextLocale(context: Context): Context? {
        val language = when (Locale.getDefault().language) {
            "eu",
            "gl",
            "ca" -> "es"
            else -> Locale.getDefault().language
        }

        val locale = Locale(language, language)
        Locale.setDefault(locale)
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            updateResourcesLocale(context, locale)
        } else updateResourcesLocaleLegacy(context, locale)
    }

    @TargetApi(Build.VERSION_CODES.N_MR1)
    private fun updateResourcesLocale(context: Context, locale: Locale): Context? {
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context? {
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}