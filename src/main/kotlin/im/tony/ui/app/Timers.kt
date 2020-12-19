package im.tony.ui.app

import java.util.*
import java.util.function.Consumer


object Timers {
  private val timers: MutableSet<Timer> = HashSet()
  fun newTimer(name: String): Timer {
    val timer = Timer(name)
    timers.add(timer)
    return timer
  }

  fun stop() {
    timers.forEach(Consumer { obj: Timer -> obj.cancel() })
  }
}
