package im.tony.google.services

import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.Spreadsheet
import im.tony.Const

public interface GoogleSheetsService {
  public val mainSheet: Spreadsheet
  public val inspectionData: List<List<Any>>
  public val topsData: List<List<Any>>
}

private val SheetsServiceImpl =
  object :
    GoogleSheetsService,
    GenericService<Sheets>(lazy { ServiceCreator.createSheets() }) {
    private fun loadDataSheet(): Spreadsheet = service.spreadsheets().get(Const.InputDataSheetId).setIncludeGridData(true).execute()

    override val mainSheet: Spreadsheet by lazy { loadDataSheet() }

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
  }

public val SheetsService: GoogleSheetsService = SheetsServiceImpl
