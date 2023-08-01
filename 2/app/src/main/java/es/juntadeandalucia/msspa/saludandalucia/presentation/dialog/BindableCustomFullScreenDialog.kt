package es.juntadeandalucia.msspa.saludandalucia.presentation.dialog

abstract class BindableCustomFullScreenDialog(override val onDismiss: (() -> Any)? = null) :
    CustomFullScreenDialog() {
    abstract override fun bindData()
}
