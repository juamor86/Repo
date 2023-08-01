package es.juntadeandalucia.msspa.saludandalucia.presentation.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.utils.Utils
import kotlinx.android.synthetic.main.view_custom_dialog.accept_btn
import kotlinx.android.synthetic.main.view_custom_dialog.cancel_btn
import kotlinx.android.synthetic.main.view_custom_dialog.icon_iv
import kotlinx.android.synthetic.main.view_custom_dialog.message_tv
import kotlinx.android.synthetic.main.view_custom_dialog.title_tv

class CustomDialog(
    context: Context,
    @DrawableRes private val icon: Int? = null,
    @StringRes private val title: Int? = null,
    @StringRes private val message: Int? = null,
    @StringRes private val positiveText: Int? = null,
    private val args: Array<String> = emptyArray(),
    private val onAccept: (() -> Unit)? = null,
    @StringRes private val negativeText: Int? = null,
    private val onCancel: (() -> Unit)? = null,
    private val typeDialog: TypeDialog = TypeDialog.INFO,
    private val messageString: String? = null,
    private val titleString: String? = null
) :
    AlertDialog(context) {

    enum class TypeDialog(@ColorRes val textColor: Int, @DrawableRes val cancelBackgroundColor: Int, @DrawableRes val acceptBackgroundColor: Int, val allowTouchOutside: Boolean = false) {
        ERROR(R.color.red_orange, R.drawable.selector_button_error, R.drawable.selector_accept_button_error),
        WARNING(R.color.yellow_warning, R.drawable.selector_button_warning, R.drawable.selector_accept_button_warning),
        INFO(R.color.blue_chill, R.drawable.selector_button_info, R.drawable.selector_acept_button_info),
        SUCCESS(R.color.la_palma, R.drawable.selector_button_success, R.drawable.selector_accept_button_success,);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.let { Utils.secureAgainstScreenshots(it) }
        setContentView(R.layout.view_custom_dialog)
        window!!.decorView.setBackgroundColor(Color.TRANSPARENT)
        setCanceledOnTouchOutside(typeDialog.allowTouchOutside)
        setCancelable(false)
        icon?.let {
            icon_iv.setImageResource(icon)
        }

        title?.let {
            title_tv.apply {
                text = if (args.isEmpty()) {
                    context.getText(it)
                } else {
                    context.getString(it, *args)
                }
                visibility = View.VISIBLE
            }
        }
        message?.let {
            message_tv.apply {
                text = if (args.isEmpty()) {
                    context.getText(it)
                } else {
                    context.getString(it, *args)
                }
                visibility = View.VISIBLE
            }
        }
        messageString?.let {
            message_tv.apply {
                message_tv.movementMethod = LinkMovementMethod.getInstance()

                text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY)

                visibility = View.VISIBLE
            }
        }
        titleString?.let {
            title_tv.apply {
                message_tv.movementMethod = LinkMovementMethod.getInstance()

                text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY)

                visibility = View.VISIBLE
            }
        }
        positiveText?.let {
            accept_btn.text = context.getString(it)
        }
        accept_btn.setOnClickListener {
            dismiss()
            onAccept?.invoke()
        }
        onCancel?.apply {
            cancel_btn.apply {
                negativeText?.let {
                    text = context.getString(it)
                }
                setOnClickListener {
                    dismiss()
                    invoke()
                }
                visibility = View.VISIBLE
            }
        }

        with(typeDialog) {
            when (this) {
                TypeDialog.WARNING -> {
                    accept_btn.apply {
                        setTextColor(resources.getColor(R.color.white))
                        background = context.getDrawable(acceptBackgroundColor)
                    }
                    cancel_btn.apply {
                        setTextColor(resources.getColor(textColor))
                        background = context.getDrawable(cancelBackgroundColor)
                    }
                }
                TypeDialog.SUCCESS -> {
                    accept_btn.apply {
                        setTextColor(resources.getColor(R.color.white))
                        background = context.getDrawable(acceptBackgroundColor)
                    }
                }
                TypeDialog.ERROR -> {
                    accept_btn.apply {
                        setTextColor(resources.getColor(R.color.white))
                        background = context.getDrawable(acceptBackgroundColor)
                    }
                }
                else -> {
                    cancel_btn.apply {
                        setTextColor(resources.getColor(textColor))
                        background = context.getDrawable(cancelBackgroundColor)
                    }
                    accept_btn.apply {
                        setTextColor(resources.getColor(R.color.white))
                        background = context.getDrawable(R.drawable.selector_acept_button_info)
                    }
                }
            }
        }
    }
}
