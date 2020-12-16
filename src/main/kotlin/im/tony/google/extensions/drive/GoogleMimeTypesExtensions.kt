package im.tony.google.extensions.drive

enum class GoogleMimeTypes {
  Invalid,

  /**
   * Audio File
   */
  Audio,

  /**
   * Google Docs File
   */
  Document,

  /**
   * 3rd Party Shortcut
   */
  DriveSdk,

  /**
   * Google Drawing File
   */
  Drawing,

  /**
   * Google Drive File
   */
  File,

  /**
   * Google Drive Folder
   */
  Folder,

  /**
   * Google Forms File
   */
  Form,
  FusionTable,

  /**
   * Google My Maps
   */
  Map,

  /**
   * Photo File
   */
  Photo,

  /**
   * Google Slides File
   */
  Presentation,

  /**
   * Google Apps Script
   */
  Script,

  /**
   * Google Drive Shortcut
   */
  Shortcut,

  /**
   * Google Sites
   */
  Site,

  /**
   * Google Sheets File
   */
  Spreadsheet,
  Unknown,

  /**
   * Video File
   */
  Video;

  override fun toString(): String = when (this) {
    Audio -> "application/vnd.google-apps.audio"
    Document -> "application/vnd.google-apps.document"
    DriveSdk -> "application/vnd.google-apps.drive-sdk"
    Drawing -> "application/vnd.google-apps.drawing"
    File -> "application/vnd.google-apps.file"
    Folder -> "application/vnd.google-apps.folder"
    Form -> "application/vnd.google-apps.form"
    FusionTable -> "application/vnd.google-apps.fusiontable"
    Map -> "application/vnd.google-apps.map"
    Photo -> "application/vnd.google-apps.photo"
    Presentation -> "application/vnd.google-apps.presentation"
    Script -> "application/vnd.google-apps.script"
    Shortcut -> "application/vnd.google-apps.shortcut"
    Site -> "application/vnd.google-apps.site"
    Spreadsheet -> "application/vnd.google-apps.spreadsheet"
    Unknown -> "application/vnd.google-apps.unknown"
    Video -> "application/vnd.google-apps.video"
    Invalid -> ""
  }

  /**
   * ### Returns this [GoogleMimeTypes] as a string that can be used in [Drive.Files.List.q]
   * The [include] parameter dictates whether the Q string uses = or !=.
   * For example: [GoogleMimeTypes.Audio].asQueryString(true) would become "mimeType='application/vnd.google-apps.audio'"
   */
  fun asQueryString(include: Boolean = true): String = "mimeType${if (include) "=" else "!="}'$this'"

  companion object {
    val allMimes: Collection<String> by lazy { values().toMutableList().apply { this.remove(Invalid) }.map { it.toString() } }

    fun fromMime(input: String?): GoogleMimeTypes = when (input) {
      "application/vnd.google-apps.audio" -> Audio
      "application/vnd.google-apps.document" -> Document
      "application/vnd.google-apps.drive-sdk" -> DriveSdk
      "application/vnd.google-apps.drawing" -> Drawing
      "application/vnd.google-apps.file" -> File
      "application/vnd.google-apps.folder" -> Folder
      "application/vnd.google-apps.form" -> Form
      "application/vnd.google-apps.fusiontable" -> FusionTable
      "application/vnd.google-apps.map" -> Map
      "application/vnd.google-apps.photo" -> Photo
      "application/vnd.google-apps.presentation" -> Presentation
      "application/vnd.google-apps.script" -> Script
      "application/vnd.google-apps.shortcut" -> Shortcut
      "application/vnd.google-apps.site" -> Site
      "application/vnd.google-apps.spreadsheet" -> Spreadsheet
      "application/vnd.google-apps.unknown" -> Unknown
      "application/vnd.google-apps.video" -> Video
      else -> Invalid
    }

    fun fromName(input: String?): GoogleMimeTypes = when (input?.toLowerCase()) {
      "audio" -> Audio
      "document" -> Document
      "driveSdk" -> DriveSdk
      "drawing" -> Drawing
      "file" -> File
      "folder" -> Folder
      "form" -> Form
      "fusiontable" -> FusionTable
      "map" -> Map
      "photo" -> Photo
      "presentation" -> Presentation
      "script" -> Script
      "shortcut" -> Shortcut
      "site" -> Site
      "spreadsheet" -> Spreadsheet
      "unknown" -> Unknown
      "video" -> Video
      else -> Invalid
    }

    fun fromQueryString(input: String?): GoogleMimeTypes = fromMime(
      input
        ?.removePrefix("mimeType!=")
        ?.removePrefix("mimeType=")
        ?.removeSurrounding("'")
    )
  }
}
