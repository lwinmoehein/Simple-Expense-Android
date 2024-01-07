package lab.justonebyte.simpleexpense.ui.account

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.widget.DatePicker
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.utils.dateFormatter
import java.util.Calendar
import java.util.Date


data class FileFormat(val iconId:Int,val nameId:Int)

val formats = listOf(FileFormat(1,R.string.excel_format),
    FileFormat(2,R.string.pdf_format),
    FileFormat(3,R.string.csv_format)
)

@Composable
fun ChooseFormat(
    onFormatChosen:(format:FileFormat)->Unit
){
    var chosenFormat by remember { mutableStateOf<FileFormat?>(null) }

    Column() {
        ExportScreenTitle(title = "Choose Export File Format : ")
        LazyVerticalGrid(
            modifier = Modifier
                .heightIn(max = 300.dp)
                .fillMaxWidth(),
            userScrollEnabled = true,
            columns = GridCells.Fixed(3),

            content = {

                items(formats.size) { index ->
                    val isSelected = chosenFormat?.let { (it.nameId== formats[index].nameId) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .absolutePadding(right = 5.dp)
                            .clickable {
                                chosenFormat = formats[index]
                                onFormatChosen(chosenFormat!!)
                            },
                        colors =  CardDefaults.cardColors(
                            containerColor = if(isSelected==true) MaterialTheme.colorScheme.primary else  MaterialTheme.colorScheme.onSecondary,
                        )
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
        ExportScreenTitle(title = "Export To :")
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
                text =  "Choose Download Folder :",
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


@Composable
fun ExportScreen(
    settingsViewModel: SettingsViewModel,
    onExportClicked:(from:String,to:String,format:FileFormat)->Unit,
    chooseDownloadFolderLauncher: ActivityResultLauncher<Intent>
) {

    var chosenFormat by remember { mutableStateOf<FileFormat?>(null) }
    val downloadFolder by settingsViewModel.viewModelUiState.collectAsState()
    val toDate = remember { mutableStateOf(dateFormatter(System.currentTimeMillis())) }
    val fromDate = remember { mutableStateOf(dateFormatter(System.currentTimeMillis())) }

        Column(
            modifier = Modifier.absolutePadding(top = 5.dp).padding(16.dp),
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
                downloadFolder = downloadFolder.downloadFolder
            )

            Spacer(modifier = Modifier.absolutePadding(top = 20.dp))

            Button(
                onClick = {
                    chosenFormat?.let { onExportClicked(fromDate.value,toDate.value, it) }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.export))
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

    ExportScreenTitle(title = "Select Export Date Range : ")
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
                Text(text = "From")
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
                Text(text = "To")
                Text(
                    text = toDate,
                    modifier = Modifier.clickable { toDatePickerDialog.show() })
            }
        }
    }
}

@Composable
fun ExportScreenTitle(title:String){
    Text(text = title, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(bottom = 5.dp))
}
