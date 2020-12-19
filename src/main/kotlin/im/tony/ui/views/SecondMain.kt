package im.tony.ui.views

import javafx.beans.property.ObjectProperty
import javafx.geometry.Pos
import javafx.util.Duration
import kotlinx.coroutines.*
import org.controlsfx.control.StatusBar
import tornadofx.*
import tornadofx.controlsfx.statusbar
import kotlin.math.round

class SecondMain : View("My View") {
  private var isRunning = false
  private var isRunningProperty = booleanProperty(isRunning)
  private var notifier: ObjectProperty<Any?> = objectProperty()

  // private val notifierActive = obj
  private var statusBar: StatusBar by singleAssign()

  private fun runProgress() {
    if (isRunning) return

    val task = TaskStatus()
    task.running.addListener { _, old, new -> if (!old && new) statusBar.text = "Starting Async Task..." }
    task.completed.addListener { _, old, new ->
      if (!old && new) {
        statusBar.text = "Async Task Complete"
        statusBar.progress = 1.0
      }
    }
    task.progress.addListener { _, _, new ->
      statusBar.progress = new as Double
      log.warning("Progress: $new or ${round(new * 100)}% (hopefully...)")
    }
    notifier.set(object {})
    runAsync(task) {
      this@SecondMain.isRunning = true
      Thread.sleep(5000)
      runLater(Duration.seconds(0.5)) {
        this@SecondMain.isRunning = false
        notifier.set(null)
      }
    }
  }

  override val root = borderpane {
    setPrefSize(800.0, 600.0)

    center<GeneratorRunnerView>()
    bottom {
      statusbar {
        statusBar = this
        fitToParentWidth()
        prefHeight(40.0)
        this.text = "Hello status-bar!"
        this.progress = 0.5
      }
    }

    top {
      fitToParentWidth()
      prefHeight(40.0)
      hbox(10.0, Pos.CENTER) {
        button("Start Progress") {
          disableWhen { notifier.isNotNull }
          action {
            runProgress()
          }
        }
      }
    }
  }
}
