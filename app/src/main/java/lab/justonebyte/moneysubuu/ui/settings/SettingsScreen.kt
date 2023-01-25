package lab.justonebyte.moneysubuu.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import lab.justonebyte.moneysubuu.ui.appContentPadding
import lab.justonebyte.moneysubuu.ui.category.CategoryViewModel


@Composable
fun SettingsScreen(
    openDrawer:()->Unit,
){
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val settingsUiState by settingsViewModel.viewModelUiState.collectAsState()
    
   Column (
       modifier = Modifier.padding(appContentPadding)
           ){
       Text(text = stringResource(id =  settingsUiState.selectedCurrency.name))
       Text(text = settingsUiState.defaultBalanceType.name)
   }
}