package es.juntadeandalucia.msspa.saludandalucia.presentation.base

import androidx.annotation.StringRes
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationEntity
import es.juntadeandalucia.msspa.saludandalucia.R

class BaseContract {

    interface View {
        fun showLoading() {}
        fun showLoadingBlocking() {}
        fun hideLoading() {}
        fun showErrorDialog() {}
        fun showErrorDialogAndBack() {}
        fun showErrorDialog(@StringRes title: Int) {}
        fun showErrorDialog(@StringRes title: Int, @StringRes message: Int) {}
        fun showErrorDialog(@StringRes message: Int, onAccept: (() -> Unit)) {}
        fun showConfirmDialog(
            @StringRes title: Int? = null,
            @StringRes message: Int? = null,
            onAccept: (() -> Unit)? = null,
            onCancel: (() -> Unit)? = null
        ) {
        }
        fun showConfirmDialog(
            @StringRes title: Int? = null,
            message: String,
            onAccept: (() -> Unit)? = null,
            onCancel: (() -> Unit)? = null
        ) {
        }
        fun showConfirmDialog(
            title: String,
            onAccept: (() -> Unit)? = null,
            onCancel: (() -> Unit)? = null
        ) {
        }
        fun showEmptyView() {}
        fun showErrorView() {}
        fun showTooManyRequestDialog() {}
        fun showErrorDialogAndFinish(error: Int = R.string.dialog_error_text) {}
        fun showInfoDialog(@StringRes message: Int, onAccept: () -> Unit)
        fun sendEvent( event: String, onComplete: () -> Unit = {}) {}
        fun showUserLoggedNeededDialog(
            mSSPAAuthEntity: MsspaAuthenticationEntity? = null,
            requiredScope: String = ""
        ) {
        }
    }

    interface Presenter {
        fun unsubscribe() {}
        fun setViewContract(baseFragment: View)
        fun onViewCreated() {}
    }
}
