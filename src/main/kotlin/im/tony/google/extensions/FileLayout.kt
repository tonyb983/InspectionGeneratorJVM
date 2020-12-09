package im.tony.google.extensions

import java.util.*

enum class FileFieldType {
  DriveFile,
  DriveUser,
  String,
  Boolean,
  Date,
  Int,
  Long,
  Double,
  StringList,
  StringStringMap,
  UserList
}

const val DRIVEFILE: String = "drive#file"
const val DRIVEUSER: String = "drive#user"
const val STRING: Int = 0
const val BOOLEAN: Int = 1
const val DATE: Int = 2
const val INTEGER: Int = 3
const val LONG: Int = 4
const val DOUBLE: Int = 5
const val STRINGLIST: Int = 6
const val STRINGMAP: Int = 7
const val USER: Int = 8
const val USERLIST: Int = 9
const val MIMETYPE: Int = 10
const val BYTEARRAY: Int = 11
const val CONTENTRESTRICTIONLIST: Int = 12

object GFM {
  const val kind = DRIVEFILE
  const val id = STRING
  const val name = STRING
  const val mimeType = MIMETYPE
  const val description = STRING
  const val starred = BOOLEAN
  const val trashed = BOOLEAN
  const val explicitlyTrashed = BOOLEAN
  const val trashingUser = USER
  const val trashedTime = DATE
  const val parents = STRINGLIST
  const val properties = STRINGMAP
  const val appProperties = STRINGMAP
  const val spaces = STRINGLIST
  const val version = LONG
  const val webContentLink = STRING
  const val webViewLink = STRING
  const val iconLink = STRING
  const val hasThumbnail = BOOLEAN
  const val thumbnailLink = STRING
  const val thumbnailVersion = LONG
  const val viewedByMe = BOOLEAN
  const val viewedByMeTime = DATE
  const val createdTime = DATE
  const val modifiedTime = DATE
  const val modifiedByMeTime = DATE
  const val modifiedByMe = BOOLEAN
  const val sharedWithMeTime = DATE
  const val sharingUser = USER
  const val owners = USERLIST
  const val teamDriveID = STRING
  const val driveID = STRING
  const val lastModifyingUser = USER
  const val shared = BOOLEAN
  const val ownedByMe = BOOLEAN
  const val capabilities = STRINGMAP
  const val viewersCanCopyContent = BOOLEAN
  const val copyRequiresWriterPermission = BOOLEAN
  const val writersCanShare = BOOLEAN
  const val permissions = STRINGLIST
  const val permissionIDS = STRINGLIST
  const val hasAugmentedPermissions = BOOLEAN
  const val folderColorRGB = STRING
  const val originalFilename = STRING
  const val fullFileExtension = STRING
  const val fileExtension = STRING
  const val md5Checksum = STRING
  const val size = LONG
  const val quotaBytesUsed = LONG
  const val headRevisionID = STRING
  object contentHints {
    object thumbnail {
      const val image = BYTEARRAY
      const val mimeType = MIMETYPE
    }
    const val indexableText = STRING
  }
  object imageMediaMetadata {
    const val width = LONG
    const val height = LONG
    const val rotation = LONG
    object location {
      const val latitude = DOUBLE
      const val longitude = DOUBLE
      const val altitude = DOUBLE
    }
    const val time = STRING
    const val cameraMake = STRING
    const val cameraModel = STRING
    const val exposureTime = DOUBLE
    const val aperture = DOUBLE
    const val flashUsed = BOOLEAN
    const val focalLength = DOUBLE
    const val isoSpeed = INTEGER
    const val meteringMode = STRING
    const val sensor = STRING
    const val exposureMode = STRING
    const val colorSpace = STRING
    const val whiteBalance = STRING
    const val exposureBias = LONG
    const val maxApertureValue = DOUBLE
    const val subjectDistance = INTEGER
    const val lens = STRING
  }
  object videoMediaMetadata {
    const val width = INTEGER
    const val height = INTEGER
    const val durationMillis = LONG
  }
  const val isAppAuthorized = BOOLEAN
  const val exportLinks = STRINGMAP
  object shortcutDetails {
    const val targetID = STRING
    const val targetMIMEType = STRING
  }
  const val contentRestrictions = CONTENTRESTRICTIONLIST
}

data class ContentRestriction (
  val readOnly: Boolean? = null,
  val reason: String? = null,
  val restrictingUser: String? = null,
  val restrictionTime: Date? = null,
  val type: String? = null
)

data class User (
  val kind: String? = "drive#user",
  val displayName: String? = null,
  val photoLink: String? = null,
  val me: Boolean? = null,
  val permissionID: String? = null,
  val emailAddress: String? = null
)
