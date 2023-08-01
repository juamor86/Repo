package es.juntadeandalucia.msspa.saludandalucia.presentation.home.cert

import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomFullScreenDialog
import kotlinx.android.synthetic.main.view_certificate_invalid.*

class InvalidCertificateDialog(
    override val onDismiss: (() -> Any)? = null,
    val expiration: String? = null
) :
    CustomFullScreenDialog() {

    override fun bindContentLayout() = R.layout.view_certificate_invalid

    override fun bindData() {
        super.bindData()
        expiration?.let {
            expired_tv.text = expired_tv.text.toString().plus(it)
            expired_tv.visibility = View.VISIBLE
        }
    }
}
