package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.dashboard.onboarding

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomFullScreenDialog
import kotlinx.android.synthetic.main.view_custom_full_screen_bottom_sheet_dialog.close_iv
import kotlinx.android.synthetic.main.view_item_on_boarding.on_boarding_iv
import kotlinx.android.synthetic.main.view_item_on_boarding.on_boarding_text_tv
import kotlinx.android.synthetic.main.view_item_on_boarding.on_boarding_title_tv
import kotlinx.android.synthetic.main.view_monitoring_program_on_boarding.close_btn

class MonitoringOnBoardingDialog() : CustomFullScreenDialog() {

    override fun bindContentLayout(): Int = R.layout.view_monitoring_program_on_boarding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(resources) {
            on_boarding_title_tv.text = getText(R.string.follow_up_title).toString().uppercase()

            on_boarding_iv.setImageResource(R.drawable.ic_monitoring_program)

            on_boarding_text_tv.text = getText(R.string.monitoring_on_boarding_first_access)
        }

        close_btn.setOnClickListener {
            dialog!!.dismiss()
        }

        close_iv.setOnClickListener {
            dialog!!.dismiss()
        }
    }
}