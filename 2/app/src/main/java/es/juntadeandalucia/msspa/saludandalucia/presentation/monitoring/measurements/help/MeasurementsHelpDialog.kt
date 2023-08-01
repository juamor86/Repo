package es.juntadeandalucia.msspa.saludandalucia.presentation.monitoring.measurements.help

import android.os.Bundle
import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomFullScreenDialog
import kotlinx.android.synthetic.main.fragment_measurements_info.*

class MeasurementsHelpDialog(private val helpText: String) : CustomFullScreenDialog() {

    override fun bindContentLayout(): Int = R.layout.fragment_measurements_info

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        help_tv.text = helpText
    }
}
