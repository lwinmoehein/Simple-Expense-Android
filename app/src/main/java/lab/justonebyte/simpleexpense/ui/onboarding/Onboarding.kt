package lab.justonebyte.simpleexpense.ui.onboarding

import android.content.Context
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowDown
import compose.icons.feathericons.ChevronDown
import kotlinx.coroutines.launch
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.AppLocale
import lab.justonebyte.simpleexpense.model.OnBoardingItem
import lab.justonebyte.simpleexpense.ui.components.AppAlertDialog
import lab.justonebyte.simpleexpense.ui.home.HomeViewModel
import lab.justonebyte.simpleexpense.utils.changeLocale

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(
    onStartClick: ()->Unit,
    context: Context = LocalContext.current
) {
    val items = OnBoardingItem.getData()
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState()

    val onBoardViewModel = hiltViewModel<OnBoardViewModel>()
    val onBoardUiState by onBoardViewModel.viewModelUiState.collectAsState()


    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.onboard_bg),
            contentDescription = null, // You can provide a description here
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
       Column(
           modifier = Modifier.fillMaxSize(),
       ) {
           TopSection(currentLocale = onBoardUiState.currentLocale,onChangeLocale = { locale->
               scope.launch {
                   onBoardViewModel.changeLocale(locale)
                   changeLocale(context,locale.value)
               }
           })
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
               size = items.size, index = pageState.currentPage,
               onButtonClick = {
                   scope.launch {
                      onStartClick()
                   }
               },
               currentLocale = onBoardUiState.currentLocale
           )
       }

    }
}

@ExperimentalPagerApi
@Composable
fun TopSection(
     currentLocale: AppLocale,
     onChangeLocale:(locale:AppLocale)->Unit
) {
    var isDialogShown by remember { mutableStateOf(false) }

    if(isDialogShown){
        AppAlertDialog(
            title = stringResource(id = R.string.select_language)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable{
                        onChangeLocale(AppLocale.Myanmar)
                        isDialogShown = false
                    }
                ) {
                    RadioButton(selected = currentLocale==AppLocale.Myanmar, onClick = null)
                    Text(text = stringResource(id = AppLocale.Myanmar.name))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                            onChangeLocale(AppLocale.English)
                            isDialogShown = false
                    }
                ){
                    RadioButton(selected = currentLocale==AppLocale.English, onClick = null)
                    Text(text = stringResource(id = AppLocale.English.name ))
                }
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 50.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Row (
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                isDialogShown = true
            }
        ){
            Text(text = stringResource(id = currentLocale.name), fontWeight = FontWeight.Bold)
            Icon(imageVector = FeatherIcons.ChevronDown, contentDescription ="" )
        }
    }
}

@Composable
fun BottomSection(
    currentLocale: AppLocale,
    modifier: Modifier,size: Int, index: Int, onButtonClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Indicators
        Indicators(size, index)

        Spacer(modifier = Modifier.height(20.dp))

        if(currentLocale==AppLocale.English){
            Button(
                onClick = {
                    onButtonClick()
                },
            ) {
                Row(
                    Modifier.padding(horizontal = 30.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(style = MaterialTheme.typography.labelLarge,text = stringResource(id = R.string.start), color = Color.White, fontWeight = FontWeight.ExtraBold)
                    Icon(
                        Icons.Outlined.KeyboardArrowRight,
                        tint = Color.White,
                        contentDescription = "Localized description"
                    )
                }
            }
        }else{
            Button(
                onClick = {
                    onButtonClick()
                },
            ) {
                Row(
                    Modifier.padding(horizontal = 30.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(style = MaterialTheme.typography.labelLarge,text = stringResource(id = R.string.start), color = Color.White, fontWeight = FontWeight.ExtraBold)
                    Icon(
                        Icons.Outlined.KeyboardArrowRight,
                        tint = Color.White,
                        contentDescription = "Localized description"
                    )
                }
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
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.secondary
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
                modifier = Modifier
                    .width(if (item.image == R.drawable.exports) 300.dp else 300.dp)
                    .height(if (item.image == R.drawable.exports) 300.dp else 300.dp)
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