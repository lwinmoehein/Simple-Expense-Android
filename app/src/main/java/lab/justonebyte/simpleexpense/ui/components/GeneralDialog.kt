package lab.justonebyte.simpleexpense.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@Composable
fun GeneralDialog(
    onDismissRequest: (() -> Unit)? = null,
    properties: DialogProperties = DialogProperties(),
    dialogState: MutableState<Boolean>,
    title: String,
    message: String? = null,
    positiveBtnText: String,
    onPositiveBtnClicked: () -> Unit = {},
    negativeBtnText: String? = null,
    onNegativeBtnClicked: (() -> Unit)? = null,
) {
    Dialog(
        onDismissRequest = {
            dialogState.value = false

            onDismissRequest?.invoke()
        },
        properties = properties,
    ) {
        GeneralDialogSkeleton(
            title = title,
            message = message,
            positiveBtnText = positiveBtnText,
            onPositiveBtnClicked = {
                dialogState.value = false

                onPositiveBtnClicked()
            },
            negativeBtnText = negativeBtnText,
            onNegativeBtnClicked = {
                dialogState.value = false

                onNegativeBtnClicked?.invoke()
            },
        )
    }
}



@Composable
fun GeneralDialogSkeleton(
    title: String,
    message: String? = null,
    positiveBtnText: String,
    onPositiveBtnClicked: () -> Unit = {},
    negativeBtnText: String? = null,
    onNegativeBtnClicked: (() -> Unit)? = null,
) {
    Box(Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    text = title,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )

                if (message != null) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                        text = message,
                        fontSize = 15.sp,
                        color = Color(0xFF677987),
                        textAlign = TextAlign.Center,
                        lineHeight = 23.sp
                    )
                }

                Row {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (negativeBtnText != null) {
                            OutlinedButton(
                                modifier = Modifier
                                    .weight(.5f),
                                onClick = {
                                    onNegativeBtnClicked?.invoke()
                                }
                            ) {
                                Text(negativeBtnText)
                            }

                            Spacer(modifier = Modifier.width(16.dp))
                        }

                        Button(
                            modifier = Modifier
                                .weight(.5f),
                            onClick = {
                                onPositiveBtnClicked()
                            }
                        ) {
                            Text(positiveBtnText)
                        }
                    }
                }
            }
        }
    }
}

