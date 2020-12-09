package im.tony.google.services

import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.FileList
import com.google.api.services.drive.model.File as DriveFile
import im.tony.google.extensions.*

interface GoogleDriveService {
  val fileList: FileList
  val allFiles: List<DriveFile>
}

data class FileSearchParamters(
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

val DriveService: GoogleDriveService = DriveServiceImpl
