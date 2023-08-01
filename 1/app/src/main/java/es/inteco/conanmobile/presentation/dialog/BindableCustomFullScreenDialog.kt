package es.inteco.conanmobile.presentation.dialog

/**
 * Bindable custom full screen dialog
 *
 * @property onDismiss
 * @constructor Create empty Bindable custom full screen dialog
 */
abstract class BindableCustomFullScreenDialog(override val onDismiss: (() -> Any)? = null) :
    CustomFullScreenDialog() {
    abstract override fun bindData()
}
