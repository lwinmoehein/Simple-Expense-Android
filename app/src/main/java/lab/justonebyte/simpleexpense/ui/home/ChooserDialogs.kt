package lab.justonebyte.simpleexpense.ui.home

import androidx.compose.runtime.Composable
import lab.justonebyte.simpleexpense.ui.components.AppAlertDialog

@Composable
fun ChooseTransactionTypeDialog(
    isOpen:Boolean = false,
    onAddIncome:()->Unit,
    onAddExpense:()->Unit
){
    if (isOpen) {
        AppAlertDialog(
            title = "What type of transaction?",
            positiveBtnText =null,
            negativeBtnText = null,
            content = {
                        AddTransactionAction(
                            onAddIncome = {
                               onAddIncome()
                            },
                            onAddExpense = {
                                onAddExpense()
                            }
                        )
            }
        )
    }
}
@Composable
fun ChooseTransactionActionDialog(
    isOpen:Boolean = false,
    onEdit:()->Unit,
    onDelete:()->Unit
){
    if (isOpen) {
        AppAlertDialog(
            title = null,
            positiveBtnText =null,
            negativeBtnText = null,
            content = {
                ChooseTransactionAction(
                    onDeleteClick = {
                        onDelete()
                    },
                    onEditClick = {
                        onEdit()
                    }
                )
            }
        )
    }
}