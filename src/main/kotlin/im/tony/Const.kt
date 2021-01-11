package im.tony

import com.google.api.services.docs.v1.DocsScopes
import com.google.api.services.drive.DriveScopes
import com.google.api.services.sheets.v4.SheetsScopes
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf

object Const {
  const val ApplicationName: String = "Inspection Generator (JVM)"
  const val TokensDirectoryPath: String = "tokens"
  const val DefaultPort: Int = 8888
  const val NoViolationDocId: String = "1V7JpnbYXFNVSwuTH2z9hZLav_uhqhEAyOosLMlSwBZ4"
  const val ViolationDocId: String = "1kfKFd8oL7uRxPNfV_HFypcMLUHV__qgIAFMZQDpIurg"
  const val WorkingDirectoryDriveId: String = "1F2fnZvpKp04od_OHyQzd4w6LX9QJ8B-S"
  const val InputDataSheetId: String = "1rCEgcr6W6ouIhkdTiKhACDs7HJy4exvFZoCZh4317Hg"

  object NamedRanges {
    const val InspectionData: String = "AllData"
    const val TopsData: String = "TopsData"
  }

  val ApplicationScopes: ImmutableSet<String> by lazy { persistentSetOf(DriveScopes.DRIVE, DocsScopes.DOCUMENTS, SheetsScopes.SPREADSHEETS) }
  val ScenicViewShortcut = KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN)

  const val SendDate: String = "01/01/2021"
  const val DueDate: String = "February 28, 2021"
}
