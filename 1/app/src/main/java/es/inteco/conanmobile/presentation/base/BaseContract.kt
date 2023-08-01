package es.inteco.conanmobile.presentation.base

import androidx.annotation.StringRes

/**
 * Base contract
 *
 * @constructor Create empty Base contract
 */
class BaseContract {

    /**
     * View
     *
     * @constructor Create empty View
     */
    interface View {
        /**
         * Show loading
         *
         */
        fun showLoading() {
            // Empty default implementation
        }

        /**
         * Show loading blocking
         *
         */
        fun showLoadingBlocking() {
            // Empty default implementation
        }

        /**
         * Hide loading
         *
         */
        fun hideLoading() {
            // Empty default implementation
        }

        /**
         * Show error dialog
         *
         */
        fun showErrorDialog() {
            // Empty default implementation
        }

        /**
         * Show error dialog
         *
         * @param title
         */
        fun showErrorDialog(@StringRes title: Int) {
            // Empty default implementation
        }

        /**
         * Show error dialog
         *
         * @param title
         * @param message
         */
        fun showErrorDialog(@StringRes title: Int, @StringRes message: Int) {
            // Empty default implementation
        }

        /**
         * Show error dialog
         *
         * @param message
         * @param onAccept
         * @receiver
         */
        fun showErrorDialog(@StringRes message: Int, onAccept: (() -> Unit)) {
            // Empty default implementation
        }

        /**
         * Show confirm dialog
         *
         * @param title
         * @param message
         * @param positiveText
         * @param cancelText
         * @param onAccept
         * @param onCancel
         */
        fun showConfirmDialog(
            @StringRes title: Int? = null,
            @StringRes message: Int,
            @StringRes positiveText: Int? = null,
            @StringRes cancelText: Int? = null,
            onAccept: (() -> Unit)? = null,
            onCancel: (() -> Unit)? = null
        ) {
            // Empty default implementation
        }

        /**
         * Show empty view
         *
         */
        fun showEmptyView() {
            // Empty default implementation
        }

        /**
         * Show error view
         *
         */
        fun showErrorView() {
            // Empty default implementation
        }

        /**
         * Navigate up
         *
         */
        fun navigateUp() {
            // Empty default implementation
        }
    }

    /**
     * Presenter
     *
     * @constructor Create empty Presenter
     */
    interface Presenter {
        /**
         * On create
         *
         */
        fun onCreate() {
            // Empty default implementation
        }

        /**
         * Unsubscribe
         *
         */
        fun unsubscribe() {
            // Empty default implementation
        }

        /**
         * Set view contract
         *
         * @param baseFragment
         */
        fun setViewContract(baseFragment: View)

        /**
         * On view created
         *
         */
        fun onViewCreated() {
            // Empty default implementation
        }
    }
}
