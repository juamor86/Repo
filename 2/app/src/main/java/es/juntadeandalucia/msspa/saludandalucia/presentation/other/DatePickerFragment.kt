package es.juntadeandalucia.msspa.saludandalucia.presentation.other

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import es.juntadeandalucia.msspa.saludandalucia.R
import java.util.Calendar
import java.util.Date

class DatePickerFragment() : DialogFragment() {

    private var listener: DatePickerDialog.OnDateSetListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(),
            R.style.PickerTheme, listener, year, month, day)
    }

    companion object {
        fun newInstance(listener: (Date) -> Unit): DatePickerFragment {
            val fragment = DatePickerFragment()
            fragment.listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                listener(cal.time)
            }
            return fragment
        }
    }
}
