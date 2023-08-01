package com.example.wmm.model.entities

import androidx.annotation.StringRes
import com.example.wmm.R

enum class WeekType(val id: Int, @StringRes val weekName: Int) {
    FIRSTWEEK(0, R.string.first_week_name),
    SECONDWEEK(1, R.string.second_week_name),
    THIRDWEEK(2, R.string.third_week_name),
    FORTHWEEK(3, R.string.forth_week_name),
    FIFTHWEEK(4, R.string.fifth_week_name);

    companion object {
        fun getWeekOfMonth(idWeek: Int) = values().find { it.id == idWeek }
    }
}