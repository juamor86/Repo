package es.juntadeandalucia.msspa.saludandalucia.presentation.base

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomDialog
import es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic.DynamicNavigator
import es.juntadeandalucia.msspa.saludandalucia.presentation.main.MainActivity

abstract class BaseFragment : Fragment(), BaseContract.View {

    private val presenter by lazy { bindPresenter() }

    abstract fun bindPresenter(): BaseContract.Presenter

    abstract fun injectComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        presenter.setViewContract(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(bindLayout(), container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideLoading()
        presenter.onViewCreated()
    }

    override fun sendEvent(event: String, onComplete: () -> Unit) {
        (activity as MainActivity).sendEvent(event, onComplete)
    }

    protected fun getNavigator(): DynamicNavigator {
        return activity as DynamicNavigator
    }

    fun hideKeyboard() {
        view?.let { (activity as BaseActivity).hideKeyboard(it) }
    }

    @LayoutRes
    abstract fun bindLayout(): Int

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
            onAccept = { findNavController().navigateUp() },
            type = CustomDialog.TypeDialog.INFO
        )
    }

    override fun showErrorDialog(@StringRes title: Int) {
        showDialog(icon = R.drawable.ic_alert_info, title = title, type = CustomDialog.TypeDialog.INFO)
    }

    override fun showErrorDialog(@StringRes title: Int, @StringRes message: Int) {
        showDialog(
            icon = R.drawable.ic_alert_info,
            title = title,
            message = message,
            type = CustomDialog.TypeDialog.INFO
        )
    }

    override fun showErrorDialog(@StringRes message: Int, onAccept: () -> Unit) {
        showDialog(
            icon = R.drawable.ic_alert_info,
            message = message,
            onAccept = onAccept,
            type = CustomDialog.TypeDialog.INFO
        )
    }

    protected fun showErrorDialog(
        @StringRes title: Int? = null,
        @StringRes message: Int? = null,
        @StringRes positiveText: Int? = null,
        onAccept: (() -> Unit)? = {}
    ) {
        context?.let {
            val errorDialog = CustomDialog(
                context = it,
                title = title,
                message = message,
                positiveText = positiveText,
                onAccept = onAccept,
                typeDialog = CustomDialog.TypeDialog.INFO
            )
            errorDialog.show()
        }
    }

    override fun showErrorDialogAndFinish(error: Int) {
        showErrorDialog(title = error) {
            if (isAdded) {
                findNavController().navigateUp()
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

    override fun showConfirmDialog(
        title: String,
        onAccept: (() -> Unit)?,
        onCancel: (() -> Unit)?
    ) {
        showDialog(
            icon = R.drawable.ic_alert_info,
            onAccept = onAccept,
            onCancel = onCancel,
            type = CustomDialog.TypeDialog.INFO,
            titleString = title
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

    override fun showTooManyRequestDialog() {
        showDialog(
            icon = R.drawable.ic_alert_info,
            message = R.string.too_many_request_error_message,
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
        type: CustomDialog.TypeDialog = CustomDialog.TypeDialog.INFO,
        messageString: String? = null,
        titleString:  String? = null
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
                    onCancel = onCancel,
                    typeDialog = type,
                    messageString = messageString,
                    titleString = titleString
                )
                errorDialog.show()
            }
        }
    }

    protected fun showInfoDialog(
        @StringRes message: Int,
        args: Array<String>
    ) {
        val infoDialog = CustomDialog(
            requireActivity(),
            message = message,
            args = args
        )
        infoDialog.show()
    }

    protected fun showWarningDialog(
        @StringRes title: Int? = null,
        @StringRes message: Int? = null,
        @StringRes positiveText: Int? = null,
        @StringRes cancelText: Int? = null,
        onCancel: (() -> Unit)? = null,
        onAccept: (() -> Unit)? = {}
    ) {
        context?.let {
            val warningDialog = CustomDialog(
                icon = R.drawable.ic_warning,
                context = it,
                title = title,
                message = message,
                positiveText = positiveText,
                onAccept = onAccept,
                typeDialog = CustomDialog.TypeDialog.WARNING,
                negativeText = cancelText,
                onCancel = onCancel
            )
            warningDialog.show()
        }
    }

    override fun showConfirmDialog(
        title: Int?,
        message: String,
        onAccept: (() -> Unit)?,
        onCancel: (() -> Unit)?
    ) {
        showDialog(
            icon = R.drawable.ic_alert_info,
            title = title,
            onAccept = onAccept,
            onCancel = onCancel,
            messageString = message
        )
    }

    protected fun showSuccessDialog(
        @StringRes title: Int? = null,
        @StringRes positiveText: Int? = null,
        onAccept: (() -> Unit)? = {}
    ) {
        context?.let {
            val successDialog = CustomDialog(
                icon = R.drawable.ic_success,
                context = it,
                title = title,
                positiveText = positiveText,
                onAccept = onAccept,
                typeDialog = CustomDialog.TypeDialog.SUCCESS
            )
            successDialog.show()
        }
    }

    override fun showUserLoggedNeededDialog(
        mSSPAAuthEntity: MsspaAuthenticationEntity?,
        requiredScope: String
    ) {
        showConfirmDialog(
            title = R.string.user_not_logged,
            onAccept = {
                activity?.let {
                    (requireActivity() as MainActivity).launchLogin(
                        mSSPAAuthEntity = mSSPAAuthEntity,
                        requiredScope = requiredScope
                    )
                }
            },
            onCancel = { })
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
        if (isAdded) {
            if (activity != null) {
                (activity as MainActivity).hideLoading()
            }
        }
    }

    /**
     * Animate the views included with the anim introduced.
     * @param element View/s: views to animate
     * @param animId @AnimRes Int: animation to use
     * @param postAnimation: Callback executed after the animation ends
     * */
    protected fun animateViews(
        vararg element: View,
        @AnimRes animId: Int,
        postAnimation: () -> Unit = {}
    ) {
        val anim = AnimationUtils.loadAnimation(requireContext(), animId).apply {
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                    // Does nothing
                }

                override fun onAnimationEnd(animation: Animation?) {
                    postAnimation.invoke()
                }

                override fun onAnimationStart(animation: Animation?) {
                    // Does nothing
                }
            })
        }
        element.forEach {
            with(it) {
                startAnimation(anim)
            }
        }
    }

    protected fun checkViewVisibleInScroll(scroll: View, toCheckView: View): Boolean {
        val scrollBounds = Rect()
        scroll.getDrawingRect(scrollBounds)

        val top = toCheckView.y
        val bottom = top + toCheckView.height

        return (scrollBounds.top < top && scrollBounds.bottom >= bottom)
    }

    override fun onDetach() {
        super.onDetach()
        hideLoading()
        presenter.unsubscribe()
    }
}
