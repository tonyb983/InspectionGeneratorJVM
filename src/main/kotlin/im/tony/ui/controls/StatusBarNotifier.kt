package im.tony.ui.controls

import im.tony.ui.app.Timers
import javafx.beans.property.DoubleProperty
import org.controlsfx.control.StatusBar
import tornadofx.runLater
import java.util.*


class StatusBarNotifier(private val statusBar: StatusBar) {
  private var doubleProperty: DoubleProperty? = null
  private var timer = createTimer()

  // count is 1-based
  fun setMsgSentProgress(count: Int, total: Int) {
    displayProgressOnProgressBar((count.toFloat() / total.toFloat()).toDouble())
    val percentage = percentage(count, total)
    displayMessageToStatusBar(String.format(Locale.ENGLISH, "Sent messages: %d/%d (%06.3f)%%", count, total, percentage))
  }

  fun clearMsgSentProgress() {
    displayProgressOnProgressBar(0.0)
  }

  private fun resetStatusBarOnConstruction() {
    statusBar.leftItems.clear()
    val rightItems = statusBar.rightItems
    rightItems.clear()
    doubleProperty = statusBar.progressProperty()
  }

  private fun percentage(count: Int, total: Int): Double {
    return count * PERCENTAGE_MAX / total
  }

  private fun displayMessageToStatusBar(message: String) {
    runLater { statusBar.textProperty().set(message) }
  }

  fun displayMessageWithFadeTimeout(message: String, delayMs: Long) {
    displayMessageToStatusBar(message)
    scheduleFadingOut(delayMs)
  }

  private fun scheduleFadingOut(delayMs: Long) {
    timer.cancel()
    timer = createTimer()
    timer.schedule(object : TimerTask() {
      override fun run() {
        runLater { statusBar.text = "" }
      }
    }, delayMs)
  }

  private fun displayProgressOnProgressBar(value: Double) {
    runLater { doubleProperty!!.set(value) }
  }

  companion object {
    const val PERCENTAGE_MAX = 100.0
    private fun createTimer(): Timer = Timers.newTimer("Thread-StatusBarNotifier")
  }

  init {
    resetStatusBarOnConstruction()
  }
}
