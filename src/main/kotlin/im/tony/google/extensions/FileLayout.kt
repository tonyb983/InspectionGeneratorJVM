package im.tony.google.extensions

import java.util.*

public enum class FileFieldType {
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

public const val DRIVEFILE: String = "drive#file"
public const val DRIVEUSER: String = "drive#user"
public const val STRING: Int = 0
public const val BOOLEAN: Int = 1
public const val DATE: Int = 2
public const val INTEGER: Int = 3
public const val LONG: Int = 4
public const val DOUBLE: Int = 5
public const val STRINGLIST: Int = 6
public const val STRINGMAP: Int = 7
public const val USER: Int = 8
public const val USERLIST: Int = 9
public const val MIMETYPE: Int = 10
public const val BYTEARRAY: Int = 11
public const val CONTENTRESTRICTIONLIST: Int = 12

public object GFM {
  public const val kind: String = DRIVEFILE
  public const val id: Int = STRING
  public const val name: Int = STRING
  public const val mimeType: Int = MIMETYPE
  public const val description: Int = STRING
  public const val starred: Int = BOOLEAN
  public const val trashed: Int = BOOLEAN
  public const val explicitlyTrashed: Int = BOOLEAN
  public const val trashingUser: Int = USER
  public const val trashedTime: Int = DATE
  public const val parents: Int = STRINGLIST
  public const val properties: Int = STRINGMAP
  public const val appProperties: Int = STRINGMAP
  public const val spaces: Int = STRINGLIST
  public const val version: Int = LONG
  public const val webContentLink: Int = STRING
  public const val webViewLink: Int = STRING
  public const val iconLink: Int = STRING
  public const val hasThumbnail: Int = BOOLEAN
  public const val thumbnailLink: Int = STRING
  public const val thumbnailVersion: Int = LONG
  public const val viewedByMe: Int = BOOLEAN
  public const val viewedByMeTime: Int = DATE
  public const val createdTime: Int = DATE
  public const val modifiedTime: Int = DATE
  public const val modifiedByMeTime: Int = DATE
  public const val modifiedByMe: Int = BOOLEAN
  public const val sharedWithMeTime: Int = DATE
  public const val sharingUser: Int = USER
  public const val owners: Int = USERLIST
  public const val teamDriveID: Int = STRING
  public const val driveID: Int = STRING
  public const val lastModifyingUser: Int = USER
  public const val shared: Int = BOOLEAN
  public const val ownedByMe: Int = BOOLEAN
  public const val capabilities: Int = STRINGMAP
  public const val viewersCanCopyContent: Int = BOOLEAN
  public const val copyRequiresWriterPermission: Int = BOOLEAN
  public const val writersCanShare: Int = BOOLEAN
  public const val permissions: Int = STRINGLIST
  public const val permissionIDS: Int = STRINGLIST
  public const val hasAugmentedPermissions: Int = BOOLEAN
  public const val folderColorRGB: Int = STRING
  public const val originalFilename: Int = STRING
  public const val fullFileExtension: Int = STRING
  public const val fileExtension: Int = STRING
  public const val md5Checksum: Int = STRING
  public const val size: Int = LONG
  public const val quotaBytesUsed: Int = LONG
  public const val headRevisionID: Int = STRING

  public object contentHints {
    public object thumbnail {
      public const val image: Int = BYTEARRAY
      public const val mimeType: Int = MIMETYPE
    }

    public const val indexableText: Int = STRING
  }

  public object imageMediaMetadata {
    public const val width: Int = LONG
    public const val height: Int = LONG
    public const val rotation: Int = LONG

    public object location {
      public const val latitude: Int = DOUBLE
      public const val longitude: Int = DOUBLE
      public const val altitude: Int = DOUBLE
    }

    public const val time: Int = STRING
    public const val cameraMake: Int = STRING
    public const val cameraModel: Int = STRING
    public const val exposureTime: Int = DOUBLE
    public const val aperture: Int = DOUBLE
    public const val flashUsed: Int = BOOLEAN
    public const val focalLength: Int = DOUBLE
    public const val isoSpeed: Int = INTEGER
    public const val meteringMode: Int = STRING
    public const val sensor: Int = STRING
    public const val exposureMode: Int = STRING
    public const val colorSpace: Int = STRING
    public const val whiteBalance: Int = STRING
    public const val exposureBias: Int = LONG
    public const val maxApertureValue: Int = DOUBLE
    public const val subjectDistance: Int = INTEGER
    public const val lens: Int = STRING
  }

  public object videoMediaMetadata {
    public const val width: Int = INTEGER
    public const val height: Int = INTEGER
    public const val durationMillis: Int = LONG
  }

  public const val isAppAuthorized: Int = BOOLEAN
  public const val exportLinks: Int = STRINGMAP

  public object shortcutDetails {
    public const val targetID: Int = STRING
    public const val targetMIMEType: Int = STRING
  }

  public const val contentRestrictions: Int = CONTENTRESTRICTIONLIST
}

public data class ContentRestriction(
  val readOnly: Boolean? = null,
  val reason: String? = null,
  val restrictingUser: String? = null,
  val restrictionTime: Date? = null,
  val type: String? = null
)

public data class User(
  val kind: String? = "drive#user",
  val displayName: String? = null,
  val photoLink: String? = null,
  val me: Boolean? = null,
  val permissionID: String? = null,
  val emailAddress: String? = null
)
