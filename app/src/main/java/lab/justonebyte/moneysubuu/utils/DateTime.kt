package lab.justonebyte.moneysubuu.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun dateFormatter(milliseconds: Double): String {
    return SimpleDateFormat("dd/MM/yyyy").format(Date(milliseconds.toLong())).toString()
}