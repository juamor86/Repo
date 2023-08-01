package es.juntadeandalucia.msspa.authentication.presentation.base

import androidx.annotation.StringRes
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationError
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationWarning
import javax.crypto.Cipher

class BaseContract {

    interface View {
        fun showLoading() {}
        fun hideLoading() {}
        fun showLoadingBlocking() {}
        fun showErrorView() {}
        fun showEmptyView() {}
        fun authenticateForEncryption(
            onSuccess: (Cipher?, Cipher?) -> Unit,
            onError: (Throwable) -> Unit,
            onErrorInt: (Int) -> Unit? = {},
            title: Int? = null,
            subtitle: Int? = null,
            negativeButtonText: Int? = null,
            encrypt: Boolean,
            keyString:String,
            isNeedCipher:Boolean
        ) {
        }

        fun authenticateForDecryption(
            onSuccess: (Cipher) -> Unit,
            onError: (Throwable) -> Unit,
            onErrorInt: (Int) -> Unit? = {},
            keyString:String
        ) {
        }

        fun sendEvent( event: String) {}
        fun setResultSuccess(entity: MsspaAuthenticationEntity)
        fun setResultError(errorType: MsspaAuthenticationError)
        fun setResultWarning(warningType: MsspaAuthenticationWarning)
        fun showDialog(
            @StringRes title: Int? = null,
            onAccept: (() -> Unit)? = null,
            onCancel: (() -> Unit)? = null
        )
        fun showConfirm(
            @StringRes title: Int? = null,
            @StringRes message: Int? = null,
            onAccept: (() -> Unit)? = null,
            onCancel: (() -> Unit)? = null
        )


        fun showError(error: MsspaAuthenticationError)
        fun showError(error: Int)
        fun showWarning(
            warning: MsspaAuthenticationWarning,
            onAccept: (() -> Unit)? = null,
            onCancel: (() -> Unit)? = null
        )

        fun showWarning(message: Int, onAccept: (() -> Unit)?)
        fun isPhoneSecured(): Boolean
        fun showWarning(
            title: Int,
            message: Int,
            onAccept: (() -> Unit)?,
            onCancel: (() -> Unit)?)
    }

    interface Presenter {
        fun onCreate() {}
        fun onResume() {}
        fun unsubscribe() {}
        fun onViewCreated(){}
        fun setViewContract(baseFragment: View)
    }
}
