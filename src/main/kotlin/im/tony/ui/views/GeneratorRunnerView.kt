package im.tony.ui.views

import im.tony.data.InspectionData
import im.tony.data.services.InspectionDataService
import im.tony.data.services.OwnerDataService
import im.tony.events.DriveFileCreatedEvent
import im.tony.google.DocsWordReplacer
import im.tony.google.services.DocsService
import im.tony.google.services.DriveService
import im.tony.utils.ensurePositive
import javafx.beans.property.ObjectProperty
import javafx.collections.ObservableList
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import javafx.util.Duration
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import org.controlsfx.control.Notifications
import org.controlsfx.control.action.Action
import tornadofx.*
import kotlin.coroutines.CoroutineContext

class GeneratorRunnerView : View("Generator"), CoroutineScope {
  private val loadedInspections: ObservableList<Pair<InspectionData, Boolean>> = observableListOf()
  private val loadingLock: ObjectProperty<Any?> = objectProperty()
  private val runningLock: ObjectProperty<Any?> = objectProperty()
  private val wordReplacer: DocsWordReplacer by lazy { DocsWordReplacer(DriveService, DocsService, OwnerDataService.owners.values) }

  private var job = Job()
  override val coroutineContext: CoroutineContext
    get() = Dispatchers.JavaFx + job

  init {
    initializeEventListener()
  }

  private fun initializeEventListener() {
    println("Subscribing to DriveFileCreatedEvents")
    subscribe<DriveFileCreatedEvent> {
      println("DriveFileCreatedEvent received: $it")
      log.warning("DriveFileCreatedEvent received: $it")
    }
  }

  override fun onUndock() {
    super.onUndock()
    job.cancel()
    job = Job()
  }

  override val root = pane {
    fitToParentSize()

    vbox {
      fitToParentSize()
      spacing = 20.0
      hbox {
        prefHeight(40.0)
        fitToParentWidth()
        button("Load Random Inspection") {
          disableWhen { loadingLock.isNotNull }
          action {
            loadRandomInspection()
          }
        }

        button("Load all Inspections") {
          disableWhen { loadingLock.isNotNull }
          action {
            loadAllInspections()
          }
        }

        button("Run Random Inspection") {
          disableWhen { runningLock.isNotNull.and(loadingLock.isNotNull) }
          action {
            runRandomInspection()
          }
        }

        button("Run All Inspections") {
          disableWhen { runningLock.isNotNull }
          action {
            this@GeneratorRunnerView.launch { runAllInspections() }
          }
        }
      }

      spacer {
        this.prefHeight(20.0)
      }

      listview(loadedInspections) {
        this.isEditable = false
        cellFormat {
          this.graphic = cache(this.item.first.homeId) {
            vbox {
              if (!this@cellFormat.item.second) {
                lazyContextmenu {
                  item("Refresh Inspection List") {
                    action {
                      this@cellFormat.listView.refresh()
                    }
                  }
                  item("Run Inspection") {
                    action {
                      this@GeneratorRunnerView.launch { runSpecificInspection(this@cellFormat.index) }
                    }
                  }
                }
              }
              text(this@cellFormat.item.first.homeId) {
                textFill = Color.DODGERBLUE
                font = Font.font(12.0)
                isStrikethrough = this@cellFormat.item.second
                textAlignment = TextAlignment.RIGHT
              }
              text("${this@cellFormat.item.first.streetNumber} ${this@cellFormat.item.first.streetName}") {
                textFill = Color.DIMGREY
                font = Font.font(20.0)
                isStrikethrough = this@cellFormat.item.second
                textAlignment = TextAlignment.RIGHT
              }
              text("Issues: ${this@cellFormat.item.first.issueCount}") {
                textFill = if (this@cellFormat.item.first.isGood) Color.DIMGREY else Color.RED
                font = Font.font(12.0)
                isStrikethrough = this@cellFormat.item.second
                textAlignment = TextAlignment.RIGHT
              }
            }
          }
        }
      }
    }
  }

  private fun emptyListThreshold(): Notifications = Notifications.create()
    .title("Inspection List Empty")
    .text("No inspections are loaded, load at\nleast one inspection before generating.")
    .darkStyle()
    .action(Action {
      if (it.isConsumed) return@Action
      loadRandomInspection(10)
    })
    .hideAfter(Duration.seconds(5.0))

  private fun allCompleteThreshold(): Notifications = Notifications.create()
    .title("Inspections Complete")
    .text("All loaded inspections are completed.\nLoad more inspections before trying again.")
    .darkStyle()
    .action(Action {
      if (it.isConsumed) return@Action
      loadRandomInspection(10)
    })
    .hideAfter(Duration.seconds(5.0))

