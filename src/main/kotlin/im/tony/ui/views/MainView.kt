package im.tony.ui.views

import im.tony.Const
import im.tony.google.extensions.drive.GoogleMimeTypesExtensions
import im.tony.google.services.DriveService
import im.tony.google.services.SheetsService
import im.tony.google.types.DriveFile
import im.tony.ui.UiServices
import im.tony.utils.quote
import io.github.serpro69.kfaker.Faker
import javafx.beans.binding.StringBinding
import javafx.collections.ObservableList
import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.layout.Priority
import tornadofx.*
import java.util.logging.Level
import kotlin.random.Random

public class MainView : View("My View") {
  private val mainText: String
  private val faker: Faker = Faker()

  private val random: Random = Random(0)
  private val levels: List<Level> by lazy { listOf(Level.FINER, Level.FINE, Level.FINEST, Level.INFO, Level.WARNING, Level.SEVERE, Level.CONFIG) }

  private fun getRandomLevel(): Level = levels.random()

  private fun getRandomQuote(): String = when (random.nextInt(0, 120)) {
    in 0..5 -> "His last words were ... '${faker.quote.famousLastWords()}'"
    in 6..10 -> "Matz said ... '${faker.quote.matz()}'"

    in 11..15 -> faker.quote.mostInterestingManInTheWorld()
    in 16..20 -> "Robin said ... '${faker.quote.robin()}'"

    in 21..25 -> "Yoda said ... '${faker.quote.yoda()}'"
    in 26..30 -> "Siegler said ... '${faker.quote.singularSiegler()}'"

    in 31..35 -> "Ancient Hero: ${faker.ancient.hero()}"
    in 36..40 -> "Ancient Primordial: ${faker.ancient.primordial()}"

    in 41..45 -> "Ancient God: ${faker.ancient.god()}"
    in 46..50 -> "Ancient Titan: ${faker.ancient.titan()}"

    in 51..55 -> "Ancient Titan: ${faker.ancient.titan()}"
    in 56..60 -> "Michael said ... '${faker.michaelScott.quotes()}'"

    in 61..65 -> faker.bigBangTheory.quotes()
    in 66..70 -> faker.departed.quotes()

    in 71..75 -> faker.rickAndMorty.quotes()
    in 76..80 -> faker.greekPhilosophers.quotes()

    in 81..85 -> faker.drWho.quotes()
    in 86..90 -> faker.prince.lyric()

    in 91..95 -> faker.witcher.quotes()
    in 96..120 -> faker.eSport.quote()

    else -> "Whoops"
  }

  private val docs by lazy {
    DriveService.fetchFilesOfType(GoogleMimeTypesExtensions.Document, "iconLink")?.files?.toObservable() ?: observableListOf()
  }
  private val folders by lazy {
    DriveService.fetchFilesOfType(GoogleMimeTypesExtensions.Folder, "iconLink")?.files?.toObservable() ?: observableListOf()
  }

  private var selectedDoc: DriveFile? = null
  private var selectedFolder: DriveFile? = null

  private val sizeObs: StringBinding = stringBinding(
    UiServices.records.sizeProperty,
    UiServices.records,
    UiServices.records.sizeProperty
  ) { "Records Size: ${this.value}" }

  private fun createFileList(parent: EventTarget, l: ObservableList<DriveFile>, onSelect: (DriveFile?) -> Unit): ListView<DriveFile> {
    return parent.listview(l) {
      this.selectionModel.selectionMode = SelectionMode.SINGLE
      cellFormat {
        this.graphic = cache(it.id) {
          hbox {
            if (it.iconLink != null) {
              imageview(it.iconLink) {
                prefWidth = prefHeight
              }
            }
            vbox {
              text(it.id)
              text(it.name)
            }
          }
        }
      }

      isEditable = false
      onUserSelect(1) {
        onSelect.invoke(it)
      }
    }
  }

  init {
    var s = ""
    val spreadsheets = SheetsService.loadSpreadsheetMetadata(Const.InputDataSheetId)
    s += "Title: ${spreadsheets.properties.title}\n\n"
    spreadsheets.sheets.forEach { sheet ->
      s += "Sheet Title: ${sheet.properties.title}\n"
      s += "Sheet Type: ${sheet.properties.sheetType}\n"
      sheet.properties.gridProperties?.also { gp ->
        s += "Row Count: ${gp.rowCount} | Col Count: ${gp.columnCount}\n"
        s += "Frozen Rows: ${gp.frozenRowCount} | Frozen Cols: ${gp.frozenColumnCount}\n"
      }
      s += "\n"
    }

    mainText = s
  }

  override val root: Parent = borderpane {
    setPrefSize(800.0, 600.0)
    top {
      scrollpane(fitToWidth = true, fitToHeight = true) {
        text(mainText)
      }
    }

    bottom<LogView>()

    right {
      vbox(10, Pos.CENTER) {
        button("Add Log") {
          action {
            UiServices.logger.log(getRandomLevel(), getRandomQuote())
          }
        }

        spacer(Priority.SOMETIMES) {
          maxHeight = 100.0
        }

        text(sizeObs)
      }
    }

    center {
      vbox {
        hbox(alignment = Pos.TOP_CENTER) {
          fitToParentSize()

          createFileList(this, docs) { selectedDoc = it }
          createFileList(this, folders) { selectedFolder = it }
        }

        spacer(Priority.SOMETIMES)

        hbox {
          spacer(Priority.SOMETIMES)
          button("Make Copy") {
            action {
              if (selectedDoc == null || selectedFolder == null) {
                UiServices.log(Level.WARNING, "MainView Make Copy") { "Make Copy cannot run with null doc or null folder." }
              } else {
                UiServices.log(
                  Level.INFO,
                  "MainView Make Copy"
                ) { "Would have created copy of '${selectedDoc!!.name}' with '${selectedFolder!!.name}' as its parent." }
              }
            }
          }
          spacer(Priority.SOMETIMES)
        }
      }
    }
  }
}
