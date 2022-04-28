package lab.justonebyte.moneysubuu.ui.components


import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import lab.justonebyte.moneysubuu.utils.dateFormatter
import java.util.*

@Composable
fun DatePicker(
    date:String= dateFormatter(System.currentTimeMillis()),
    onDateChosen:(date:String)->Unit,
    isShown:Boolean =false
){


    // Fetching the Local Context
    val mContext = LocalContext.current

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()
    mCalendar.time = Date()

    // Declaring integer values
    // for year, month and day
    val mYear = remember {
        val splitedDate = date.split('-')
        if(splitedDate.size>2){
            mutableStateOf(splitedDate[0].toInt())
        }else{
            mutableStateOf(mCalendar.get(Calendar.YEAR))
        }
    }
    val mMonth = remember {
        val splitedDate = date.split('-')
        if(splitedDate.size>2){
            mutableStateOf(splitedDate[1].toInt())
        }else{
            mutableStateOf(mCalendar.get(Calendar.MONTH))
        }
    }
    val mDay = remember {
        val splitedDate = date.split('-')
        if(splitedDate.size>2){
            mutableStateOf(splitedDate[2].toInt())
        }else{
            mutableStateOf(mCalendar.get(Calendar.DAY_OF_MONTH))
        }
    }

    val chosenDate = remember { mutableStateOf(date) }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            chosenDate.value =
                "$mYear-${if (mMonth + 1 >= 10) mMonth + 1 else "0" + (mMonth + 1)+"-"+ if(mDayOfMonth<10) "0"+mDayOfMonth else mDayOfMonth}"
                onDateChosen(chosenDate.value)
        }, mYear.value, mMonth.value, mDay.value
    )
    LaunchedEffect(isShown){
       if(isShown){
           mDatePickerDialog.show()
       }
    }
}