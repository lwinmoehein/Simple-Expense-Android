package lab.justonebyte.simpleexpense.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("SimpleDateFormat")
fun getFormattedYear(timestamp:Long):String{
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy")
    return formatter.format(date)
}

@SuppressLint("SimpleDateFormat")
fun getFormattedMonth(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy-MM")
    return formatter.format(date)
}

@SuppressLint("SimpleDateFormat")
fun getReadableFormattedMonth(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("MMMM")
    return formatter.format(date)
}
@SuppressLint("SimpleDateFormat")
fun getFormattedDay(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(date)
}

@SuppressLint("SimpleDateFormat")
fun getReadableFormattedDay(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("MMMM dd yyyy")
    return formatter.format(date)
}

@SuppressLint("SimpleDateFormat")
fun getCurrentYear():String{
    val date = Date(System.currentTimeMillis())
    val formatter = SimpleDateFormat("yyyy")
    return formatter.format(date)
}

@SuppressLint("SimpleDateFormat")
fun getCurrentMonth(): String {
    val date = Date(System.currentTimeMillis())
    val formatter = SimpleDateFormat("yyyy-MM")
    return formatter.format(date)
}


@SuppressLint("SimpleDateFormat")
fun getCurrentDay(): String {
    val date = Date(System.currentTimeMillis())
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(date)
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

fun getTimestampForMonthStart(yearMonthString: String): Long {
    val (year, month) = yearMonthString.split("-").map { it.toInt() }
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, 1, 0, 0, 0) // Month is 0-indexed in Calendar
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

fun getTimestampForMonthEnd(yearMonthString: String): Long {
    val (year, month) = yearMonthString.split("-").map { it.toInt() }
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59)
    calendar.set(Calendar.MILLISECOND, 999)
    return calendar.timeInMillis
}

fun getCurrentDayTimeMillisecond():Long{
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND,

        0)
    calendar.set(Calendar.MILLISECOND, 0)
    val dayStartTime = calendar.timeInMillis

    val currentTimeMillis = System.currentTimeMillis()
    return currentTimeMillis - dayStartTime
}
fun convertDateStringToTimestamp(dateString: String): Long {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = dateFormat.parse(dateString) ?: return 0

    val calendar = Calendar.getInstance()
    calendar.time = date
    return calendar.timeInMillis
}