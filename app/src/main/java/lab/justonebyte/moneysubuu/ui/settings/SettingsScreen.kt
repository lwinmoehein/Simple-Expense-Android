package lab.justonebyte.moneysubuu.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.ui.appContentPadding


@Composable
fun SettingsScreen(
    openDrawer:()->Unit,
){
   Column (
       modifier = Modifier.padding(appContentPadding)
           ){
       Text(text = "Settings")
   }
}