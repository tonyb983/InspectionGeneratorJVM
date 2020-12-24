package im.tony.ui.controllers

import im.tony.events.DriveFileCreatedEvent
import tornadofx.Controller

class DriveFileController : Controller() {
  fun initializeEventListener() {
    println("Subscribing to DriveFileCreatedEvents")
    subscribe<DriveFileCreatedEvent> {
      println("DriveFileCreatedEvent received: $it")
      log.warning("DriveFileCreatedEvent received: $it")
    }
  }
}
