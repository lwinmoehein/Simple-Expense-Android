package lab.justonebyte.simpleexpense.model

import lab.justonebyte.simpleexpense.R

class OnBoardingItems(
    val image: Int,
    val title: Int,
    val desc: Int
) {
    companion object{
        fun getData(): List<OnBoardingItems>{
            return listOf(
                OnBoardingItems(R.drawable.record, R.string.confirm, R.string.showcase_settings),
                OnBoardingItems(R.drawable.cloud, R.string.cancel, R.string.showcase_add_transaction),
                OnBoardingItems(R.drawable.export, R.string.confirm, R.string.showcase_balance_card)
            )
        }
    }
}