package es.juntadeandalucia.msspa.saludandalucia.presentation.quiz.dialog

import android.os.Bundle
import android.view.View
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.domain.entities.AppointmentEntity
import es.juntadeandalucia.msspa.saludandalucia.presentation.dialog.CustomFullScreenDialog
import kotlinx.android.synthetic.main.view_appointment.*

class AppointmentDialog(
    private val appointment: AppointmentEntity,
    private val onCancelAppointment: (View) -> Unit,
    private val onAddToCalendar: (View) -> Unit
) : CustomFullScreenDialog() {

    override fun bindContentLayout() = R.layout.view_appointment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        showAppointmentData()
    }

    private fun setupView() {
        cancel_appointment_btn.setOnClickListener(onCancelAppointment)
        add_to_calendar_btn.setOnClickListener(onAddToCalendar)
    }

    private fun showAppointmentData() {
        with(appointment) {
            appointment_date_tv.text = date
            appointment_center_tv.text = center
            appointment_topic_tv.text = topic
            appointment_task_tv.text = task
        }
    }
}
