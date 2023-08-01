package es.juntadeandalucia.msspa.saludandalucia.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import es.juntadeandalucia.msspa.saludandalucia.R

abstract class TopSheetDialog(context: Context) : Dialog(context, R.style.TopDialog) {

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
                windowAnimations = R.style.TopDialog
            }
        }
    }
}
