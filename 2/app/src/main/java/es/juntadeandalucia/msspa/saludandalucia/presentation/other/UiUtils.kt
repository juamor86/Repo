package es.juntadeandalucia.msspa.saludandalucia.presentation.other

import android.content.Context
import androidx.fragment.app.FragmentActivity
import java.util.Date

class UiUtils {

    enum class PickerType { DATE, TIME }
    companion object {
        fun showDatePicker(type: PickerType, context: Context, listener: (Date) -> Unit) {
            val dialog = if (type == PickerType.DATE) {
                DatePickerFragment.newInstance(listener)
            } else {
                TimePickerFragment.newInstance(listener)
            }

            dialog.show((context as FragmentActivity).supportFragmentManager, "datepicker")
        }
    }
}
