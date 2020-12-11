package im.tony.google.services

import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.FileList
import im.tony.google.extensions.*
import com.google.api.services.drive.model.File as DriveFile

public interface GoogleDriveService {
  public val fileList: FileList
  public val allFiles: List<DriveFile>

  public fun createFile(name: String, mimeType: GoogleMimeTypes? = null, parentId: String? = null): DriveFile
  public fun createFile(name: String? = null, setCreateOptions: DriveFile.() -> Unit): DriveFile

  public fun copyFile(originalId: String, newName: String, modification: (DriveFile.() -> DriveFile)? = null): DriveFile
}

public data class FileSearchParamters(
  val query: String? = null,
  val space: String? = null,
  val corpora: String? = null
)

private val DriveServiceImpl =
  object :
    GoogleDriveService,
    GenericService<Drive>(lazy { ServiceCreator.createDrive() }) {
    override val fileList: FileList by lazy {
      this.service.files().list().setSpaces(DriveSpaces.Drive).setCorpora(DriveCorporas.Drive).execute()
    }

    override val allFiles: List<DriveFile> by lazy { fileList.files }

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

    override fun copyFile(originalId: String, newName: String, modification: (DriveFile.() -> DriveFile)?): DriveFile = service
      .files()
      .copy(originalId, modification?.invoke(DriveFile().setName(newName)) ?: DriveFile().setName(newName))
      .execute()
  }

public val DriveService: GoogleDriveService = DriveServiceImpl
