package es.juntadeandalucia.msspa.saludandalucia.presentation.home.cert

import android.text.method.LinkMovementMethod
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomFullScreenDialog
import kotlinx.android.synthetic.main.view_item_on_boarding.*

class CertificateOnBoardingDialog(private val layout: Int, override val onDismiss: (() -> Any)?) : CustomFullScreenDialog() {

    override fun bindContentLayout() = layout

    override fun onShow() {
        on_boarding_text_tv.movementMethod = LinkMovementMethod.getInstance()
    }
}
