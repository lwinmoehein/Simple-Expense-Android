package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import java.util.*

@Composable
fun MonthPicker(
    selectedMonth:Int,
    selectedYear:Int,
    onYearSelected:(year:Int)->Unit,
    onMonthSelected:(month:Int)->Unit,
    onConfirmPicker:()->Unit,
    isMonthPicker:Boolean = true
){
    Card() {
        Column() {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier
                .absolutePadding(left = 20.dp, right = 20.dp)
                .fillMaxWidth()) {
                NumberPicker(
                    modifier = Modifier.weight(1f),
                    value = selectedYear,
                    range = 2020..Calendar.getInstance().get(Calendar.YEAR),
                    onValueChange = {
                        onYearSelected(it)
                    },
                    dividersColor = MaterialTheme.colorScheme.primary
                )
                if(isMonthPicker){
                    NumberPicker(
                        modifier = Modifier.weight(1f),
                        value = selectedMonth,
                        range = 1..12,
                        onValueChange = {
                            onMonthSelected(it)
                        },
                        dividersColor = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = { onConfirmPicker() }) {
                    Text(text = "OK")
                }
            }
        }
    }
}
