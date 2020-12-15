@file:Suppress("unused")

package im.tony.google.types

public typealias DriveFile = com.google.api.services.drive.model.File
public typealias DriveFileList = com.google.api.services.drive.model.FileList
public typealias DriveChannel = com.google.api.services.drive.model.Channel
public typealias DriveGeneratedIds = com.google.api.services.drive.model.GeneratedIds
public typealias DriveAbout = com.google.api.services.drive.model.About
public typealias DriveChange = com.google.api.services.drive.model.Change
public typealias DriveChangeList = com.google.api.services.drive.model.ChangeList
public typealias DriveComment = com.google.api.services.drive.model.Comment
public typealias DriveCommentList = com.google.api.services.drive.model.CommentList
public typealias DriveUser = com.google.api.services.drive.model.User
public typealias DriveReply = com.google.api.services.drive.model.Reply
public typealias DriveReplyList = com.google.api.services.drive.model.ReplyList
public typealias DriveRevision = com.google.api.services.drive.model.Revision
public typealias DriveRevisionList = com.google.api.services.drive.model.RevisionList
public typealias DriveStartToken = com.google.api.services.drive.model.StartPageToken

public typealias DriveChangesStartTokenRequest = com.google.api.services.drive.Drive.Changes.GetStartPageToken
public typealias DriveChangesWatchRequest = com.google.api.services.drive.Drive.Changes.Watch
public typealias DriveChangeListRequest = com.google.api.services.drive.Drive.Changes.List

public typealias DriveFileListRequest = com.google.api.services.drive.Drive.Files.List
public typealias DriveFileGetRequest = com.google.api.services.drive.Drive.Files.Get
public typealias DriveFileCopyRequest = com.google.api.services.drive.Drive.Files.Copy
public typealias DriveFileCreateRequest = com.google.api.services.drive.Drive.Files.Create
public typealias DriveFileDeleteRequest = com.google.api.services.drive.Drive.Files.Delete
public typealias DriveFileEmptyTrashRequest = com.google.api.services.drive.Drive.Files.EmptyTrash
public typealias DriveFileExportRequest = com.google.api.services.drive.Drive.Files.Export
public typealias DriveFileGenerateIdsRequest = com.google.api.services.drive.Drive.Files.GenerateIds
public typealias DriveFileUpdateRequest = com.google.api.services.drive.Drive.Files.Update
public typealias DriveFilesWatchRequest = com.google.api.services.drive.Drive.Files.Watch

public typealias DriveChannelsStopRequest = com.google.api.services.drive.Drive.Channels.Stop

public typealias DriveRevListRequest = com.google.api.services.drive.Drive.Revisions.List
public typealias DriveRevGetRequest = com.google.api.services.drive.Drive.Revisions.Get
public typealias DriveRevDeleteRequest = com.google.api.services.drive.Drive.Revisions.Delete
public typealias DriveRevUpdateRequest = com.google.api.services.drive.Drive.Revisions.Update

public typealias DriveCommentsCreateRequest = com.google.api.services.drive.Drive.Comments.Create
public typealias DriveCommentsGetRequest = com.google.api.services.drive.Drive.Comments.Get
public typealias DriveCommentsListRequest = com.google.api.services.drive.Drive.Comments.List
public typealias DriveCommentsDeleteRequest = com.google.api.services.drive.Drive.Comments.Delete
public typealias DriveCommentsUpdateRequest = com.google.api.services.drive.Drive.Comments.Update

public typealias DrivePermissionCreateRequest = com.google.api.services.drive.Drive.Permissions.Create
public typealias DrivePermissionDeleteRequest = com.google.api.services.drive.Drive.Permissions.Delete
public typealias DrivePermissionGetRequest = com.google.api.services.drive.Drive.Permissions.Get
public typealias DrivePermissionListRequest = com.google.api.services.drive.Drive.Permissions.List
public typealias DrivePermissionUpdateRequest = com.google.api.services.drive.Drive.Permissions.Update

public typealias DriveAboutRequest = com.google.api.services.drive.Drive.About.Get

public typealias DriveRepliesCreateRequest = com.google.api.services.drive.Drive.Replies.Create
public typealias DriveRepliesDeleteRequest = com.google.api.services.drive.Drive.Replies.Delete
public typealias DriveRepliesGetRequest = com.google.api.services.drive.Drive.Replies.Get
public typealias DriveRepliesListRequest = com.google.api.services.drive.Drive.Replies.List
public typealias DriveRepliesUpdateRequest = com.google.api.services.drive.Drive.Replies.Update

