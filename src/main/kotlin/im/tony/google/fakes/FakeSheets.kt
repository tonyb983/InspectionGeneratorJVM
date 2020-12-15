package im.tony.google.fakes

import com.google.api.services.sheets.v4.model.*

public class FakeSheet(private val data: MutableMap<String, Any?> = mutableMapOf()) : MutableMap<String, Any?> by data

public class FakeSpreadsheet {
  public val spreadsheet: Spreadsheet = Spreadsheet()
  public val ssProps: SpreadsheetProperties = SpreadsheetProperties()
  public val sheet: Sheet = Sheet()
  public val sProps: SheetProperties = SheetProperties()
  public val gridData: GridData = GridData()
  public val dimensionGroup: DimensionGroup = DimensionGroup()

  private fun tester() {
    val firstRow = sheet.data.first().rowData.first().getValues()
    firstRow.forEach { it.formattedValue }
  }
}
