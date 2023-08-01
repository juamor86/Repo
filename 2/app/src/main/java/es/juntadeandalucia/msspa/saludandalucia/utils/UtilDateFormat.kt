package es.juntadeandalucia.msspa.saludandalucia.utils

import es.juntadeandalucia.msspa.saludandalucia.utils.Consts.Companion.DAY_MILLISECONDS
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.datetime.Instant
import java.time.temporal.ChronoUnit

class UtilDateFormat {

    companion object {
        const val DATE_FORMAT_TZ = "yyyy-MM-dd'T'HH:mm:ss"
        const val DATE_FORMAT_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSSX"
        const val DATE_FORMAT_TZ_SLASH = "yyyy/MM/dd'T'HH:mm:ss"


        private val parserTimeZone =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        private val parserTimeCertificate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        private val hourFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        private val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        private val dateFormatterShort = SimpleDateFormat("dd MMM", Locale.getDefault())
        private val dateFormatterNotification = SimpleDateFormat("dd MMM yy", Locale.getDefault())
        private val formatterDateMonthName = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        private val formatterDateTime = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())
        private val formatterDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        private val formatterTime = SimpleDateFormat("HH:mm", Locale.getDefault())
        private val simpleFormatterTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        fun getHoursDate(date: Long): String {
            return hourFormatter.format(Date(date))
        }

        fun getDateFormatter(date: Long): String {
            return dateFormatter.format(Date(date))
        }

        fun getDateFormatterShort(date: Long): String {
            return dateFormatterShort.format(Date(date))
        }

        fun getDateFormatterNotification(date: Long): String {
            return dateFormatterNotification.format(Date(date))
        }

        fun getStringDateToLong(date: String): Long {
            return parserTimeZone.parse(date).time
        }

        fun getStringTraditionalShortDateToLong(date: String): Long =
            formatterDate.parse(date).time

        fun getStringDateCertificateToLong(date: String): Long {
            return parserTimeCertificate.parse(date).time
        }

        fun dateToStringMonthName(date: Date) = formatterDateMonthName.format(date)

        fun formatStringDateTime(valueDate: String): Date = formatterDateTime.parse(valueDate)

        fun dateToString(date: Date): String {
            return formatterDate.format(date)
        }

        fun timeToString(date: Date): String {
            return formatterTime.format(date)
        }

        fun dateTimeToString(date: Date): String {
            return formatterDateTime.format(date)
        }

        fun formatDateString(valueDate: String?): String? {
            if (valueDate == null) {
                return null
            }
            return dateToString(stringToDate(valueDate, "dd-MM-yyyy"))
        }

        fun formatAmericanToStandardDate(valueDate: String): String {
            val array = valueDate.split("-")
            return array[2] + "/" + array[1] + "/" + array[0]
        }

        fun formatTimeString(valueDate: String?): String? {
            if (valueDate == null) {
                return null
            }
            return timeToString(stringToDate(valueDate, "HH:mm"))
        }

        fun formatDateTime(valueDateTime: String?): String {
            if (valueDateTime == null) {
                return ""
            }
            return dateTimeToString(stringToDate(valueDateTime, DATE_FORMAT_TZ))
        }

        fun stringToDate(datStr: String, format: String): Date {
            val formater = SimpleDateFormat(format, Locale.getDefault())
            return formater.parse(datStr)!!
        }

        fun dateToString(date: Date, format: String): String =
            SimpleDateFormat(format, Locale.getDefault()).format(date)

        fun daysBetweenDates(firstDate: Long, lastDate: Long) =
            (firstDate - lastDate) / DAY_MILLISECONDS

        fun instantToDateShortFormat(instant: Instant?): Date =
            parserTimeCertificate.parse(instant.toString().substring(0, 11))!!

        fun formatToAmericanSimpleDatetoString(date: Date): String =
            parserTimeCertificate.format(date)

        fun formatToSimpleTimeString(date: Date): String {
            val cosa = simpleFormatterTime.format(date)
            val stringArray = cosa.split(":").toMutableList()
            stringArray[2] = "00"
            return stringArray.joinToString(":")
        }
    }
}
