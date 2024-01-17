package lab.justonebyte.simpleexpense.ui.onboarding

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.OnBoardingItem

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoarding() {
    val items = OnBoardingItem.getData()
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState()

    Column(modifier = Modifier.fillMaxSize()) {
       Column(
           modifier = Modifier.fillMaxSize(),
       ) {
           HorizontalPager(
               count = items.size,
               state = pageState,
               modifier = Modifier
                   .weight(5f)
                   .fillMaxWidth()
           ) { page ->
               Column(
                   modifier = Modifier.fillMaxWidth(),
                   verticalArrangement = Arrangement.Center,
                   horizontalAlignment = Alignment.CenterHorizontally
               ) {
                   OnBoardingItem(item = items[page])
               }
           }
           BottomSection(
               modifier = Modifier.weight(1f),
               size = items.size, index = pageState.currentPage
           ) {
               if (pageState.currentPage + 1 < items.size) scope.launch {
                   pageState.scrollToPage(pageState.currentPage + 1)
               }
           }
       }

    }
}

@ExperimentalPagerApi
@Composable
fun TopSection(onBackClick: () -> Unit = {}, onSkipClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        // Back button
        IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(imageVector = Icons.Outlined.KeyboardArrowLeft, contentDescription = null)
        }

        // Skip Button
        TextButton(
            onClick = onSkipClick,
            modifier = Modifier.align(Alignment.CenterEnd),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = "Skip", color = Color.White)
        }
    }
}

@Composable
fun BottomSection(modifier: Modifier,size: Int, index: Int, onButtonClick: () -> Unit = {}) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Indicators
        Indicators(size, index)
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedButton(
            onClick = { /* do something */ },
        ) {
            Row(
                Modifier.padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "START", fontWeight = FontWeight.ExtraBold)
                Icon(
                    Icons.Outlined.KeyboardArrowRight,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Localized description"
                )
            }
        }
    }
}

@Composable
fun Indicators(size: Int, index: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        repeat(size) {
            Indicator(isSelected = it == index)
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(
        targetValue = if (isSelected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
    ) {

    }
}

@Composable
fun OnBoardingItem(item: OnBoardingItem) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = item.image),
                contentDescription = "Image1",
                modifier = Modifier.width(if(item.image == R.drawable.exports) 300.dp else 300.dp).height(if(item.image == R.drawable.exports) 300.dp else 300.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = stringResource(id = item.title),
                style = MaterialTheme.typography.headlineLarge,
                // fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                letterSpacing = 1.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(id = item.desc),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp),
                letterSpacing = 1.sp,
                color = Color.Black
            )
        }
}