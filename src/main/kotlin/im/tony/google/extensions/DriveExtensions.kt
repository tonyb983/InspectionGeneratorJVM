package im.tony.google.extensions

import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveRequest
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.http.Consts
import org.apache.http.entity.ContentType

/*
contains	The content of one string is present in the other.
  =	        The content of a string or boolean is equal to the other.
  !=        The content of a string or boolean is not equal to the other.
  <	        A value is less than another.
  <=        A value is less than or equal to another.
  >	        A value is greater than another.
  >=        A value is greater than or equal to another.
  in        An element is contained within a collection.
  and       Return items that match both queries.
  or        Return items that match either query.
  not       Negates a search query.
  has       A collection contains an element matching the parameters.
 */

public enum class QueryOperators {
  Contains,
  Eq,
  NotEq,
  Lt,
  Lte,
  Gt,
  Gte,
  In,
  And,
  Or,
  Not,
  Has;

  override fun toString(): String = when(this) {
    Contains -> "contains"
    Eq -> "="
    NotEq -> "!="
    Lt -> "<"
    Lte -> "<="
    Gt -> ">"
    Gte -> ">="
    In -> "in"
    And -> "and"
    Or -> "or"
    Not -> "not"
    Has -> "has"
  }
}

//<editor-fold desc="Drive Spaces Extensions">
/**
 * Convenience [Enum] for setting the [Drive.Files.List.spaces][com.google.api.services.drive.Drive.Files.List.spaces]
 * with my [setSpace() extension function][im.tony.google.extensions.setSpaces].
 */
public enum class DriveSpaces {
  Drive,
  AppDataFolder,
  Photos,
  All;

  public operator fun plus(space: DriveSpaces): String {
    if (this == space) return this.toString()
    if (this == All || space == All) return All.toString()

    return "${this},$space"
  }

  override fun toString(): String = when(this) {
    Drive -> "drive"
    AppDataFolder -> "appDataFolder"
    Photos -> "photos"
    All -> "drive,appDataFolder,photos"
  }
}

/**
 * ### A comma-separated list of spaces to query within the corpus.
 * Supported values are [DriveSpaces.Drive], [DriveSpaces.AppDataFolder], and [DriveSpaces.Photos].
 */
public fun Drive.Files.List.setSpaces(vararg spaces: DriveSpaces): Drive.Files.List = this.setSpaces(spaces.joinToString(","))

//</editor-fold>

//<editor-fold desc="Drive Corpora Extensions">
public enum class DriveCorporas {
  User,
  Drive,
  Domain,
  AllDrives;

  override fun toString(): String = when (this) {
    User -> "user"
    Drive -> "drive"
    Domain -> "domain"
    AllDrives -> "allDrives"
  }
}

/**
 * Groupings of files to which the query applies. Supported groupings are: [DriveCorporas.User] (files created by,
 * opened by, or shared directly with the user), [DriveCorporas.Drive] (files in the specified shared drive as
 * indicated by the 'driveId'), [DriveCorporas.Domain] (files shared to the user's domain), and [DriveCorporas.AllDrives] (A
 * combination of 'user' and 'drive' for all drives where the user is a member).
 *
 * #### When able, use *[User][DriveCorporas.User]* or *[Drive][DriveCorporas.Drive]*, instead of *[AllDrives][DriveCorporas.AllDrives]*, for efficiency.
 */
public fun Drive.Files.List.setCorpora(corpora: DriveCorporas): Drive.Files.List = this.setSpaces(corpora.toString())
//</editor-fold>

public enum class GoogleMimeTypes {
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
  }

  /**
   * ### Returns this [GoogleMimeTypes] as a string that can be used in [Drive.Files.List.q]
   * The [include] parameter dictates whether the Q string uses = or !=.
   * For example: [GoogleMimeTypes.Audio].asQueryString(true) would become "mimeType='application/vnd.google-apps.audio'"
   */
  public fun asQueryString(include: Boolean): String = "mimeType${if (include) "=" else "!="}'$this'"
}


//<editor-fold desc="Order By Extensions">
public enum class OrderBy {
  CreatedTime,
  Folder,
  ModifiedByMeTime,
  ModifiedTime,
  Name,
  NameNatural,
  QuotaBytesUsed,
  Recency,
  SharedWithMeTime,
  Starred,
  ViewedByMeTime;

  public override fun toString(): String = when (this) {
    CreatedTime -> "createdTime${if (descending) " desc" else ""}"
    Folder -> "folder${if (descending) " desc" else ""}"
    ModifiedByMeTime -> "modifiedByMeTime${if (descending) " desc" else ""}"
    ModifiedTime -> "modifiedTime${if (descending) " desc" else ""}"
    Name -> "name${if (descending) " desc" else ""}"
    NameNatural -> "name_natural${if (descending) " desc" else ""}"
    QuotaBytesUsed -> "quotaBytesUsed${if (descending) " desc" else ""}"
    Recency -> "recency${if (descending) " desc" else ""}"
    SharedWithMeTime -> "sharedWithMeTime${if (descending) " desc" else ""}"
    Starred -> "starred${if (descending) " desc" else ""}"
    ViewedByMeTime -> "viewedByMeTime${if(descending) " desc" else ""}"
  }

  public fun asDesc(): OrderBy = this.apply { descending = true }

  private var descending: Boolean = false
}

public fun Drive.Files.List.setOrderBy(vararg keys: OrderBy): Drive.Files.List = this.setOrderBy(keys.joinToString(","))
//</editor-fold>


//<editor-fold desc="Drive Coroutine Extensions">
public suspend fun <T> DriveRequest<T>.executeWithCoroutines(): T = withContext(Dispatchers.IO) { execute() }

public suspend fun Drive.createFile(
  folderId: String,
  mimeType: String,
  name: String
): String {
  val metadata = File().apply {
    parents = listOf(folderId)
    setMimeType(mimeType)
    setName(name)
  }

  return files()
    .create(metadata)
    .executeWithCoroutines()
    .id
}

public suspend fun Drive.fetchOrCreateAppFolder(folderName: String): String {
  val folder = getAppFolder()

  return if (folder.isEmpty()) {
    val metadata = File().apply {
      name = folderName
      mimeType = APP_FOLDER.mimeType
    }

    files().create(metadata)
      .setFields("id")
      .executeWithCoroutines()
      .id
  } else {
    folder.files.first().id
  }
}

public suspend fun Drive.queryFiles() = files().list().setSpaces("drive").executeWithCoroutines()

public suspend fun Drive.getAppFolder(): FileList =
  files().list().setSpaces("drive").setQ("mimeType='${APP_FOLDER.mimeType}'").executeWithCoroutines()

/**
 * https://developers.google.com/drive/api/v3/mime-types
 */
public val APP_FOLDER: ContentType = ContentType.create("application/vnd.google-apps.folder", Consts.ISO_8859_1)
//</editor-fold>
