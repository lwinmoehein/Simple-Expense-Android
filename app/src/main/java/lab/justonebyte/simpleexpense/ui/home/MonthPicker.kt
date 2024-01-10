package lab.justonebyte.simpleexpense.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import java.util.*
import lab.justonebyte.simpleexpense.R


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
        Column(
            Modifier.padding(20.dp)
        ) {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier
                .fillMaxWidth()) {
                NumberPicker(
                    modifier = Modifier.weight(1f),
                    value = selectedYear,
                    range = 2020..Calendar.getInstance().get(Calendar.YEAR),
                    onValueChange = {
                        onYearSelected(it)
                    },
                    dividersColor = MaterialTheme.colorScheme.secondary,
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.primary)

                )
                if(isMonthPicker){
                    NumberPicker(
                        modifier = Modifier.weight(1f),
                        value = selectedMonth,
                        range = 1..12,
                        onValueChange = {
                            onMonthSelected(it)
                        },
                        dividersColor = MaterialTheme.colorScheme.secondary,
                        textStyle = TextStyle(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { onConfirmPicker() }) {
                    Text(text = stringResource(id = R.string.confirm), color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}
