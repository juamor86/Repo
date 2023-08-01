package es.inteco.conanmobile.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import es.inteco.conanmobile.R

/**
 * Top sheet dialog
 *
 * @constructor
 *
 * @param context
 */
abstract class TopSheetDialog(context: Context) : Dialog(context, R.style.AppTheme) {

    /**
     * Bind layout
     *
     * @return
     */
    abstract fun bindLayout(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindLayout())
        setTitle(null)
        val width = context.resources.displayMetrics.widthPixels
        window?.apply {
            setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes?.apply {
                gravity = Gravity.TOP
                windowAnimations = R.style.AppTheme
            }
        }
    }

}