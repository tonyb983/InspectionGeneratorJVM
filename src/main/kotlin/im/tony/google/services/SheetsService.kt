package im.tony.google.services

import com.google.api.services.sheets.v4.Sheets

interface GoogleSheetsService {

}

private val SheetsServiceImpl =
  object :
    GoogleSheetsService,
    GenericService<Sheets>(lazy { ServiceCreator.createSheets() })
  {

}

val SheetsService: GoogleSheetsService = SheetsServiceImpl
