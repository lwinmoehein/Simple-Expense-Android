package lab.justonebyte.simpleexpense.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
fun getFormattedYear(timestamp:Long):String{
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy")
    return formatter.format(date)
}

@SuppressLint("SimpleDateFormat")
fun getFormattedMonth(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("MMMM yyyy")
    return formatter.format(date)
}
@SuppressLint("SimpleDateFormat")
fun getFormattedDay(timestamp: Long): String {
    val date = Date(timestamp)
    val formatter = SimpleDateFormat("yyyy-MM-dd")
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
    val formatter = SimpleDateFormat("MMMM yyyy")
    return formatter.format(date)
}
@SuppressLint("SimpleDateFormat")
fun getCurrentDay(): String {
    val date = Date(System.currentTimeMillis())
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(date)
}