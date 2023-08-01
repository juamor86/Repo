package es.juntadeandalucia.msspa.saludandalucia.presentation.advices.dialog

import android.os.Bundle
import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomFullScreenDialog
import kotlinx.android.synthetic.main.view_avisas.*
import kotlinx.android.synthetic.main.view_custom_full_screen_bottom_sheet_dialog.*

class AdviceDialog(private val onHideDialog: (View) -> Unit
) : CustomFullScreenDialog() {
    override fun bindContentLayout() = R.layout.view_avisas

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        cancel_avisas_btn.setOnClickListener(onHideDialog)
        close_iv.setOnClickListener(onHideDialog)
    }
}