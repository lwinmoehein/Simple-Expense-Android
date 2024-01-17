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
                OnBoardingItem(R.drawable.record, R.string.onboarding_record_expense, R.string.onboarding_record_expense_desc),
                OnBoardingItem(R.drawable.cloud, R.string.onboarding_cloud_login, R.string.onboarding_cloud_login_desc),
                OnBoardingItem(R.drawable.exports, R.string.onboarding_export_file, R.string.onboarding_export_file_desc)
            )
        }
    }
}