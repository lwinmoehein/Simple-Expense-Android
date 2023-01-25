package lab.justonebyte.moneysubuu.model

import lab.justonebyte.moneysubuu.R
import lab.justonebyte.moneysubuu.ui.settings.SettingItem


sealed class AppLocale(override val value:String, override val name:Int): SettingItem {
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