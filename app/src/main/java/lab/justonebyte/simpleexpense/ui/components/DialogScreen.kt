package lab.justonebyte.simpleexpense.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun DialogScreen() {
    val openDefaultDialog = remember { mutableStateOf(false) }

    val openCustomDialog = remember { mutableStateOf(false) }

    DialogScreenSkeleton(
        showDefaultDialog = {
            openDefaultDialog.value = true
        },
        showCustomDialog = {
            openCustomDialog.value = true
        }
    )

    if (openDefaultDialog.value) {
        DefaultAlertDialog(
            state = openDefaultDialog
        )
    }

    if (openCustomDialog.value) {
        GeneralDialog(
            dialogState = openCustomDialog,
            title = "Are you sure?",
            message = "This cannot be undone.",
            positiveBtnText = "Yes",
            onPositiveBtnClicked = {},
            negativeBtnText = "No",
            onNegativeBtnClicked = {}
        )
    }
}



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DialogScreenSkeleton(
    showDefaultDialog: () -> Unit = {},
    showCustomDialog: () -> Unit = {},
) {
    Scaffold(
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            AppComponent.Header("Dialog")

            // ----------------------------------------------------------------
            // ----------------------------------------------------------------

            Divider()

            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 32.dp),
                onClick = {
                    showDefaultDialog()
                }
            ) {
                Text("Show Default Dialog")
            }

            // ----------------------------------------------------------------

            AppComponent.MediumSpacer()

            Button(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    showCustomDialog()
                }
            ) {
                Text("Show Custom Dialog")
            }

            // ----------------------------------------------------------------
            // ----------------------------------------------------------------

            AppComponent.BigSpacer()
        }
    }
}

@Composable
fun DefaultAlertDialog(
    state: MutableState<Boolean>
) {
    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.
            state.value = false
        },
        title = {
            Text(text = "Title")
        },
        text = {
            Text(
                "This area typically contains the supportive text " +
                        "which presents the details regarding the Dialog's purpose."
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    state.value = false
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    state.value = false
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}