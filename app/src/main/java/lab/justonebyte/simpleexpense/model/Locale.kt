package lab.justonebyte.simpleexpense.model

import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.ui.components.OptionItem


sealed class AppLocale(override val value:String, override val name:Int): OptionItem {
    object Myanmar: AppLocale("my", R.string.my)
    object English: AppLocale("en", R.string.en)

    companion object {
        fun getFromValue(value:String): AppLocale {
            return when (value){
                Myanmar.value -> Myanmar
                else -> English
            }
        }
    }

}