  private fun createBaseNotification(
    text: String,
    title: String? = null,
    vararg actions: Action,
    threshold: Notifications? = null,
    thresholdCount: Int = 5,
    duration: Duration = Duration.seconds(4.0)
  ): Notifications = Notifications.create()
    .title(title)
    .text(text)
    .darkStyle()
    .action(*actions)
    .hideAfter(duration.ensurePositive(Duration.seconds(4.0)))
    .apply { if (threshold != null) this.threshold(if (thresholdCount < 2) 5 else thresholdCount, threshold) }

  private fun showInfoNotification(
    text: String,
    title: String? = null,
    vararg actions: Action,
    threshold: Notifications? = null,
    thresholdCount: Int = 5,
  ): Unit = createBaseNotification(
    text,
    "Info${if (title != null) " - $title" else ""}",
    *actions,
    threshold = threshold,
    thresholdCount = thresholdCount,
  ).showInformation()

  private fun showWarningNotification(
    text: String,
    title: String? = null,
    vararg actions: Action,
    threshold: Notifications? = null,
    thresholdCount: Int = 5,
  ): Unit = createBaseNotification(
    text,
    "Warning${if (title != null) " - $title" else ""}",
    *actions,
    threshold = threshold,
    thresholdCount = thresholdCount,
  ).showWarning()

  private fun showErrorNotification(
    text: String,
    title: String? = null,
    vararg actions: Action,
    threshold: Notifications? = null,
    thresholdCount: Int = 5,
  ): Unit = createBaseNotification(
    text,
    "Error${if (title != null) " - $title" else ""}",
    *actions,
    threshold = threshold,
    thresholdCount = thresholdCount,
  ).showError()

  private fun runRandomInspection() = this@GeneratorRunnerView.launch(coroutineContext) {
    if (runningLock.get() != null) {
      runLater { showWarningNotification("Run is currently locked.") }
      return@launch
    }

    showInfoNotification("Starting random inspection.")

    if (loadedInspections.size < 1) {
      runLater {
        showWarningNotification(
          "No inspections are loaded, load at\nleast one inspection before generating.",
          "Inspection List Empty",
          Action {
            if (it.isConsumed) {
              return@Action
            }
            loadRandomInspection(10)
          },
          threshold = emptyListThreshold()
        )
      }
      log.warning("No inspections are loaded, load at least one inspection before generating.")
      return@launch
    }
    if (loadedInspections.all { it.second }) {
      runLater {
        showWarningNotification(
          "All loaded inspections are completed.\nLoad more inspections before trying again.",
          "Inspections Complete",
          Action {
            if (it.isConsumed) {
              return@Action
            }
            loadRandomInspection(10)
          },
          threshold = emptyListThreshold()
        )
      }
      log.warning("All loaded inspections are completed.")
      return@launch
    }

    val chosen = loadedInspections.firstOrNull { !it.second }
    if (chosen == null) {
      runLater { showErrorNotification("FirstOrNull returned null but none of the guard clauses caught it.") }
      log.severe("FirstOrNull returned null but none of the guard clauses caught it.")
      return@launch
    }

    if (chosen.second) {
      runLater { showErrorNotification("FirstOrNull returned a completed inspection despite its predicate.") }
      log.severe("FirstOrNull returned a completed inspection despite its predicate.")
      return@launch
    }

    val chosenIndex = loadedInspections.indexOf(chosen)

    runLater { showInfoNotification("Running inspection on ${chosen.first.homeId}, which ${if (chosen.second) "IS" else "IS NOT"} complete.\nFound at index $chosenIndex.") }

    val result = runInspection(chosen.first).await()
    if (result) {
      loadedInspections.remove(chosen)
      loadedInspections.add(chosen.first to true)
    }
  }

  private suspend fun runSpecificInspection(index: Int) = this@GeneratorRunnerView.launch(coroutineContext) {
    if (runningLock.get() != null) {
      runLater { showWarningNotification("Run is currently locked.") }
      return@launch
    }

    if (index !in 0 until loadedInspections.size) {
      runLater { showErrorNotification("Invalid index passed to runSpecificInspection: $index") }
      log.severe("Invalid index passed to runSpecificInspection: $index")
      return@launch
    }

    val (i, complete) = loadedInspections[index]
    if (complete) {
      runLater { showWarningNotification("Inspection ${i.homeId} has already been completed.") }
      log.warning("Inspection ${i.homeId} has already been completed.")
      return@launch
    }

    val result = runInspection(i).await()

    if (!result) {
      return@launch
    }

    loadedInspections.remove(index, index + 1)
    loadedInspections.add(i to true)
  }


