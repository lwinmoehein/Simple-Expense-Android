package lab.justonebyte.simpleexpense.ui.detail


import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import lab.justonebyte.simpleexpense.R
import lab.justonebyte.simpleexpense.model.Transaction
import lab.justonebyte.simpleexpense.model.TransactionType
import lab.justonebyte.simpleexpense.ui.components.FormattedCurrency
import lab.justonebyte.simpleexpense.ui.components.getValidCurrencyCode
import lab.justonebyte.simpleexpense.ui.home.NoData
import lab.justonebyte.simpleexpense.ui.theme.bar1
import lab.justonebyte.simpleexpense.ui.theme.bar10
import lab.justonebyte.simpleexpense.ui.theme.bar11
import lab.justonebyte.simpleexpense.ui.theme.bar12
import lab.justonebyte.simpleexpense.ui.theme.bar13
import lab.justonebyte.simpleexpense.ui.theme.bar14
import lab.justonebyte.simpleexpense.ui.theme.bar15
import lab.justonebyte.simpleexpense.ui.theme.bar16
import lab.justonebyte.simpleexpense.ui.theme.bar17
import lab.justonebyte.simpleexpense.ui.theme.bar18
import lab.justonebyte.simpleexpense.ui.theme.bar19
import lab.justonebyte.simpleexpense.ui.theme.bar2
import lab.justonebyte.simpleexpense.ui.theme.bar20
import lab.justonebyte.simpleexpense.ui.theme.bar3
import lab.justonebyte.simpleexpense.ui.theme.bar4
import lab.justonebyte.simpleexpense.ui.theme.bar5
import lab.justonebyte.simpleexpense.ui.theme.bar6
import lab.justonebyte.simpleexpense.ui.theme.bar7
import lab.justonebyte.simpleexpense.ui.theme.bar8
import lab.justonebyte.simpleexpense.ui.theme.bar9
import java.text.DecimalFormat
import java.util.Currency

class MyValueFormatter : ValueFormatter() {
    private val format = DecimalFormat("###,##0.0")


    override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
        return format.format(value)+"%"
    }
}


val colors = listOf(
    bar1,bar2,bar3,bar4,bar5,bar6,bar7,bar8,bar9,bar10,bar11,bar12,bar13,bar14,bar15,bar16,bar17,bar18,bar19,bar20
)
fun calculatePercentage(part: Int, whole: Int): Double {
    return (part.toDouble() / whole.toDouble()) * 100
}

@Composable
fun CustomPieChartWithData(
    modifier: Modifier=Modifier,
    currency: Currency,
    transactions:List<Transaction>,
    transactionType: TransactionType
){
    val materialColor =  MaterialTheme.colorScheme.primary.toArgb()
    val totalAmount = transactions.sumOf { it.amount }
    val pieEntries = transactions.groupBy { it.category }.map { PieEntry(calculatePercentage(it.value.sumOf { s-> s.amount },totalAmount).toFloat(),it.key.name) }.sortedByDescending { it.value }

    if(pieEntries.isNotEmpty()){
        Column(
            modifier = modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start) {
                Row(modifier= Modifier
                    .width(260.dp)
                    .height(260.dp)
                   ){
                    Crossfade(targetState = pieEntries, label = "") { pieEntries ->
                        AndroidView(factory = { context ->
                          PieChart(context).apply {
                                layoutParams = LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                )

                                this.minAngleForSlices = 30f
                                this.description.isEnabled = false
                                this.isDrawHoleEnabled = true
                                this.setHoleColor(Color.Transparent.toArgb())

                                this.legend.isEnabled = true
                                this.legend.isWordWrapEnabled = true
                                this.legend.textSize = 13F
                                this.legend.textColor = materialColor
                                this.legend.horizontalAlignment =
                                    Legend.LegendHorizontalAlignment.CENTER
                                
                                this.setEntryLabelColor(Color.Transparent.toArgb())
                            }
                        },
                            modifier = Modifier
                                .padding(5.dp)
                                , update = {
                                    updatePieChartWithData(it,pieEntries)
                            })
                    }
                }
                Column(verticalArrangement = Arrangement.Center, modifier = Modifier
                    .weight(1f)
                    .absolutePadding(top = 30.dp, left = 5.dp)) {
                    Text(
                        text = stringResource(R.string.total),
                        style = MaterialTheme.typography.titleLarge
                    )
                    FormattedCurrency(
                        amount = totalAmount.toLong(),
                        color =if(transactionType==TransactionType.Income) MaterialTheme.colorScheme.primary  else Color.Red,
                        currencyCode = getValidCurrencyCode(currency)
                    )
                }
            }
        }
    }else{
        NoData(modifier = Modifier.fillMaxSize())
    }
}
fun updatePieChartWithData(
    chart: PieChart,
    entries: List<PieEntry>
) {
    val ds = PieDataSet(entries,"")


    // on below line we are specifying position for value
    ds.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

    // on below line we are specifying position for value inside the slice.
    ds.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE


    ds.sliceSpace = 1f

    ds.colors =  colors.map { it.toArgb() }

    ds.valueTextSize = 12f


    ds.valueTypeface = Typeface.DEFAULT_BOLD

    val d = PieData(ds)

    d.setValueFormatter(MyValueFormatter())
    d.setValueTextColor(Color.White.toArgb())

    chart.data = d


    chart.invalidate()
}