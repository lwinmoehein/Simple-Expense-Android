package lab.justonebyte.moneysubuu.ui.account

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.utils.dateFormatter
import java.util.*


@Composable
fun ExportScreen(){
    MyLayout()

}

@Composable
fun MyLayout() {
    val mContext = LocalContext.current
    val localFocusManage = LocalFocusManager.current

    val mCalendar = Calendar.getInstance()

    mCalendar.time = Date()
    val mYear: Int = mCalendar.get(Calendar.YEAR)
    val mMonth: Int = mCalendar.get(Calendar.MONTH)
    val mDay: Int = mCalendar.get(Calendar.DAY_OF_MONTH)
    val toDate = remember { mutableStateOf(dateFormatter(System.currentTimeMillis())) }
    val toDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            var selectedDate = "$mYear-${if (mMonth + 1 >= 10) (mMonth + 1) else ("0" +(mMonth + 1))}-${if (mDayOfMonth + 1 >= 10) mDayOfMonth else ("0$mDayOfMonth")}"
            toDate.value = selectedDate
        }, mYear, mMonth, mDay
    )

    toDatePickerDialog.datePicker.maxDate = Date().time

    val fromDate = remember { mutableStateOf(dateFormatter(System.currentTimeMillis())) }
    val fromDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            var selectedDate = "$mYear-${if (mMonth + 1 >= 10) (mMonth + 1) else ("0" +(mMonth + 1))}-${if (mDayOfMonth + 1 >= 10) mDayOfMonth else ("0$mDayOfMonth")}"
            fromDate.value = selectedDate
        }, mYear, mMonth, mDay
    )

    toDatePickerDialog.datePicker.maxDate = Date().time

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // First row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "From")
            Text(text = fromDate.value,modifier = Modifier.clickable { fromDatePickerDialog.show() })
        }

        // Second row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "To")
            Text(text = toDate.value, modifier = Modifier.clickable { toDatePickerDialog.show() })
        }

        // Third row
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.export))
        }
    }
}
