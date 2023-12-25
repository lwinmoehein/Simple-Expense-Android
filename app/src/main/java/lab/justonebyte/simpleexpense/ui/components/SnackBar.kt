package lab.justonebyte.simpleexpense.ui.components

import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
import lab.justonebyte.simpleexpense.R

enum class SnackBarType{
    INCORRECT_DATA,
    INCORRECT_CATEGORY_DATA,
    ADD_CATEGORY_SUCCESS,
    UPDATE_CATEGORY_SUCCESS,
    REMOVE_CATEGORY_SUCCESS,
    LANG_CHANGE_SUCCESS
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
            SnackBarType.INCORRECT_CATEGORY_DATA-> stringResource(id = R.string.incorrect_category_data)
            SnackBarType.ADD_CATEGORY_SUCCESS-> stringResource(id = R.string.add_category_success)
            SnackBarType.UPDATE_CATEGORY_SUCCESS-> stringResource(id = R.string.update_category_success)
            SnackBarType.REMOVE_CATEGORY_SUCCESS-> stringResource(id = R.string.remove_category_success)
            SnackBarType.LANG_CHANGE_SUCCESS-> stringResource(id = R.string.lang_change_success)
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