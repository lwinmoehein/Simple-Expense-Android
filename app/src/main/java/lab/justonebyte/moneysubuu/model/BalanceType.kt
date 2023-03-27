package lab.justonebyte.moneysubuu.model

import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.ui.components.OptionItem

sealed class BalanceType(override val value:Int, override val name:Int): OptionItem {
    object DAILY: BalanceType(1, R.string.daily)
    object MONTHLY: BalanceType(2, R.string.monthly)
    object YEARLY: BalanceType(3, R.string.yearly)
    object TOTAL: BalanceType(4, R.string.total)

    companion object {
        fun getFromValue(value:Int): BalanceType {
            return when (value){
                DAILY.value -> DAILY
                MONTHLY.value -> MONTHLY
                YEARLY.value->YEARLY
                else->TOTAL
            }
        }
    }

}
