package lab.justonebyte.simpleexpense.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.data.SettingPrefRepository
import lab.justonebyte.simpleexpense.ui.account.SettingsViewModel
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingRepository: SettingPrefRepository


    val settingViewModel: SettingsViewModel by viewModels()

    private var isOnboardingShowed = true

    private var activity: Activity? = null


    private val chooseDownloadFolderLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                val contentResolver = applicationContext.contentResolver
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                contentResolver.takePersistableUriPermission(uri, takeFlags)

                if (uri.toString().isNotEmpty()) {
                    CoroutineScope(Dispatchers.Main).launch {
                        settingRepository.updateDownloadFolder(uri.toString())
                    }
                }

            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity = this

        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            SimpleExpenseApp(
                chooseDownloadFolderLauncher = chooseDownloadFolderLauncher,
                settingsViewModel = settingViewModel
            )
        }
        MobileAds.initialize(this) {}

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingViewModel.rewardedAd.collect {
                    it?.let { ad ->
                        ad.show(activity as MainActivity, OnUserEarnedRewardListener { rewardItem ->
                            // Handle the reward.
                            val rewardAmount = rewardItem.amount
                            val rewardType = rewardItem.type
                            Log.d("Ad", "User earned the reward.")
                            settingViewModel.changeIsAdLoadingToFalse()
                        })
                    } ?: run {
                        Log.d("Ad", "The rewarded ad wasn't ready yet.")
                        settingViewModel.changeIsAdLoadingToFalse()
                    }
                }
            }
        }
    }
}


