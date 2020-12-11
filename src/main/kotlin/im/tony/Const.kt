package im.tony

import com.google.api.services.docs.v1.DocsScopes
import com.google.api.services.drive.DriveScopes
import com.google.api.services.sheets.v4.SheetsScopes
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf

public object Const {
  public const val ApplicationName: String = "Inspection Generator (JVM)"
  public const val TokensDirectoryPath: String = "tokens"
  public const val DefaultPort: Int = 8888
  public const val NoViolationDocId: String = "1V7JpnbYXFNVSwuTH2z9hZLav_uhqhEAyOosLMlSwBZ4"
  public const val ViolationDocId: String = "1kfKFd8oL7uRxPNfV_HFypcMLUHV__qgIAFMZQDpIurg"
  public const val WorkingDirectoryDriveId: String = "1F2fnZvpKp04od_OHyQzd4w6LX9QJ8B-S"
  public const val InputDataSheetId: String = "1rCEgcr6W6ouIhkdTiKhACDs7HJy4exvFZoCZh4317Hg"

  public object NamedRanges {
    public const val InspectionData: String = "AllData"
    public const val TopsData: String = "TopsData"
  }

  public val ApplicationScopes: ImmutableSet<String> by lazy { persistentSetOf(DriveScopes.DRIVE, DocsScopes.DOCUMENTS, SheetsScopes.SPREADSHEETS) }

}
