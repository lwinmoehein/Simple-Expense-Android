package lab.justonebyte.simpleexpense.model

import lab.justonebyte.simpleexpense.R

class OnBoardingItem(
    val image: Int,
    val title: Int,
    val desc: Int
) {
    companion object{
        fun getData(): List<OnBoardingItem>{
            return listOf(
                OnBoardingItem(R.drawable.record, R.string.confirm, R.string.showcase_settings),
                OnBoardingItem(R.drawable.cloud, R.string.cancel, R.string.showcase_add_transaction),
                OnBoardingItem(R.drawable.exports, R.string.confirm, R.string.showcase_balance_card)
            )
        }
    }
}