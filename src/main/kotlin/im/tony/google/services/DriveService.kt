package im.tony.google.services

import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.FileList
import im.tony.google.extensions.DriveCorporas
import im.tony.google.extensions.DriveSpaces
import im.tony.google.extensions.setCorpora
import im.tony.google.extensions.setSpaces
import com.google.api.services.drive.model.File as DriveFile

public interface GoogleDriveService {
  public val fileList: FileList
  public val allFiles: List<DriveFile>
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
  }

public val DriveService: GoogleDriveService = DriveServiceImpl
