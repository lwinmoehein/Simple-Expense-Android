package lab.justonebyte.simpleexpense.ui.account

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.DocumentsContract
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.utils.dateFormatter
import java.util.*



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
    
    Text(text = stringResource(id = R.string.choose_format))
    LazyVerticalGrid(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.heightIn(max=300.dp),
        userScrollEnabled = true,
        columns = GridCells.Fixed(3),
        // content padding
        contentPadding = PaddingValues(
            top = 5.dp,
            bottom = 30.dp
        ),
        content = {

            items(formats.size) { index ->
                val isSelected = chosenFormat?.let { (it.nameId== formats[index].nameId) }

                Card(
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth()
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExportScreen(
    settingsViewModel: SettingsViewModel,
    onExportClicked:(from:String,to:String,format:FileFormat)->Unit,
    context:Context = LocalContext.current,
) {
    val mContext = LocalContext.current
    val contentResolver = LocalContext.current.contentResolver
    val downloadedFile = settingsViewModel.downloadedFile.collectAsState()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/plain")) { selectedUri ->
        if (selectedUri != null) {
            contentResolver.openOutputStream(selectedUri)?.use {
                it.write(downloadedFile.value?.bytes())
            }
        } else {
            println("No file was selected")
        }
    }

    if(downloadedFile.value!==null){
        launcher.launch("test.txt")
    }


    val mCalendar = Calendar.getInstance()
    var chosenFormat by remember { mutableStateOf<FileFormat?>(null) }


    mCalendar.time = Date()
    val mYear: Int = mCalendar.get(Calendar.YEAR)
    val mMonth: Int = mCalendar.get(Calendar.MONTH)
    val mDay: Int = mCalendar.get(Calendar.DAY_OF_MONTH)
    val toDate = remember { mutableStateOf(dateFormatter(System.currentTimeMillis())) }
    val toDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            var selectedDate = "$mYear-${if (mMonth + 1 >= 10) (mMonth + 1) else ("0" +(mMonth + 1))}-${if (mDayOfMonth + 1 >= 10) mDayOfMonth else ("0$mDayOfMonth")}"
            toDate.value = selectedDate
        }, mYear, mMonth, mDay
    )

    toDatePickerDialog.datePicker.maxDate = Date().time

    val fromDate = remember { mutableStateOf(dateFormatter(System.currentTimeMillis())) }
    val fromDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            var selectedDate = "$mYear-${if (mMonth + 1 >= 10) (mMonth + 1) else ("0" +(mMonth + 1))}-${if (mDayOfMonth + 1 >= 10) mDayOfMonth else ("0$mDayOfMonth")}"
            fromDate.value = selectedDate
        }, mYear, mMonth, mDay
    )

    toDatePickerDialog.datePicker.maxDate = Date().time

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // First row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "From")
            Text(text = fromDate.value,modifier = Modifier.clickable { fromDatePickerDialog.show() })
        }

        // Second row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "To")
            Text(text = toDate.value, modifier = Modifier.clickable { toDatePickerDialog.show() })
        }
        ChooseFormat(onFormatChosen = {
            chosenFormat = it
        })

        // Third row
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
