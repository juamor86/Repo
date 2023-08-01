package es.inteco.conanmobile.presentation.dialog

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import es.inteco.conanmobile.R
import kotlinx.android.synthetic.main.view_custom_dialog.*


/**
 * Custom dialog
 *
 * @property icon
 * @property title
 * @property message
 * @property messageText
 * @property positiveText
 * @property args
 * @property onAccept
 * @property negativeText
 * @property onCancel
 * @property typeDialog
 * @constructor
 *
 * @param context
 */
class CustomDialog(
    context: Context,
    @DrawableRes private val icon: Int? = null,
    @StringRes private val title: Int? = null,
    @StringRes private val message: Int? = null,
    private val messageText: String? = null,
    @StringRes private val positiveText: Int? = null,
    private val args: Array<String> = emptyArray(),
    private val onAccept: (() -> Unit)? = null,
    @StringRes private val negativeText: Int? = null,
    private val onCancel: (() -> Unit)? = null,
    private val typeDialog: TypeDialog = TypeDialog.INFO
) : AlertDialog(context) {

    /**
     * Type dialog
     *
     * @property textColor
     * @property cancelBackgroundColor
     * @property acceptBackgroundColor
     * @constructor Create empty Type dialog
     */
    enum class TypeDialog(
        @ColorRes val textColor: Int,
        @DrawableRes val cancelBackgroundColor: Int,
        @DrawableRes val acceptBackgroundColor: Int
    ) {
        /**
         * Error
         *
         * @constructor Create empty Error
         */
        ERROR(
            R.color.red_orange,
            R.drawable.selector_button_error,
            R.drawable.selector_accept_button_error
        ),

        /**
         * Warning
         *
         * @constructor Create empty Warning
         */
        WARNING(
            R.color.yellow_warning,
            R.drawable.selector_button_warning,
            R.drawable.selector_accept_button_warning
        ),

        /**
         * Info
         *
         * @constructor Create empty Info
         */
        INFO(
            R.color.dark_grey,
            R.drawable.selector_button_info,
            R.drawable.selector_acept_button_info
        ),

        /**
         * Success
         *
         * @constructor Create empty Success
         */
        SUCCESS(
            R.color.green_selected,
            R.drawable.selector_button_success,
            R.drawable.selector_accept_button_success
        );
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_custom_dialog)
        window!!.decorView.setBackgroundColor(Color.TRANSPARENT)

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
                movementMethod = LinkMovementMethod.getInstance()
                text = if (args.isEmpty()) {
                    context.getText(it)
                } else {
                    context.getString(it, *args)
                }
                visibility = View.VISIBLE
            }
        }
        messageText?.let {
            message_tv.apply {
                text = it
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
            setCancelable(true)
        } ?: setCancelable(false)

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
                TypeDialog.SUCCESS, TypeDialog.ERROR -> {
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
