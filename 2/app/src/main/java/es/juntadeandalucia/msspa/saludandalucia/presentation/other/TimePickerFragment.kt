package es.juntadeandalucia.msspa.saludandalucia.presentation.other

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import es.juntadeandalucia.msspa.saludandalucia.R
import es.juntadeandalucia.msspa.saludandalucia.utils.UtilDateFormat
import java.util.Calendar
import java.util.Date

class TimePickerFragment() : DialogFragment() {

    private var listener: TimePickerDialog.OnTimeSetListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val actualTime = UtilDateFormat.timeToString(Date()).split(":")
        return TimePickerDialog(requireContext(), R.style.PickerTheme, listener, actualTime[0].toInt(), actualTime[1].toInt(), true)
    }

    companion object {
        fun newInstance(listener: (Date) -> Unit): TimePickerFragment {
            val fragment = TimePickerFragment()
            fragment.listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                listener(cal.time)
            }
            return fragment
        }
    }
}