public typealias DrivesListRequest = com.google.api.services.drive.Drive.Drives.List
public typealias DrivesCreateRequest = com.google.api.services.drive.Drive.Drives.Create
public typealias DrivesDeleteRequest = com.google.api.services.drive.Drive.Drives.Delete
public typealias DrivesUnhideRequest = com.google.api.services.drive.Drive.Drives.Unhide
public typealias DrivesUpdateRequest = com.google.api.services.drive.Drive.Drives.Update
public typealias DrivesHideRequest = com.google.api.services.drive.Drive.Drives.Hide
public typealias DrivesGetRequest = com.google.api.services.drive.Drive.Drives.Get

public typealias TeamDriveCreateRequest = com.google.api.services.drive.Drive.Teamdrives.Create
public typealias TeamDriveListRequest = com.google.api.services.drive.Drive.Teamdrives.List
public typealias TeamDriveGetRequest = com.google.api.services.drive.Drive.Teamdrives.Get
public typealias TeamDriveUpdateRequest = com.google.api.services.drive.Drive.Teamdrives.Update
public typealias TeamDriveDeleteRequest = com.google.api.services.drive.Drive.Teamdrives.Delete

public typealias DocsFile = com.google.api.services.docs.v1.model.Document
public typealias DocsList = com.google.api.services.docs.v1.model.List
public typealias DocsBatchUpdateResponseModel = com.google.api.services.docs.v1.model.BatchUpdateDocumentResponse
public typealias DocsReplaceTextResponseModel = com.google.api.services.docs.v1.model.ReplaceAllTextResponse
public typealias DocsReplaceTextRequestModel = com.google.api.services.docs.v1.model.ReplaceAllTextRequest
public typealias DocsBatchUpdateRequestModel = com.google.api.services.docs.v1.model.BatchUpdateDocumentRequest

public typealias DocsCreateRequest = com.google.api.services.docs.v1.Docs.Documents.Create
public typealias DocsGetRequest = com.google.api.services.docs.v1.Docs.Documents.Get
public typealias DocsBatchUpdateRequest = com.google.api.services.docs.v1.Docs.Documents.BatchUpdate

public typealias Spreadsheet = com.google.api.services.sheets.v4.model.Spreadsheet
public typealias SheetsFile = com.google.api.services.sheets.v4.model.Sheet
public typealias SheetsColor = com.google.api.services.sheets.v4.model.Color
public typealias SheetsValueRange = com.google.api.services.sheets.v4.model.ValueRange

public typealias SheetsMetadataGetRequest = com.google.api.services.sheets.v4.Sheets.Spreadsheets.DeveloperMetadata.Get
public typealias SheetsMetadataSearchRequest = com.google.api.services.sheets.v4.Sheets.Spreadsheets.DeveloperMetadata.Search

public typealias SpreadsheetGetRequest = com.google.api.services.sheets.v4.Sheets.Spreadsheets.Get
public typealias SpreadsheetCreateRequest = com.google.api.services.sheets.v4.Sheets.Spreadsheets.Create
public typealias SpreadsheetGetByDataFilterRequest = com.google.api.services.sheets.v4.Sheets.Spreadsheets.GetByDataFilter
public typealias SpreadsheetBatchUpdateRequest = com.google.api.services.sheets.v4.Sheets.Spreadsheets.BatchUpdate

public typealias SheetsValuesGetRequest = com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.Get
public typealias SheetsValuesAppendRequest = com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.Append
public typealias SheetsValuesUpdateRequest = com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.Update
public typealias SheetsValuesClearRequest = com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.Clear
public typealias SheetsValuesBatchUpdateRequest = com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.BatchUpdate
public typealias SheetsValuesBatchUpdateByDataFilterRequest = com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.BatchUpdateByDataFilter
public typealias SheetsValuesBatchClearRequest = com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.BatchClear
public typealias SheetsValuesBatchClearByDataFilterRequest = com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.BatchClearByDataFilter
public typealias SheetsValuesBatchGetRequest = com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.BatchGet
public typealias SheetsValuesBatchGetByDataFilterRequest = com.google.api.services.sheets.v4.Sheets.Spreadsheets.Values.BatchGetByDataFilter
