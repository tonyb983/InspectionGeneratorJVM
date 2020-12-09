package im.tony

import com.google.api.services.docs.v1.DocsScopes
import com.google.api.services.drive.DriveScopes
import com.google.api.services.sheets.v4.SheetsScopes
import java.util.*

object Const {
  const val ApplicationName = "Inspection Generator (JVM)"
  const val TokensDirectoryPath = "tokens"
  const val DefaultPort = 8888
  const val NoViolationDocId = "1V7JpnbYXFNVSwuTH2z9hZLav_uhqhEAyOosLMlSwBZ4"
  const val ViolationDocId = "1kfKFd8oL7uRxPNfV_HFypcMLUHV__qgIAFMZQDpIurg"
  const val WorkingDirectoryDriveId = "1F2fnZvpKp04od_OHyQzd4w6LX9QJ8B-S"
  const val InspectionDataSheetId = "1rCEgcr6W6ouIhkdTiKhACDs7HJy4exvFZoCZh4317Hg"
  const val WestwindOwnerDataId = "1r09c2n5SrNjv8d-vMTj30ytgie5fLjU5_l7g5jjlW-w"

  val ApplicationScopes by lazy { listOf(DriveScopes.DRIVE, DocsScopes.DOCUMENTS, SheetsScopes.SPREADSHEETS) }

}