package lab.justonebyte.simpleexpense.ui.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.ui.components.AppAlertDialog

@Composable
fun ChooseCategoryActionDialog(
    isOpen:Boolean = false,
    onDeleteCategory:()->Unit,
    onEditCategory:()->Unit
){
    if (isOpen) {
        AppAlertDialog(
            title = "What type of transaction?",
            positiveBtnText =null,
            negativeBtnText = null,
            content = {
                ChooseTransactionAction(
                    onDeleteCategory = {
                        onDeleteCategory()
                    },
                    onEditCategory = {
                        onEditCategory()
                    }
                )
            }
        )
    }
}

@Composable
fun ChooseTransactionAction(
    onDeleteCategory: () -> Unit,
    onEditCategory: () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onDeleteCategory()
            }
        ) {
            Text(text =  stringResource(id = R.string.delete_cat))
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onEditCategory()
            }
        ) {
            Text(text = stringResource(id = R.string.edit_cat))
        }
    }

}