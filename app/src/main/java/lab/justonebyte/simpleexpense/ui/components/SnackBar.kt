package lab.justonebyte.simpleexpense.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
import lab.justonebyte.simpleexpense.R

 enum  class SnackBarType{
    INCORRECT_DATA,
    INCORRECT_CATEGORY_DATA,
    ADD_CATEGORY_SUCCESS,
    UPDATE_CATEGORY_SUCCESS,
    REMOVE_CATEGORY_SUCCESS,
    LANG_CHANGE_SUCCESS,
    FILE_EXPORT_SUCCESS,
    FILE_EXPORT_FAILED,
    SELECT_CORRECT_DOWNLOAD_FOLDER
}
@Composable
fun SuBuuSnackBar(
    snackBarType: SnackBarType?=null,
    onDismissSnackBar:()->Unit,
    snackbarHostState: SnackbarHostState,
){
    if (snackBarType!=null) {

        val snackMessage: String = when (snackBarType) {
            SnackBarType.INCORRECT_DATA ->stringResource(id = R.string.incorrect_data)
            SnackBarType.INCORRECT_CATEGORY_DATA-> stringResource(id = R.string.incorrect_category_data)
            SnackBarType.ADD_CATEGORY_SUCCESS-> stringResource(id = R.string.add_category_success)
            SnackBarType.UPDATE_CATEGORY_SUCCESS-> stringResource(id = R.string.update_category_success)
            SnackBarType.REMOVE_CATEGORY_SUCCESS-> stringResource(id = R.string.remove_category_success)
            SnackBarType.LANG_CHANGE_SUCCESS-> stringResource(id = R.string.lang_change_success)
            SnackBarType.FILE_EXPORT_SUCCESS-> stringResource(id = R.string.file_export_success)
            SnackBarType.FILE_EXPORT_FAILED-> stringResource(id = R.string.file_export_failed)
            SnackBarType.SELECT_CORRECT_DOWNLOAD_FOLDER-> stringResource(id = R.string.select_correct_download_folder)

            else -> stringResource(id = R.string.incorrect_data)
        }


        val onActionDismissState by rememberUpdatedState(onDismissSnackBar)

        LaunchedEffect(snackMessage) {
            snackbarHostState.showSnackbar(
                message = snackMessage
            )
            onActionDismissState()
        }
    }
}