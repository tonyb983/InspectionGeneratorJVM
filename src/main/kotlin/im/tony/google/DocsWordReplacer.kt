package im.tony.google

import com.google.api.services.docs.v1.model.BatchUpdateDocumentRequest
import com.google.api.services.docs.v1.model.BatchUpdateDocumentResponse
import com.google.api.services.docs.v1.model.Document
import im.tony.Const
import im.tony.data.InspectionData
import im.tony.data.OwnerData
import im.tony.data.services.IOwnerDataService
import im.tony.data.services.OwnerDataService
import im.tony.google.extensions.drive.GoogleMimeTypes
import im.tony.google.services.DocsService
import im.tony.google.services.DriveService
import im.tony.google.services.GoogleDocsService
import im.tony.google.services.GoogleDriveService

class WordReplacerInitException : Exception("Unable to initialize DocsWordReplacer.")

class DocsWordReplacer(
  private val driveService: GoogleDriveService,
  private val docsService: GoogleDocsService,
  private val ownerDataService: IOwnerDataService
) {
  data class Result(
    val propertyDoc: Document? = null,
    val propertyDocResponses: BatchUpdateDocumentResponse? = null,
    val didRunAlt: Boolean = false,
    val altDoc: Document? = null,
    val altDocResponses: BatchUpdateDocumentResponse? = null,
    val thrown: MutableCollection<Throwable> = mutableSetOf(),
  ) {
    constructor(
      propertyDoc: Document? = null,
      propertyDocResponses: BatchUpdateDocumentResponse? = null,
      didRunAlt: Boolean = false,
      altDoc: Document? = null,
      altDocResponses: BatchUpdateDocumentResponse? = null,
      vararg thrown: Throwable,
    ) : this(propertyDoc, propertyDocResponses, didRunAlt, altDoc, altDocResponses, thrown.toMutableSet())

    constructor(
      propertyDoc: Document? = null,
      propertyDocResponses: BatchUpdateDocumentResponse? = null,
      didRunAlt: Boolean = false,
      altDoc: Document? = null,
      altDocResponses: BatchUpdateDocumentResponse? = null,
      thrown: Throwable,
    ) : this(propertyDoc, propertyDocResponses, didRunAlt, altDoc, altDocResponses, mutableSetOf(thrown))
  }


  private var folders = driveService.fetchFilesOfType(GoogleMimeTypes.Folder)?.files?.map { it.name } ?: mutableListOf()

  private fun refreshFolders() {
    folders = driveService.fetchFilesOfType(GoogleMimeTypes.Folder)?.files?.map { it.name } ?: mutableListOf()
  }

  private fun splitNumbersAndLetters(input: String): Pair<Boolean, String> {
    val numbers = input.takeWhile { it.isDigit() }
    val letters = input.takeLastWhile { it.isLetter() }

    if (numbers.length == input.length || letters.length == input.length) {
      return false to input
    }

    return true to "$numbers $letters"
  }

/*

{ "%SEND_DATE%", _ => Property.SendDate },
{ "%HOMEOWNER_NAME%", prop => prop.Homeowner ?? String.Empty },
{ "%TO_ADDRESS_1%", prop => prop.PropertyAddress?.Line1 ?? String.Empty },
{ "%TO_ADDRESS_2%", prop => prop.PropertyAddress?.Line2 ?? String.Empty },
{ "%STREET_NUM%", prop => prop.StreetNumber ?? String.Empty },
{ "%STREET_NAME%", prop => prop.StreetName ?? String.Empty },
{ "%ISSUE_LIST%", prop => prop.Issues?.Length > 0 ? String.Join("\n", prop.Issues) : String.Empty },

{ "%SEND_DATE%", _ => Property.SendDate },
{ "%HOMEOWNER_NAME%", prop => prop.Homeowner ?? String.Empty },
{ "%TO_ADDRESS_1%", prop => prop.PropertyAddress?.Line1 ?? String.Empty },
{ "%TO_ADDRESS_2%", prop => prop.PropertyAddress?.Line2 ?? String.Empty },
{ "%STREET_NUM%", prop => prop.StreetNumber ?? String.Empty },
{ "%STREET_NAME%", prop => prop.StreetName ?? String.Empty },
{ "%ISSUE_LIST%", prop => prop.Issues?.Length > 0 ? String.Join("\n", prop.Issues) : String.Empty },

{ "%SEND_DATE%", _ => Property.SendDate },
{ "%HOMEOWNER_NAME%", prop => prop.Homeowner ?? String.Empty },
{ "%TO_ADDRESS_1%", prop => prop.SecondaryAddress?.Line1 ?? String.Empty },
{ "%TO_ADDRESS_2%", prop => prop.SecondaryAddress?.Line2 ?? String.Empty },
{ "%STREET_NUM%", prop => prop.StreetNumber ?? String.Empty },
{ "%STREET_NAME%", prop => prop.StreetName ?? String.Empty },
{ "%ISSUE_LIST%", prop => prop.Issues?.Length > 0 ? String.Join("\n", prop.Issues) : String.Empty },

*/

  private fun createReplacementsFor(inspectionData: InspectionData, ownerData: OwnerData, alt: Boolean): BatchUpdateDocumentRequest {
    val (to1, to2) = ownerData.getAddress(alt)

    return docsService.createBatchUpdateRequest(
      docsService.createReplaceTextRequest("%SEND_DATE%", true, Const.SendDate),
      docsService.createReplaceTextRequest("%TO_ADDRESS_1%", true, to1),
      docsService.createReplaceTextRequest("%TO_ADDRESS_2%", true, to2),
      docsService.createReplaceTextRequest("%HOMEOWNER_NAME%", true, ownerData.familyName),
      docsService.createReplaceTextRequest("%STREET_NUM%", true, inspectionData.streetNumber),
      docsService.createReplaceTextRequest("%STREET_NAME%", true, inspectionData.streetName),
      docsService.createReplaceTextRequest("%ISSUE_LIST%", true, inspectionData.issues.joinToString("\n")),
    )
  }

  fun runOn(inspectionData: InspectionData): Result {
    val owner = runCatching {
      ownerDataService.owners.getValue(inspectionData.homeId)
    }.getOrElse { return Result(thrown = it) }

    val original = runCatching {
      if (inspectionData.isGood) {
        docsService.noViolationTemplate
      } else {
        docsService.violationTemplate
      }
    }.getOrElse { return Result(thrown = it) }

    var parentFolder: String? = null
    val (couldSplit, parentName) = splitNumbersAndLetters(inspectionData.homeId)
    if (couldSplit) {
      parentFolder = folders.firstOrNull { it.contains(parentName) }
      if (parentFolder == null) {
        refreshFolders()
        parentFolder = folders.firstOrNull { it.contains(parentName) }
      }
    }

    val toProp = runCatching {
      driveService.copyFile(original.documentId, "${inspectionData.homeId} - To Property") {
        if (parentFolder != null) {
          this.parents = listOf(parentFolder)
        }
      }
    }.getOrElse { return Result(thrown = it) }

    val toPropDoc = runCatching {
      docsService.getDocument(toProp.driveId)
    }.getOrElse { return Result(thrown = it) }

    val batch1 = runCatching {
      createReplacementsFor(inspectionData, owner, false)
    }.getOrElse { return Result(toPropDoc, thrown = it) }

    val resp1 = runCatching {
      docsService.executeRequests(toPropDoc.documentId, batch1)
    }.getOrElse { return Result(toPropDoc, thrown = it) }

    if (!owner.hasAltAddress()) {
      return Result(toPropDoc, resp1)
    }

    val toAlt = runCatching {
      driveService.copyFile(original.documentId, "${inspectionData.homeId} - To Alternate") {
        if (parentFolder != null) {
          this.parents = listOf(parentFolder)
        }
      }
    }.getOrElse { return Result(toPropDoc, resp1, thrown = it) }

    val toAltDoc = this.runCatching {
      docsService.getDocument(toAlt.driveId)
    }.getOrElse { return Result(toPropDoc, resp1, thrown = it) }

    val batch2 = runCatching {
      createReplacementsFor(inspectionData, owner, true)
    }.getOrElse { return Result(toPropDoc, resp1, true, toAltDoc, thrown = it) }

    val resp2 = runCatching {
      docsService.executeRequests(toAltDoc.documentId, batch2)
    }.getOrElse { return Result(toPropDoc, resp1, true, toAltDoc, thrown = it) }

    return Result(toPropDoc, resp1, true, toAltDoc, resp2)
  }

  companion object {
    private var instance: DocsWordReplacer? = null
    val Global: DocsWordReplacer
      get() {
        if (instance == null) {
          instance = DocsWordReplacer(DriveService, DocsService, OwnerDataService)

          if (instance == null) {
            throw WordReplacerInitException()
          }
        }

        return instance!!
      }
  }
}
