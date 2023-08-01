package es.juntadeandalucia.msspa.saludandalucia.presentation.dynamic_questionnaires.questionnaires.detail.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import es.juntadeandalucia.msspa.saludandalucia.R
import kotlinx.android.synthetic.main.view_image_detail_dialog.*


class ImageDetailDialog(context: Context, theme: Int = R.style.AppTheme_FullScreenDialog) :
    AlertDialog(context, theme) {

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        this.window!!.setLayout(width, height)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_image_detail_dialog)
        close_iv.setOnClickListener {
            this.dismiss()
        }
    }

    fun setImage(bitmap: Bitmap) {
        picture_detail_iv.setImageBitmap(bitmap)
    }
}