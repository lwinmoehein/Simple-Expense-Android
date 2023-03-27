package lab.justonebyte.moneysubuu.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


val globalTimestampFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

@SuppressLint("SimpleDateFormat")
fun dateFormatter(milliseconds: Long): String {
    return SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).format(Date(milliseconds)).toString()
}
@SuppressLint("SimpleDateFormat")
fun weekFormatter(milliseconds: Long): String {
    return SimpleDateFormat("ww",Locale.ENGLISH).format(Date(milliseconds)).toString()
}
@SuppressLint("SimpleDateFormat")
fun monthFormatter(milliseconds: Long): String {
    return SimpleDateFormat("yyyy-MM",Locale.ENGLISH).format(Date(milliseconds)).toString()
}
@SuppressLint("SimpleDateFormat")
fun yearFormatter(milliseconds: Long): String {
    return SimpleDateFormat("yyyy",Locale.ENGLISH).format(Date(milliseconds)).toString()
}
fun getCurrentDate():String{
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    return sdf.format(Date())
}
fun getCurrentMonth():String{
    val sdf = SimpleDateFormat("yyyy-MM", Locale.ENGLISH)
    return sdf.format(Date())
}
fun getCurrentYear():String{
    val sdf = SimpleDateFormat("yyyy", Locale.ENGLISH)
    return sdf.format(Date())
}
fun getCurrentGlobalTime():String{
    val currentDateTime = Date()
    return globalTimestampFormat.format(currentDateTime)
}
fun formatDateString(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = inputFormat.parse(dateString)

    val outputFormat = SimpleDateFormat("MMMM d , yyyy", Locale.getDefault())
    return outputFormat.format(date)
}
fun formatMonthString(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = inputFormat.parse(dateString)

    val outputFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    return outputFormat.format(date)
}
fun formatYearString(dateString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = inputFormat.parse(dateString)

    val outputFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    return outputFormat.format(date)
}
fun getFormatedDate(dateString:String,pattern: String ="MMMM d , yyyy"):Date{
    val inputFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return inputFormat.parse(dateString)
}
fun getFormatedMonth(dateString:String):Date{
    val inputFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    return inputFormat.parse(dateString)
}
fun getFormatedYear(dateString:String):Date{
    val inputFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    return inputFormat.parse(dateString)
}