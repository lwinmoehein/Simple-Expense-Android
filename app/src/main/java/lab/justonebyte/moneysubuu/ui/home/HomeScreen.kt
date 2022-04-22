package lab.justonebyte.moneysubuu.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lab.justonebyte.moneysubuu.ui.appContentPadding
import lab.justonebyte.moneysubuu.ui.theme.SuBuuShapes

@Composable
fun HomeScreen(
    openDrawer:()->Unit,
){
   Column (modifier = Modifier.padding(appContentPadding)){
       CurrentBalance()
   }
}
@Composable
fun CurrentBalance(
    modifier: Modifier =Modifier
){
   Card(
       shape = SuBuuShapes.small,
       modifier = modifier.fillMaxWidth().height(100.dp).padding(10.dp),
       backgroundColor = MaterialTheme.colors.primary
   ) {
       Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
       ) {
           Row() {
               Text(
                   text = "Current Balance : ",
                   style = MaterialTheme.typography.h5
               )
               Text(
                   text = "10000ks",
                   style = MaterialTheme.typography.h5

               )
           }
       }
   }
}