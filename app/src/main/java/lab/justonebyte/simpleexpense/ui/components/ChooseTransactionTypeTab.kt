package lab.justonebyte.simpleexpense.ui.components

import android.annotation.SuppressLint
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.BalanceType

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
        homeTabs.forEachIndexed { index, tab ->
            Tab(
                selected = currentHomeTabIndex == index,
                onClick = {
                    currentHomeTabIndex = index
                    currentHomeTabIndex = index
                    when (currentHomeTabIndex) {
                        0 -> currentBalanceType.value = BalanceType.MONTHLY
                        1 -> currentBalanceType.value = BalanceType.YEARLY
                        else -> currentBalanceType.value = BalanceType.TOTAL
                    }
                    onTypeChanged(currentBalanceType.value)
                },
                text = { Text(text = stringResource(id = tab.name), maxLines = 2, overflow = TextOverflow.Ellipsis) }
            )
        }
    }

}