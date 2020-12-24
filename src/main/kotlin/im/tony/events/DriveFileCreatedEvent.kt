package im.tony.events

import im.tony.google.extensions.drive.GoogleMimeTypes
import io.konform.validation.Validation
import io.konform.validation.jsonschema.enum
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern
import tornadofx.EventBus
import tornadofx.FXEvent

data class DriveFileCreatedEvent(
  val fileId: String,
  val fileName: String? = null,
  val mimeType: String? = null
) : FXEvent(EventBus.RunOn.BackgroundThread) {
  constructor(fileId: String, fileName: String? = null, mimeType: GoogleMimeTypes? = null) : this(fileId, fileName, mimeType?.toString())

  fun asDataFileString(): String = "" +
    "fileId=$fileId" +
    (if (fileName != null) ",fileName=$fileName" else "") +
    if (mimeType != null) ",mimeType=$mimeType" else ""

  companion object {
    fun fromDataFile(fileLine: String?): DriveFileCreatedEvent? {
      if (fileLine.isNullOrBlank()) {
        return null
      }

      val parts = fileLine.split(",")
      val fId = parts.firstOrNull { it.startsWith("fileId=") }?.removePrefix("fileId=") ?: return null
      val fName = parts.firstOrNull { it.startsWith("fileName=") }?.removePrefix("fileName=")
      val mType = parts.firstOrNull { it.startsWith("mimeType=") }?.removePrefix("mimeType=")

      return DriveFileCreatedEvent(fId, fName, mType)
    }
  }
}

val DriveFileCreatedEventValidator = Validation<DriveFileCreatedEvent> {
  DriveFileCreatedEvent::fileId required {
    this.pattern(Regex("([-\\w]{25,})")) hint "File ID should match the Regex."
    this.minLength(25) hint "Google Drive File ID's should be at least 25 characters."
    this.maxLength(35) hint "Google Drive File ID's should be at most 35 characters."
  }

  DriveFileCreatedEvent::mimeType ifPresent {
    this.enum<GoogleMimeTypes>()
  }
}
