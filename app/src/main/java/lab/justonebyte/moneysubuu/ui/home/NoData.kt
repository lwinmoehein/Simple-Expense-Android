package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import lab.justonebyte.moneysubuu.R

@Composable
fun NoData(modifier: Modifier,text:String= stringResource(id = R.string.no_data)){
    Column(modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text =text)
    }
}