package lab.justonebyte.simpleexpense.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import lab.justonebyte.simpleexpense.model.BalanceType
import lab.justonebyte.simpleexpense.ui.home.*
import java.util.*
import lab.justonebyte.simpleexpense.R

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

    Row(
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier.width(250.dp)
    ) {
        homeTabs.forEachIndexed { index, balanceType ->
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(if (currentHomeTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                    .padding(5.dp)
                    .weight(1f)
                    .clickable {
                        currentHomeTabIndex = index
                        when (currentHomeTabIndex) {
                            0 -> currentBalanceType.value = BalanceType.MONTHLY
                            1 -> currentBalanceType.value = BalanceType.YEARLY
                            else -> currentBalanceType.value = BalanceType.TOTAL
                        }
                        onTypeChanged(currentBalanceType.value)
                    },
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = balanceType.name),
                    color = if (currentHomeTabIndex == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }

}