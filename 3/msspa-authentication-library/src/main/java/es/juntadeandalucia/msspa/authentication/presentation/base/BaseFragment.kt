package es.juntadeandalucia.msspa.authentication.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationError
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationWarning
import es.juntadeandalucia.msspa.authentication.security.CrytographyManager
import es.juntadeandalucia.msspa.authentication.utils.ApiConstants
import es.juntadeandalucia.msspa.authentication.utils.BiometricUtils
import javax.crypto.Cipher
import javax.inject.Inject

abstract class BaseFragment : Fragment(), BaseContract.View {

    private val presenter by lazy { bindPresenter() }

    abstract fun bindPresenter(): BaseContract.Presenter

    @set:Inject
    var cryptoManager: CrytographyManager? = null

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

    override fun onStop() {
        super.onStop()
        presenter.unsubscribe()
    }

    fun hideKeyboard() {
        view?.let { (activity as BaseActivity).hideKeyboard(it) }
    }

    @LayoutRes
    abstract fun bindLayout(): Int


    override fun showLoading() {
        if (isAdded) {
            (activity as BaseActivity).apply {
                showLoading()
            }
        }
    }

    override fun showLoadingBlocking() {
        if (isAdded) {
            (activity as BaseActivity).apply {
                showLoadingBlocking()
            }
        }
    }

    override fun hideLoading() {
        if (isAdded) {
            (activity as BaseActivity).apply {
                hideLoading()
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

    override fun authenticateForEncryption(
        onSuccess: (Cipher?, Cipher?) -> Unit,
        onError: (Throwable) -> Unit,
        onErrorInt: (Int) -> Unit?,
        title: Int?,
        subtitle: Int?,
        negativeButtonText: Int?,
        encrypt: Boolean,
        keyString: String,
        isNeedCipher: Boolean
    ) {
        cryptoManager?.promptForEncryption(
            onSuccess = onSuccess,
            onError = onError,
            onErrorInt = onErrorInt,
            fragmentActivity = requireActivity(),
            keyString = keyString,
            title = title,
            subtitle = subtitle,
            negativeButtonText = negativeButtonText,
            encrypt = encrypt,
            isNeedCipher = isNeedCipher
        )
    }

    override fun authenticateForDecryption(
        onSuccess: (Cipher) -> Unit,
        onError: (Throwable) -> Unit,
        onErrorInt: (Int) -> Unit?,
        keyString: String
    ) {
        cryptoManager?.promptForDecryption(
            onSuccess,
            onError,
            onErrorInt,
            requireActivity(),
            keyString
        )
    }

    override fun sendEvent(event:String){
        if (isAdded) {
            (requireActivity() as BaseActivity).sendEvent(event)
        }
    }

    override fun setResultSuccess(entity: MsspaAuthenticationEntity) {
        if (isAdded) {
            (requireActivity() as BaseActivity).setResultSuccess(entity)
        }
    }

    override fun setResultError(errorType: MsspaAuthenticationError) {
        if (isAdded) {
            (requireActivity() as BaseActivity).setResultError(errorType)
        }
    }

    override fun setResultWarning(warningType: MsspaAuthenticationWarning) {
        if (isAdded) {
            (requireActivity() as BaseActivity).setResultWarning(warningType)
        }
    }

    override fun showError(error: MsspaAuthenticationError) {
        if (isAdded) {
            (requireActivity() as BaseActivity).showError(error)
        }
    }

    override fun showWarning(
        warning: MsspaAuthenticationWarning,
        onAccept: (() -> Unit)?,
        onCancel: (() -> Unit)?
    ) {
        if (isAdded) {
            (requireActivity() as BaseActivity).showWarning(warning, onAccept, onCancel)
        }
    }

    override fun showWarning(message: Int, onAccept: (() -> Unit)?) {
        if (isAdded) {
            (requireActivity() as BaseActivity).showWarning(
                message, onAccept
            )
        }
    }

    override fun showWarning(title:Int, message:Int, onAccept:(() -> Unit)?, onCancel: (() -> Unit)?) {
        if (isAdded) {
            (requireActivity() as BaseActivity).showWarning(title,
                message, onAccept, onCancel
            )
        }
    }

    override fun showError(error: Int) {
        if (isAdded) {
            (requireActivity() as BaseActivity).showError(
                error
            )
        }
    }

    override fun showDialog(title: Int?, onAccept: (() -> Unit)?, onCancel: (() -> Unit)?) {
        if (isAdded) {
            (requireActivity() as BaseActivity).showDialog(
                title, onAccept, onCancel
            )
        }
    }

    override fun showConfirm(title: Int?,message: Int?, onAccept: (() -> Unit)?, onCancel: (() -> Unit)?) {
        if (isAdded) {
            (requireActivity() as BaseActivity).showConfirm(
                title, message ,onAccept, onCancel
            )
        }
    }

    override fun isPhoneSecured(): Boolean = BiometricUtils.isBiometricOrSecured(requireContext())
}
