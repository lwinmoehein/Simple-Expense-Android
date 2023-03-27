package lab.justonebyte.moneysubuu.ui.components

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import lab.justonebyte.moneysubuu.model.BalanceType
import lab.justonebyte.moneysubuu.ui.home.*
import lab.justonebyte.moneysubuu.ui.theme.SuBuuShapes
import lab.justonebyte.moneysubuu.utils.getCurrentMonth
import lab.justonebyte.moneysubuu.utils.getCurrentDate
import lab.justonebyte.moneysubuu.utils.getCurrentYear
import java.util.*
import lab.justonebyte.moneysubuu.R

sealed class BalanceTypeOption( override val name:Int,override val value:Any): OptionItem {
    object DAILY: BalanceTypeOption(R.string.daily, BalanceType.DAILY)
    object MONTHLY: BalanceTypeOption(R.string.monthly, BalanceType.MONTHLY)
    object YEARLY:BalanceTypeOption(R.string.yearly, BalanceType.YEARLY)
    object TOTAL:BalanceTypeOption(R.string.total, BalanceType.TOTAL)
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun ChooseTransactionTypeTab(
    balanceType: BalanceType,
    onTypeChanged: (type: BalanceType) -> Unit
){

    val currentBalanceType = mutableStateOf(balanceType)


    var currentHomeTabIndex: Int by remember { mutableStateOf(0) }
    val homeTabs = listOf<OptionItem>(BalanceType.MONTHLY,BalanceType.YEARLY,BalanceType.TOTAL)

    TabRow(selectedTabIndex = currentHomeTabIndex) {
        homeTabs.forEachIndexed { index, balanceType ->
            Tab(
                selected = currentHomeTabIndex == index,
                onClick = {
                    currentHomeTabIndex = index

                    when(currentHomeTabIndex){
                        0->currentBalanceType.value = BalanceType.MONTHLY
                        1->currentBalanceType.value = BalanceType.YEARLY
                        else->currentBalanceType.value = BalanceType.TOTAL
                    }
                    onTypeChanged(currentBalanceType.value)
                },
                text = { Text(text = stringResource(id = balanceType.name), maxLines = 2, overflow = TextOverflow.Ellipsis) }
            )
        }
    }

}