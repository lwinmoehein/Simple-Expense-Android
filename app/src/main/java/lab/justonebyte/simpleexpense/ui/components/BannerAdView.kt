package lab.justonebyte.simpleexpense.ui.components

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import lab.justonebyte.simpleexpense.R

@SuppressLint("VisibleForTests")
@Composable
fun BannerAdView(
    isTest: Boolean = true
) {
    val unitId = "ca-app-pub-3940256099942544/6300978111"

    AndroidView(
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.LARGE_BANNER)
                adUnitId = unitId
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}