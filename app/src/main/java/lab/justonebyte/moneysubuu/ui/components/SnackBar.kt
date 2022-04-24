package lab.justonebyte.moneysubuu.ui.components

import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
import lab.justonebyte.moneysubuu.R

enum class SnackBarType{
    INCORRECT_DATA
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SuBuuSnackBar(
    snackBarType: SnackBarType?=null,
    onDismissSnackBar:()->Unit,
    scaffoldState: BottomSheetScaffoldState,
){
    if (snackBarType!=null) {

        var snackMessage: String = when (snackBarType) {
            SnackBarType.INCORRECT_DATA ->stringResource(id = R.string.incorrect_data)
            else -> stringResource(id = R.string.incorrect_data)
        }


        val onActionDismissState by rememberUpdatedState(onDismissSnackBar)

        LaunchedEffect(snackMessage, scaffoldState) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = snackMessage
            )
            onActionDismissState()
        }
    }
}