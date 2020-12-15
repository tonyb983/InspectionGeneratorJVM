package im.tony.google.extensions

public sealed class LayoutField(public val name: String) {
  public sealed class Kind(public val value: String) : LayoutField("kind") {
    public class DriveFile : Kind("drive#file")
    public class DriveUser : Kind("drive#user")
  }

  public class Boolean(name: String) : LayoutField(name)
  public class Int(name: String) : LayoutField(name)
  public class Long(name: String) : LayoutField(name)
  public class Double(name: String) : LayoutField(name)
  public class Str(name: String) : LayoutField(name)
  public class DateTime(name: String) : LayoutField(name)
  public class Array(name: String) : LayoutField(name)
  public class Map(name: String) : LayoutField(name)
}


//
//{
//  "kind": "drive#file",
//  "id": "string",
//  "name": "string",
//  "mimeType": "string",
//  "description": "string",
//  "starred": true,
//  "trashed": true,
//  "explicitlyTrashed": true,
//  "trashingUser": "$user",
//  "trashedTime": "datetime",
//  "parents": [
//  "string"
//  ],
//  "properties": {
//  "key": "string"
//},
//  "appProperties": {
//  "key": "string"
//},
//  "spaces": [
//  "string"
//  ],
//  "version": 2147483700,
//  "webContentLink": "string",
//  "webViewLink": "string",
//  "iconLink": "string",
//  "hasThumbnail": true,
//  "thumbnailLink": "string",
//  "thumbnailVersion": 2147483700,
//  "viewedByMe": true,
//  "viewedByMeTime": "datetime",
//  "createdTime": "datetime",
//  "modifiedTime": "datetime",
//  "modifiedByMeTime": "datetime",
//  "modifiedByMe": true,
//  "sharedWithMeTime": "datetime",
//  "sharingUser": "$user",
//  "owners": [
//  "$user"
//  ],
//  "teamDriveId": "string",
//  "driveId": "string",
//  "lastModifyingUser": "$user",
//  "shared": true,
//  "ownedByMe": true,
//  "capabilities": {
//  "canAddChildren": true,
//  "canAddFolderFromAnotherDrive": true,
//  "canAddMyDriveParent": true,
//  "canChangeCopyRequiresWriterPermission": true,
//  "canChangeViewersCanCopyContent": true,
//  "canComment": true,
//  "canCopy": true,
//  "canDelete": true,
//  "canDeleteChildren": true,
//  "canDownload": true,
//  "canEdit": true,
//  "canListChildren": true,
//  "canModifyContent": true,
//  "canModifyContentRestriction": true,
//  "canMoveChildrenOutOfTeamDrive": true,
//  "canMoveChildrenOutOfDrive": true,
//  "canMoveChildrenWithinTeamDrive": true,
//  "canMoveChildrenWithinDrive": true,
//  "canMoveItemIntoTeamDrive": true,
//  "canMoveItemOutOfTeamDrive": true,
//  "canMoveItemOutOfDrive": true,
//  "canMoveItemWithinTeamDrive": true,
//  "canMoveItemWithinDrive": true,
//  "canMoveTeamDriveItem": true,
//  "canReadRevisions": true,
//  "canReadTeamDrive": true,
//  "canReadDrive": true,
//  "canRemoveChildren": true,
//  "canRemoveMyDriveParent": true,
//  "canRename": true,
//  "canShare": true,
//  "canTrash": true,
//  "canTrashChildren": true,
//  "canUntrash": true
//},
//  "viewersCanCopyContent": true,
//  "copyRequiresWriterPermission": true,
//  "writersCanShare": true,
//  "permissions": [
//  "permissions Resource"
//  ],
//  "permissionIds": [
//  "string"
//  ],
//  "hasAugmentedPermissions": true,
//  "folderColorRgb": "string",
//  "originalFilename": "string",
//  "fullFileExtension": "string",
//  "fileExtension": "string",
//  "md5Checksum": "string",
//  "size": 2147483700,
//  "quotaBytesUsed": 2147483700,
//  "headRevisionId": "string",
//  "contentHints": {
//  "thumbnail": {
//  "image": "bytes",
//  "mimeType": "string"
//},
//  "indexableText": "string"
//},
//  "imageMediaMetadata": {
//  "width": 1,
//  "height": 1,
//  "rotation": 1,
//  "location": {
//  "latitude": 1.0,
//  "longitude": 1.0,
//  "altitude": 1.0
//},
//  "time": "string",
//  "cameraMake": "string",
//  "cameraModel": "string",
//  "exposureTime": 1.0,
//  "aperture": 1.0,
//  "flashUsed": true,
//  "focalLength": 1.0,
//  "isoSpeed": 1,
//  "meteringMode": "string",
//  "sensor": "string",
//  "exposureMode": "string",
//  "colorSpace": "string",
//  "whiteBalance": "string",
//  "exposureBias": 1.0,
//  "maxApertureValue": 1.0,
//  "subjectDistance": 1,
//  "lens": "string"
//},
//  "videoMediaMetadata": {
//  "width": 1,
//  "height": 1,
//  "durationMillis": 2147483700
//},
//  "isAppAuthorized": true,
//  "exportLinks": {
//  "(key)": "string"
//},
//  "shortcutDetails": {
//  "targetId": "string",
//  "targetMimeType": "string"
//},
//  "contentRestrictions": [
//  {
//    "readOnly": true,
//    "reason": "string",
//    "restrictingUser": "$user",
//    "restrictionTime": "datetime",
//    "type": "string"
//  }
//  ],
//
//  "types": {
//  "user": {
//  "kind": "drive#user",
//  "displayName": "string",
//  "photoLink": "string",
//  "me": true,
//  "permissionId": "string",
//  "emailAddress": "string"
//}
//}
//}
