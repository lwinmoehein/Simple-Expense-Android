package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
                    }
                )
                if(isMonthPicker){
                    NumberPicker(
                        modifier = Modifier.weight(1f),
                        value = selectedMonth,
                        range = 1..12,
                        onValueChange = {
                            onMonthSelected(it)
                        }
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = { onConfirmPicker() }) {
                    Text(text = "OK", style = MaterialTheme.typography.button)
                }
            }
        }
    }
}
