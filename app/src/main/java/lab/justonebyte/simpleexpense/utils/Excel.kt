package lab.justonebyte.simpleexpense.utils

import lab.justonebyte.simpleexpense.model.Transaction
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellUtil.createCell
import org.apache.poi.xssf.usermodel.IndexedColorMap
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook

fun createWorkbook(sheetName:String,transactions:List<Transaction>): Workbook {
    // Creating excel workbook
    val workbook = XSSFWorkbook()

    //Creating first sheet inside workbook
    //Constants.SHEET_NAME is a string value of sheet name
    val sheet: Sheet = workbook.createSheet(sheetName)

    //Create Header Cell Style
    val cellStyle = getHeaderStyle(workbook)

    //Creating sheet header row
    createSheetHeader(cellStyle, sheet)

    //Adding data to the sheet
    transactions.forEachIndexed { index, transaction -> addData(index+1,sheet, transaction) }

    return workbook
}

 fun createSheetHeader(cellStyle: CellStyle, sheet: Sheet) {
    //setHeaderStyle is a custom function written below to add header style

    //Create sheet first row
    val row = sheet.createRow(0)

    //Header list
    val HEADER_LIST = listOf("Category", "Amount", "Note")

    //Loop to populate each column of header row
    for ((index, value) in HEADER_LIST.withIndex()) {

        val columnWidth = (15 * 500)

        //index represents the column number
        sheet.setColumnWidth(index, columnWidth)

        //Create cell
        val cell = row.createCell(index)

        //value represents the header value from HEADER_LIST
        cell?.setCellValue(value)

        //Apply style to cell
        cell.cellStyle = cellStyle
    }
}

fun getHeaderStyle(workbook: Workbook): CellStyle {

    //Cell style for header row
    val cellStyle: CellStyle = workbook.createCellStyle()

    //Apply cell color
    val colorMap: IndexedColorMap = (workbook as XSSFWorkbook).stylesSource.indexedColors
    var color = XSSFColor(IndexedColors.YELLOW, colorMap).indexed
    cellStyle.fillForegroundColor = color
    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)

    //Apply font style on cell text
    val whiteFont = workbook.createFont()
    color = XSSFColor(IndexedColors.WHITE, colorMap).indexed
    whiteFont.color = color
    whiteFont.bold = true
    cellStyle.setFont(whiteFont)


    return cellStyle
}

fun addData(rowIndex: Int, sheet: Sheet,transaction: Transaction) {

    //Create row based on row index
    val row = sheet.createRow(rowIndex)

    //Add data to each cell
    createCell(row, 0, transaction.category.name)
    createCell(row, 1, transaction.amount.toString())
    createCell(row, 2, if(transaction.note.isNullOrEmpty()) "-" else transaction.note)
}