  private fun runInspection(inspection: InspectionData) = this@GeneratorRunnerView.async(coroutineContext) {
    if (runningLock.get() != null) {
      runLater { showWarningNotification("Run is currently locked.") }
      return@async false
    }

    runLater { showInfoNotification("Starting Run on Inspection for ${inspection.homeId}") }
    runningLock.set({})
    val result = wordReplacer.runOn(inspection)
    runLater { showInfoNotification(result.textLog) }
    log.warning(result.textLog)

    runLater { runningLock.set(null) }
    return@async if (result.thrown.isNotEmpty()) {
      val messages = result.thrown.filter { it.message != null }.map { it.message }.joinToString("\n")

      log.severe("An exception was thrown by run on ${inspection.homeId}.${if (messages.isNotEmpty()) "\nMessages: $messages" else ""}")
      runLater {
        showErrorNotification("An exception was thrown by run on ${inspection.homeId}.${if (messages.isNotEmpty()) "\nMessages: $messages" else ""}")
        runningLock.set(null)
      }
      false
    } else {
      log.warning("Run completed without exception. Consider it complete.")
      log.info(result.textLog)
      runLater {
        showInfoNotification("Run completed without exception. Consider it complete.")
        runningLock.set(null)
      }
      true
    }
  }

  private fun loadRandomInspection(maxAttempts: Int = 25) = this@GeneratorRunnerView.launch(coroutineContext) {
    if (loadingLock.get() != null) {
      runLater { showWarningNotification("Load is currently locked.") }
      return@launch
    }

    loadingLock.set({})
    runLater { showInfoNotification("Loading random inspection.") }
    val max = if (maxAttempts <= 1) 25 else maxAttempts
    var attempts = 0
    while (true) {
      val i = InspectionDataService.inspections.values.random()
      if (
        loadedInspections
          .map { it.first }
          .none { it.homeId == i.homeId }
      ) {
        loadedInspections.add(i to false)
        break
      } else {
        attempts += 1
      }

      if (attempts >= max) {
        log.warning("Unable to get a new Inspection after $attempts attempts.")
        break
      }
    }

    loadingLock.set(null)
  }

  private fun loadAllInspections() = this@GeneratorRunnerView.launch(coroutineContext) {
    if (loadingLock.get() != null) {
      runLater { showWarningNotification("Load is currently locked.") }
      return@launch
    }

    loadingLock.set({})

    runAsync(TaskStatus()) {
      runLater { showInfoNotification("Loading all unloaded inspections.") }
      val allKeys = InspectionDataService.inspections.keys
      val keys = allKeys.subtract(loadedInspections.map { it.first.homeId })

      if (keys.isNotEmpty()) {
        keys.mapNotNull { InspectionDataService.inspections[it] }.mapTo(loadedInspections) { it to false }
      }

      runLater { loadingLock.set(null) }
    }
    return@launch
    //val unloaded = InspectionDataService.inspections.values.subtract(loadedInspections.map { it.first })
    //unloaded.forEach { loadedInspections.add(it to false) }
  }

  private suspend fun runAllInspections() = Dispatchers.JavaFx {
    if (runningLock.get() != null) {
      runLater { showWarningNotification("Run is currently locked.") }
      log.warning("Run is currently locked.")
      return@JavaFx
    }
    if (loadingLock.get() != null) {
      runLater { showWarningNotification("Load is currently locked (run all needs both locks to run).") }
      log.warning("Load is currently locked (run all needs both locks to run).")
      return@JavaFx
    }

    runningLock.set({})
    loadingLock.set({})

    runLater { showInfoNotification("Running all unrun inspections...") }
    val startTotal = loadedInspections.size
    val (done, notDone) = loadedInspections.partition { it.second }
    val doneCount = done.size
    val notDoneCount = notDone.size
    var completeCount = 0
    var failCount = 0

    if (notDone.isNotEmpty()) {
      for (todo in notDone) {
        if (!this.isActive) {

        }
      }
      // completeCount = completed.count { it.second }
      // failCount = completed.count { !it.second }
      // loadedInspections.clear()
      // loadedInspections.addAll(done)
      // loadedInspections.addAll(completed)
    }

    runLater {
      runningLock.set(null)
      loadingLock.set(null)
    }

    runLater { showInfoNotification("Run All Completed.\nStarting Size: $startTotal\nStarting Complete Count: $doneCount\nStarting Incomplete Size: $notDoneCount\nCompleted: $completeCount | Failed: $failCount\nEnding Size: ${loadedInspections.size}") }

    if (startTotal != loadedInspections.size) {
      runLater { showErrorNotification("Error, starting size does not match ending size, something probably went wrong :(") }
      log.severe("Error, starting size does not match ending size, something probably went wrong :(")
    }
  }
}
