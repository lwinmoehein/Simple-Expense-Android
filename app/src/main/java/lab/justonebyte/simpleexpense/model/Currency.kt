package lab.justonebyte.simpleexpense.model

import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.ui.components.OptionItem

sealed class Currency(override val value:Int, override val name:Int): OptionItem {
    object Kyat: Currency(1, R.string.kyat)
    object Dollar: Currency(2, R.string.dollar)

    companion object {
        fun getFromValue(value:Int): Currency {
            return when (value){
                Kyat.value -> Kyat
                else -> Dollar
            }
        }
    }

}