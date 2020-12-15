@file:Suppress("BlockingMethodInNonBlockingContext", "unused")

package im.tony.google.extensions.drive

import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveRequest
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.FileList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.http.Consts
import org.apache.http.entity.ContentType

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

public suspend fun Drive.queryFiles(): FileList = files().list().setSpaces("drive").executeWithCoroutines()

public suspend fun Drive.getAppFolder(): FileList =
  files().list().setSpaces("drive").setQ("mimeType='${APP_FOLDER.mimeType}'").executeWithCoroutines()

/**
 * https://developers.google.com/drive/api/v3/mime-types
 */
public val APP_FOLDER: ContentType = ContentType.create("application/vnd.google-apps.folder", Consts.ISO_8859_1)
