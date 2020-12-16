package im.tony.google.services

import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.Spreadsheet
import im.tony.Const
import im.tony.google.extensions.sheets.SheetsMajorDimension
import im.tony.google.extensions.sheets.setMajorDimension

interface GoogleSheetsService {
  //public val mainSheet: Spreadsheet
  val inspectionData: List<List<Any>>
  val topsData: List<List<Any>>

  fun loadEntireSpreadsheet(id: String): Spreadsheet
  fun loadSpreadsheetMetadata(id: String): Spreadsheet
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
        .setMajorDimension(SheetsMajorDimension.Rows)
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
        .setMajorDimension(SheetsMajorDimension.Rows)
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

val SheetsService: GoogleSheetsService = SheetsServiceImpl
