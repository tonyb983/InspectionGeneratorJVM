package im.tony.google.services

import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.Spreadsheet
import im.tony.Const

public interface GoogleSheetsService {
  //public val mainSheet: Spreadsheet
  public val inspectionData: List<List<Any>>
  public val topsData: List<List<Any>>

  public fun loadEntireSpreadsheet(id: String): Spreadsheet
  public fun loadSpreadsheetMetadata(id: String): Spreadsheet
}

private val SheetsServiceImpl =
  object :
    GoogleSheetsService,
    GenericService<Sheets>(lazy { ServiceCreator.createSheets() }) {

    //override val mainSheet: Spreadsheet by lazy { loadEntireSpreadsheet(Const.InputDataSheetId) }

    override val inspectionData: List<List<Any>> by lazy {
      service
        .Spreadsheets()
        .Values()
        .get(Const.InputDataSheetId, Const.NamedRanges.InspectionData)
        .setMajorDimension("ROWS")
        .execute()
        .let {
          val values = it.getValues()
          require(values.size > 0) { "InspectionData returned less than 1 row." }
          values.removeFirst()
          return@let values
        }
    }

    override val topsData: List<List<Any>> by lazy {
      service
        .Spreadsheets()
        .Values()
        .get(Const.InputDataSheetId, Const.NamedRanges.TopsData)
        .setMajorDimension("ROWS")
        .execute()
        .let {
          val values = it.getValues()
          require(values.size > 0) { "OwnerData returned less than 1 row." }
          values.removeFirst()
          return@let values
        }
    }

    override fun loadEntireSpreadsheet(id: String): Spreadsheet = service.spreadsheets().get(id).setIncludeGridData(true).execute()

    override fun loadSpreadsheetMetadata(id: String): Spreadsheet = service.spreadsheets().get(id).setIncludeGridData(false).execute()
  }

public val SheetsService: GoogleSheetsService = SheetsServiceImpl
