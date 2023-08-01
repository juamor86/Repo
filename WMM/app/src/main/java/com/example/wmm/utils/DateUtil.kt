package com.example.wmm.utils

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.Year
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.temporal.ChronoField
import java.time.temporal.WeekFields
import java.util.*

class DateUtil {

    companion object {
        private val shortDateFormatter: SimpleDateFormat =
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        private fun getShortDateFormatter(): SimpleDateFormat {
            shortDateFormatter.timeZone = TimeZone.getTimeZone("UTC")
            return shortDateFormatter
        }

        fun getTodayShortDate(): String? = getShortDateFormatter().format(Date())

        fun parseLongToStringShortDate(timeMillis: Long): String =
            getShortDateFormatter().format(Date(timeMillis))

        fun parseDateToStringShortDate(date: Date): String =
            getShortDateFormatter().format(date)

        fun parseStringShortDateToDate(date: String): Date = getShortDateFormatter().parse(date)

        fun parseDateToDateShortFormat(date: Date): Date =
            getShortDateFormatter().parse(getShortDateFormatter().format(date))!!

        fun isDateInThisYear(date: Date): Boolean {
            val givenLocalDate: LocalDateTime =
                LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC)
            val currentYear: Year = Year.now(ZoneOffset.UTC)

            return currentYear == Year.from(givenLocalDate)
        }

        fun isDateInThisMonth(date: Date): Boolean {
            val givenLocalDateTime: LocalDateTime =
                LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC)
            val currentMonth: YearMonth = YearMonth.now(ZoneOffset.UTC)
            return currentMonth == YearMonth.from(givenLocalDateTime)
        }

        fun isDateInThisWeek(date: Date): Boolean {
            val givenLocalDateTime: LocalDateTime =
                LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC)
            val givenWeekOfYear =
                givenLocalDateTime.get(WeekFields.of(Locale.getDefault()).weekOfYear())
            val currentLocalDateTime = LocalDateTime.ofInstant(Date().toInstant(), ZoneOffset.UTC)
            val currentWeekOfYear =
                currentLocalDateTime.get(WeekFields.of(Locale.getDefault()).weekOfYear())
            return isDateInThisMonth(date) && givenWeekOfYear == currentWeekOfYear
        }

        fun getFirstDayOfYear(): Long {
            val cal: Calendar = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, 1)
            cal.set(Calendar.MONTH, 0)
            return parseDateToDateShortFormat(cal.time).time
        }

        fun getLastDayOfYear(): Long {
            val cal: Calendar = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, 31)
            cal.set(Calendar.MONTH, 11)
            return parseDateToDateShortFormat(cal.time).time
        }

        fun getFirstDayOfMonth(): Long {
            val cal: Calendar = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, 1)
            return parseDateToDateShortFormat(cal.time).time
        }

        fun getLastDayOfMonth(): Long {
            val cal: Calendar = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, 31)
            return parseDateToDateShortFormat(cal.time).time
        }

        fun getFirstDayOfWeek(): Long =
            LocalDateTime.now().with(ChronoField.DAY_OF_WEEK, 1).toLocalDate().atStartOfDay()
                .toEpochSecond(ZoneOffset.UTC) * 1000


        fun getLastDayOfWeek(): Long =
            LocalDateTime.now().with(ChronoField.DAY_OF_WEEK, 7).toLocalDate().atStartOfDay()
                .toEpochSecond(ZoneOffset.UTC) * 1000
    }
}