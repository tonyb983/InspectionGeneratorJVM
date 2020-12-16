package im.tony.google.services

import com.google.api.services.drive.Drive
import im.tony.google.extensions.drive.*
import im.tony.google.types.DriveFile
import im.tony.google.types.DriveFileList
import im.tony.google.types.DriveFileListRequest

// import com.google.api.services.drive.model.File as DriveFile

interface GoogleDriveService {
  var defaultCorporas: DriveCorporas
  var defaultSpaces: DriveSpaces

  val fileList: DriveFileList
  val allFiles: List<DriveFile>

  fun fetchFiles(config: Drive.Files.List.() -> Unit): DriveFileList?

  /**
   * Fetches all files of the given *[type]*. The function will by default only fetch the
   * id, name, and mimType fields of the files, add any other file properties you'd like to
   * gather to the *[otherFileProps]* variable, they will be joined by commas and appended.
   * #### Returns a *[DriveFileList]* containing any files which match the given mimeType.
   */
  fun fetchFilesOfType(type: GoogleMimeTypes, vararg otherFileProps: String): DriveFileList? = fetchFiles {
    fields = "files(id,name,mimeType${if (otherFileProps.isNotEmpty()) ",${otherFileProps.joinToString(",")}" else ""})"
    q = type.asQueryString()
  }

  /**
   * Creates a file with the given [name], [mimeType], and [parentId], returns the
   * newly created [DriveFile].
   */
  fun createFile(name: String, mimeType: GoogleMimeTypes? = null, parentId: String? = null): DriveFile

  /**
   * Creates a file with the given [name]. Use [setCreateOptions] to customize the creation options
   * of the [DriveFile] created.
   */
  fun createFile(name: String? = null, setCreateOptions: DriveFile.() -> Unit): DriveFile

  fun copyFile(originalId: String, newName: String, modification: (DriveFile.() -> Unit)? = null): DriveFile
}

data class FileSearchParameters(
  val query: String? = null,
  val spaces: String? = null,
  val spacesType: DriveSpaces? = null,
  val corpora: String? = null,
  val corporaType: DriveCorporas? = null,
)

private fun DriveFileListRequest.applyParams(fsp: FileSearchParameters) = this.apply {
  if (fsp.corpora != null) {
    corpora = fsp.corpora
  } else if (fsp.corporaType != null) {
    setCorpora(fsp.corporaType)
  }

  if (fsp.spaces != null) {
    spaces = fsp.spaces
  } else if (fsp.spacesType != null) {
    setSpaces(fsp.spacesType)
  }

  if (fsp.query != null) {
    q = fsp.query
  }
}

private fun DriveFileListRequest.onlyNameAndId() = this.setFields("files(id, name)")
private fun DriveFileListRequest.nameIdAndMax(max: Int) = this.setFields("maxResults=$max,nextPageToken,files(id, name)")

private val DriveServiceImpl =
  object :
    GoogleDriveService,
    GenericService<Drive>(lazy { ServiceCreator.createDrive() }) {
    override var defaultCorporas: DriveCorporas = DriveCorporas.User
    override var defaultSpaces: DriveSpaces = DriveSpaces.Drive

    override val fileList: DriveFileList by lazy {
      service
        .files()
        .list()
        .setSpaces(defaultSpaces)
        .setCorpora(defaultCorporas)
        .execute()
    }

    override val allFiles: List<DriveFile> by lazy {
      service
        .files()
        .list()
        .setSpaces(defaultSpaces)
        .setCorpora(defaultCorporas)
        .onlyNameAndId()
        .execute()
        .files
    }

    override fun fetchFiles(config: Drive.Files.List.() -> Unit): DriveFileList? = service
      .files()
      .list()
      .setSpaces(defaultSpaces)
      .setCorpora(defaultCorporas)
      .apply(config)
      .execute()

    override fun createFile(name: String, mimeType: GoogleMimeTypes?, parentId: String?): DriveFile = createFile(name) {
      if (mimeType != null) this.mimeType = mimeType.toString()
      if (parentId != null) this.parents = listOf(parentId)
    }

    override fun createFile(
      name: String?,
      setCreateOptions: DriveFile.() -> Unit
    ): DriveFile {
      val file = DriveFile()

      if (name != null) file.name = name
      setCreateOptions.invoke(file)

      return file
    }

    override fun copyFile(originalId: String, newName: String, modification: (DriveFile.() -> Unit)?): DriveFile = service
      .files()
      .copy(originalId, DriveFile().setName(newName).apply { modification?.invoke(this) })
      .execute()
  }

val DriveService: GoogleDriveService = DriveServiceImpl
