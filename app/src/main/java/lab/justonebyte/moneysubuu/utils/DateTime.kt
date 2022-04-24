package lab.justonebyte.moneysubuu.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun dateFormatter(milliseconds: Long): String {
    return SimpleDateFormat("yyyy-MM-dd").format(Date(milliseconds)).toString()
}

@SuppressLint("SimpleDateFormat")
fun monthFormatter(milliseconds: Long): String {
    return SimpleDateFormat("yyyy-MM").format(Date(milliseconds)).toString()
}
@SuppressLint("SimpleDateFormat")
fun yearFormatter(milliseconds: Long): String {
    return SimpleDateFormat("yyyy").format(Date(milliseconds)).toString()
}