package lab.justonebyte.moneysubuu.ui.loan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import lab.justonebyte.moneysubuu.ui.appContentPadding

@Composable
fun LoanScreen(
){
    Column (
        modifier = Modifier.padding(appContentPadding)
    ){
        Text(text = "Loan")
    }
}