package com.example.wmm.model.entities

import androidx.annotation.StringRes
import com.example.wmm.R

enum class DayType(val id: Int, @StringRes val dayName: Int) {
    MONDAY(2, R.string.day_name_monday),
    TUESDAY(3, R.string.day_name_tuesday),
    WEDNESDAY(4, R.string.day_name_wednesday),
    THURSDAY(5, R.string.day_name_thursday),
    FRIDAY(6, R.string.day_name_friday),
    SATURDAY(7, R.string.day_name_saturday),
    SUNDAY(1, R.string.day_name_sunday);

    companion object {
        fun getDayType(idDay: Int) = values().find { it.id == idDay }
    }
}