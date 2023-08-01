package es.juntadeandalucia.msspa.authentication.presentation.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import es.juntadeandalucia.msspa.authentication.MsspaAuthenticationConfig
import es.juntadeandalucia.msspa.authentication.R
import es.juntadeandalucia.msspa.authentication.utils.Utils

class MsspaAuthCustomDialog(
        context: Context,
        @LayoutRes private val layout: Int,
        @StringRes private val title: Int? = null,
        @StringRes private val message: Int? = null,
        private val onAccept: (() -> Unit)? = null,
        private val onCancel: (() -> Unit)? = null,
        private val environment: MsspaAuthenticationConfig.Environment
) : AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.let { Utils.secureAgainstScreenshots(it, environment) }
        setContentView(layout)
        window!!.decorView.setBackgroundColor(Color.TRANSPARENT)

        findViewById<TextView>(R.id.title_tv)?.apply {
            title?.let {
                setText(it)
                visibility = View.VISIBLE
                Linkify.addLinks(this, Linkify.ALL)
                movementMethod = LinkMovementMethod.getInstance()
            } ?: run {
                visibility = View.GONE
            }
        }
        findViewById<TextView>(R.id.message_tv)?.apply {
            message?.let {
                text = context.getString(it)
                visibility = View.VISIBLE
            } ?: run {
                visibility = View.GONE
            }
        }
        findViewById<Button>(R.id.accept_btn)?.apply {
            setOnClickListener {
                onAccept?.invoke()
                dismiss()
            }
        }
        findViewById<Button>(R.id.cancel_btn)?.apply {
            if(onCancel != null){
                visibility = View.VISIBLE
            }
            setOnClickListener {
                onCancel?.invoke()
                dismiss()
            }
        }
    }
}
