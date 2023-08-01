package es.juntadeandalucia.msspa.saludandalucia.presentation.dialog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AlertDialog
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import kotlinx.android.synthetic.main.view_custom_dialog.message_tv
import kotlinx.android.synthetic.main.view_custom_scrollable_dialog.accept_btn

class CustomScrollableDialog(context: Context,private val onAccept: (() -> Unit)) : AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_custom_scrollable_dialog)
        window?.let { Utils.secureAgainstScreenshots(it) }
        window!!.decorView.setBackgroundColor(Color.TRANSPARENT)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        message_tv.apply {
            movementMethod = ScrollingMovementMethod()
            text=resources.getText(R.string.scanner_info_text)
            movementMethod = LinkMovementMethod.getInstance()
        }

        accept_btn.setOnClickListener {
            dismiss()
            onAccept.invoke()
        }
    }
}