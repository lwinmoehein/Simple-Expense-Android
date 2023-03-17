package lab.justonebyte.moneysubuu.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


val globalTimestampFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")

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