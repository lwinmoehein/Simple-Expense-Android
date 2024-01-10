package lab.justonebyte.simpleexpense.ui.account

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.widget.DatePicker
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import compose.icons.FeatherIcons
import compose.icons.feathericons.File
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.ui.components.SectionTitle
import lab.justonebyte.simpleexpense.utils.dateFormatter
import java.util.Calendar
import java.util.Date


data class FileFormat(val iconId:Int,val nameId:Int)

val excel = FileFormat(1,R.string.excel_format)
val pdf = FileFormat(1,R.string.pdf_format)

val formats = listOf(
    excel,
    pdf
)

@Composable
fun ChooseFormat(
    onFormatChosen:(format:FileFormat)->Unit
){
    var chosenFormat by remember { mutableStateOf<FileFormat?>(formats[0]) }

    Column() {
        SectionTitle(title = stringResource(id = R.string.export_file_format))
        LazyVerticalGrid(
            modifier = Modifier
                .heightIn(max = 300.dp)
                .fillMaxWidth(),
            userScrollEnabled = true,
            columns = GridCells.Fixed(3),

            content = {

                items(formats.size) { index ->
                    val isSelected = chosenFormat?.let { (it.nameId== formats[index].nameId) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .absolutePadding(right = 5.dp)
                            .border(
                                width = if (isSelected == true) 2.dp else 0.dp,
                                color = Color.Transparent
                            )
                            .clickable {
                                chosenFormat = formats[index]
                                onFormatChosen(chosenFormat!!)
                            }
                    ) {

                        Text(
                            text = stringResource(id = formats[index].nameId),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(10.dp),
                        )

                    }
                }
            }
        )
    }
}

@Composable
fun ChooseDownloadFolder(
    context:Context = LocalContext.current,
    chooseDownloadFolderLauncher: ActivityResultLauncher<Intent>,
    downloadFolder:String?
){

    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        SectionTitle(title = stringResource(id = R.string.export_path))
        if(!downloadFolder.isNullOrEmpty())
            Text(
                text = downloadFolder,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    val intent = chooseDownloadFolderIntent()
                    chooseDownloadFolderLauncher.launch(intent)
                }
            )
        else
            Text(
                text = stringResource(id = R.string.export_folder_warning),
                color = Color.Red,
                modifier = Modifier.clickable {
                    val intent = chooseDownloadFolderIntent()
                    chooseDownloadFolderLauncher.launch(intent)
                }
            )
    }
}

fun chooseDownloadFolderIntent():Intent{
    val uri = DocumentsContract.buildDocumentUri("com.android.externalstorage.documents", "primary:SimpleExpense") as Uri

    // Choose a directory using the system's file picker.
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when it loads.
        putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
    }
    return intent
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ExportScreen(
    navController: NavController,
    chooseDownloadFolderLauncher: ActivityResultLauncher<Intent>
) {

    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    val uiState by settingsViewModel.viewModelUiState.collectAsState()
    var chosenFormat by remember { mutableStateOf<FileFormat?>(formats[0]) }
    val toDate = remember { mutableStateOf(dateFormatter(System.currentTimeMillis())) }
    val fromDate = remember { mutableStateOf(dateFormatter(System.currentTimeMillis())) }

    Scaffold {
        Column(
            modifier = Modifier.padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            ChooseDateRange(
                fromDate = fromDate.value,
                toDate = toDate.value,
                onFromDateChosen = {
                    fromDate.value = it
                },
                onToDateChosen = {
                    toDate.value = it
                }
            )
            ChooseFormat(onFormatChosen = {
                chosenFormat = it
            })

            ChooseDownloadFolder(
                chooseDownloadFolderLauncher = chooseDownloadFolderLauncher,
                downloadFolder = uiState.readableDownloadFolder
            )

            Spacer(modifier = Modifier.absolutePadding(top = 20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(left = 50.dp, right = 50.dp),
            ) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if(!uiState.isExportingFile){
                            chosenFormat?.let {
                                settingsViewModel.exportDate(fromDate.value,toDate.value, it)
                            }
                        }
                    }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier =  Modifier.fillMaxWidth()// Center content within the box
                        // Expand the box to fill available space
                    ) {
                        if (uiState.isExportingFile) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .width(15.dp)
                                    .height(15.dp)
                                    .align(Alignment.Center),  // Explicitly center the indicator
                                color = Color.White,
                                strokeWidth = 3.dp
                            )
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.export),
                                )
                                Icon(modifier = Modifier
                                    .absolutePadding(left = 5.dp)
                                    .width(15.dp),imageVector = FeatherIcons.File, contentDescription = "",tint= MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun ChooseDateRange(
    fromDate: String,
    toDate:String,
    onFromDateChosen:(fromDate:String)->Unit,
    onToDateChosen:(toDate:String)->Unit
) {
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()

    mCalendar.time = Date()
    val mYear: Int = mCalendar.get(Calendar.YEAR)
    val mMonth: Int = mCalendar.get(Calendar.MONTH)
    val mDay: Int = mCalendar.get(Calendar.DAY_OF_MONTH)
    val toDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            val selectedDate = "$mYear-${if (mMonth + 1 >= 10) (mMonth + 1) else ("0" +(mMonth + 1))}-${if (mDayOfMonth + 1 >= 10) mDayOfMonth else ("0$mDayOfMonth")}"
            onToDateChosen(selectedDate)
        }, mYear, mMonth, mDay
    )

    toDatePickerDialog.datePicker.maxDate = Date().time

    val fromDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            val selectedDate = "$mYear-${if (mMonth + 1 >= 10) (mMonth + 1) else ("0" +(mMonth + 1))}-${if (mDayOfMonth + 1 >= 10) mDayOfMonth else ("0$mDayOfMonth")}"
            onFromDateChosen(selectedDate)
        }, mYear, mMonth, mDay
    )

    SectionTitle(title = stringResource(id = R.string.select_date_range))
    Card(
        colors =  CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary,
        )
    ) {
        Column(Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.from))
                Text(
                    text = fromDate,
                    modifier = Modifier.clickable { fromDatePickerDialog.show() })
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.to))
                Text(
                    text = toDate,
                    modifier = Modifier.clickable { toDatePickerDialog.show() })
            }
        }
    }
}


