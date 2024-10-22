package lab.justonebyte.simpleexpense.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("SimpleDateFormat")
fun getFormattedYear(timestamp:Long,locale: Locale? = Locale.ENGLISH):String{
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy",locale)
    return formatter.format(date)
}

@SuppressLint("SimpleDateFormat")
fun getFormattedMonth(timestamp: Long,locale: Locale? = Locale.ENGLISH): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy-MM",locale)
    return formatter.format(date)
}

@SuppressLint("SimpleDateFormat")
fun getReadableFormattedMonth(timestamp: Long,locale: Locale? = Locale.ENGLISH): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("MMMM",locale)
    return formatter.format(date)
}
@SuppressLint("SimpleDateFormat")
fun getFormattedDay(timestamp: Long,locale: Locale? = Locale.ENGLISH): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy-MM-dd",locale)
    return formatter.format(date)
}

@SuppressLint("SimpleDateFormat")
fun getReadableFormattedDay(timestamp: Long,locale: Locale? = Locale.ENGLISH): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("MMMM dd yyyy",locale)
    return formatter.format(date)
}

@SuppressLint("SimpleDateFormat")
fun getCurrentYear(locale: Locale? = Locale.ENGLISH):String{
    val date = Date(System.currentTimeMillis())
    val formatter = SimpleDateFormat("yyyy",locale)
    return formatter.format(date)
}

@SuppressLint("SimpleDateFormat")
fun getCurrentMonth(locale: Locale? = Locale.ENGLISH): String {
    val date = Date(System.currentTimeMillis())
    val formatter = SimpleDateFormat("yyyy-MM",locale)
    return formatter.format(date)
}


@SuppressLint("SimpleDateFormat")
fun getCurrentDay(locale: Locale? = Locale.ENGLISH): String {
    val date = Date(System.currentTimeMillis())
    val formatter = SimpleDateFormat("yyyy-MM-dd",locale)
    return formatter.format(date)
}
fun getCurrentDayFromTimestamp(timestampMillis: Long, locale: Locale = Locale.ENGLISH): String {
    val calendar = Calendar.getInstance(locale)
    calendar.timeInMillis = timestampMillis
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    val formatter = SimpleDateFormat("yyyy-MM-dd", locale)
    return formatter.format(calendar.time)
}

fun getTimeStampForYearStart(year: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}
fun getTimeStampForYearEnd(year: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(year, Calendar.DECEMBER, 31, 23, 59, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar.timeInMillis
}

fun getTimeStampForStartDate(dateString: String): Long {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = dateFormat.parse(dateString)

    val calendar = Calendar.getInstance()
    if (date != null) {
        calendar.time = date
    }

    // Set time to 00:00:00 for start of day
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.timeInMillis
}
fun getTimeStampForEndDate(dateString: String): Long {
    val dateFormat = SimpleDateFormat("yyyy-dd-MM", Locale.getDefault())
    val date = dateFormat.parse(dateString)

    val calendar = Calendar.getInstance()
    if (date != null) {
        calendar.time = date
    }

    // Set time to 23:59:59.999 for end of day (almost midnight)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 999)

    return calendar.timeInMillis
}

fun getTimestampForMonthStart(yearMonthString: String): Long {
    val (year, month) = yearMonthString.split("-").map { it.toInt() }
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, 1, 0, 0, 0) // Month is 0-indexed in Calendar
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

fun convertTimestampIfNeeded(timestampInMillis: Long): Long {
    val cal = Calendar.getInstance()
    cal.timeInMillis = timestampInMillis

    // Check if hours, minutes, and seconds are all zero
    if (cal.get(Calendar.HOUR_OF_DAY) == 0 &&
        cal.get(Calendar.MINUTE) == 0 &&
        cal.get(Calendar.SECOND) == 0) {

        // Set the new time to 00:00:30
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 30)
        cal.set(Calendar.MILLISECOND, 0)

        return cal.timeInMillis
    } else {
        // Timestamp is not 00:00:00, return original timestamp
        return timestampInMillis
    }
}

fun getTimestampForMonthEnd(yearMonthString: String): Long {
    val (year, month) = yearMonthString.split("-").map { it.toInt() }
    val calendar = Calendar.getInstance()
    calendar.set(year, month , calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 0, 0, 0)
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar.timeInMillis
}

fun getTimestampFromDateTimeString(dateTimeString: String,locale: Locale? = Locale.ENGLISH): Long {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale)
    val date = dateFormat.parse(dateTimeString) ?: return 0

    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.timeInMillis
}