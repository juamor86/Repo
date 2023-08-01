package com.example.wmm.model.entities

import androidx.annotation.StringRes
import com.example.wmm.R

enum class MonthType(val id: Int, @StringRes val monthName: Int) {
    JANUARY(0, R.string.month_name_january),
    FEBRUARY(1, R.string.month_name_february),
    MARCH(2, R.string.month_name_march),
    APRIL(3, R.string.month_name_april),
    MAY(4, R.string.month_name_may),
    JUNE(5, R.string.month_name_june),
    JULY(6, R.string.month_name_july),
    AUGUST(7, R.string.month_name_august),
    SEPTEMBER(8, R.string.month_name_september),
    OCTOBER(9, R.string.month_name_october),
    NOVEMBER(10, R.string.month_name_november),
    DECEMBER(11, R.string.month_name_december);

    companion object {
        fun getMonthType(idMonth: Int) = values().find { it.id == idMonth }
    }
}