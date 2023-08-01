package es.inteco.conanmobile.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import es.inteco.conanmobile.R
import es.inteco.conanmobile.presentation.dialog.CustomDialog
import es.inteco.conanmobile.presentation.main.MainActivity

/**
 * Base fragment
 *
 * @constructor Create empty Base fragment
 */
abstract class BaseFragment : Fragment(), BaseContract.View {

    private val presenter by lazy { bindPresenter() }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        presenter.setViewContract(this)
        presenter.onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(bindLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated()
    }

    override fun onStop() {
        presenter.unsubscribe()
        super.onStop()
    }

    /**
     * Bind layout
     *
     * @return
     */
    @LayoutRes
    abstract fun bindLayout(): Int

    override fun showErrorDialog() {
        // Empty default implementation
    }

    override fun showErrorDialog(@StringRes title: Int) {
        // Empty default implementation
    }

    override fun showErrorDialog(@StringRes title: Int, @StringRes message: Int) {
        showDialog(
            icon = R.drawable.ic_error,
            title = title,
            message = message
        )
    }

    override fun showErrorDialog(@StringRes message: Int, onAccept: () -> Unit) {
        // Empty default implementation
    }

    /**
     * Show error dialog
     *
     * @param title
     * @param positiveText
     * @param onAccept
     */
    protected fun showErrorDialog(
        @StringRes title: Int? = null,
        @StringRes positiveText: Int? = null,
        onAccept: (() -> Unit)? = {}
    ) {
        context?.let {
            val errorDialog = CustomDialog(
                context = it,
                title = title,
                positiveText = positiveText,
                onAccept = onAccept
            )
            errorDialog.show()
        }
    }

    override fun showConfirmDialog(
        title: Int?,
        message: Int,
        positiveText: Int?,
        cancelText: Int?,
        onAccept: (() -> Unit)?,
        onCancel: (() -> Unit)?
    ) {
        context?.let {
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

    private fun showDialog(
        @DrawableRes icon: Int? = null,
        @StringRes title: Int? = null,
        @StringRes message: Int? = null,
        @StringRes positiveText: Int? = null,
        onAccept: (() -> Unit)? = null,
        @StringRes cancelText: Int? = null,
        onCancel: (() -> Unit)? = null
    ) {
        if (isAdded) {
            context?.let {
                val errorDialog = CustomDialog(
                    context = it,
                    icon = icon,
                    title = title,
                    message = message,
                    positiveText = positiveText,
                    onAccept = onAccept,
                    negativeText = cancelText,
                    onCancel = onCancel
                )
                errorDialog.show()
            }
        }
    }

    /**
     * Show info dialog
     *
     * @param message
     */
    fun showInfoDialog(message: String) {
        val infoDialog = CustomDialog(
            requireActivity(),
            messageText = message,
        )
        infoDialog.show()
    }

    /**
     * Show warning dialog
     *
     * @param title
     * @param message
     * @param messageText
     * @param positiveText
     * @param cancelText
     * @param onCancel
     * @param onAccept
     */
    protected fun showWarningDialog(
        @StringRes title: Int? = null,
        @StringRes message: Int? = null,
        messageText: String? = null,
        @StringRes positiveText: Int? = null,
        @StringRes cancelText: Int? = null,
        onCancel: (() -> Unit)? = null,
        onAccept: (() -> Unit)? = {}
    ) {
        context?.let {
            val warningDialog = CustomDialog(
                context = it,
                title = title,
                message = message,
                messageText = messageText,
                positiveText = positiveText,
                onAccept = onAccept,
                negativeText = cancelText,
                onCancel = onCancel
            )
            warningDialog.show()
        }
    }

    /**
     * Show success dialog
     *
     * @param title
     * @param positiveText
     * @param onAccept
     */
    protected fun showSuccessDialog(
        @StringRes title: Int? = null,
        @StringRes positiveText: Int? = null,
        onAccept: (() -> Unit)? = {}
    ) {
        context?.let {
            val successDialog = CustomDialog(
                context = it,
                title = title,
                positiveText = positiveText,
                onAccept = onAccept
            )
            successDialog.show()
        }
    }

    override fun showLoading() {
        if (isAdded) {
            (activity as MainActivity).apply {
                showLoading()
            }
        }
    }

    override fun showLoadingBlocking() {
        if (isAdded) {
            (activity as MainActivity).apply {
                showLoadingBlocking()
            }
        }
    }

    override fun hideLoading() {
        if (activity != null && activity is MainActivity) {
            (activity as MainActivity).hideLoading()
        }
    }

    override fun onDetach() {
        super.onDetach()
        hideLoading()
        presenter.unsubscribe()
    }

    override fun navigateUp() {
        if (requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).navigateUp()
        }
    }

}