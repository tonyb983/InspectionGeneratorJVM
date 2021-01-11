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
import im.tony.google.types.DriveFile

class WordReplacerInitException : Exception("Unable to initialize DocsWordReplacer.")

private const val emptyString = ""

class DocsWordReplacer(
  private val driveService: GoogleDriveService,
  private val docsService: GoogleDocsService,
  private val ownerDataService: IOwnerDataService?
) {
  private var ownerList: Collection<OwnerData>? = null

  constructor(
    driveService: GoogleDriveService,
    docsService: GoogleDocsService,
    ownerList: Collection<OwnerData>
  ) : this(driveService, docsService, null) {
    this.ownerList = ownerList
  }

  private val isServiceBased: Boolean = ownerDataService != null
  private val isListBased: Boolean = ownerList != null

  init {
    println("Word replacer created. isServiceBased:${isServiceBased}, isListBased:${isListBased}, ownerList size: ${ownerList?.size}")

    if (!isServiceBased && !isListBased) {
      throw IllegalStateException("DocsWordReplacer should be either list or service based, neither has been set on this mapper.")
    }
    if (instance == null) {
      instance = this
    }
  }

  data class Result(
    val inspection: InspectionData? = null,
    val owner: OwnerData? = null,
    val propertyDoc: Document? = null,
    val propertyDocResponses: BatchUpdateDocumentResponse? = null,
    val didRunAlt: Boolean = false,
    val altDoc: Document? = null,
    val altDocResponses: BatchUpdateDocumentResponse? = null,
    val thrown: MutableCollection<Throwable> = mutableSetOf(),
    val textLog: String = emptyString
  ) {
    constructor(
      inspection: InspectionData? = null,
      owner: OwnerData? = null,
      propertyDoc: Document? = null,
      propertyDocResponses: BatchUpdateDocumentResponse? = null,
      didRunAlt: Boolean = false,
      altDoc: Document? = null,
      altDocResponses: BatchUpdateDocumentResponse? = null,
      vararg thrown: Throwable,
      textLog: String = emptyString
    ) : this(inspection, owner, propertyDoc, propertyDocResponses, didRunAlt, altDoc, altDocResponses, thrown.toMutableSet(), textLog)

    constructor(
      inspection: InspectionData? = null,
      owner: OwnerData? = null,
      propertyDoc: Document? = null,
      propertyDocResponses: BatchUpdateDocumentResponse? = null,
      didRunAlt: Boolean = false,
      altDoc: Document? = null,
      altDocResponses: BatchUpdateDocumentResponse? = null,
      thrown: Throwable,
      textLog: String = emptyString
    ) : this(inspection, owner, propertyDoc, propertyDocResponses, didRunAlt, altDoc, altDocResponses, mutableSetOf(thrown), textLog)

    private val newLine by lazy { System.lineSeparator() }

    fun printResult() = emptyString +
      "${inspection?.toString()}$newLine" +
      "${owner?.toString()}$newLine" +
      propertyDoc?.let { "Property Doc: ${it.title} (ID: ${it.documentId})$newLine" } +
      propertyDocResponses?.let { "Update Replies: ${it.replies.joinToString(" | ")}$newLine" } +
      "Did run alternate address? ${if (didRunAlt) "Yes" else "No"}$newLine" +
      altDoc?.let { "Alternate Doc: ${it.title} (ID: ${it.documentId})$newLine" } +
      propertyDocResponses?.let { "Alt Update Replies: ${it.replies.joinToString(" | ")}$newLine" } +
      thrown.let { if (it.isEmpty()) "No Exceptions Thrown$newLine" else "Exceptions:$newLine${it.joinToString(newLine)}$newLine" } +
      "==== Raw Log ========================================$newLine$textLog"
  }

  private var folders = driveService.fetchFilesOfType(GoogleMimeTypes.Folder)?.files ?: mutableListOf()

  private fun refreshFolders() {
    folders = driveService.fetchFilesOfType(GoogleMimeTypes.Folder)?.files ?: mutableListOf()
  }

  private fun splitNumbersAndLetters(input: String): Pair<Boolean, String> {
    val numbers = input.takeWhile { it.isDigit() }
    val letters = input.takeLastWhile { it.isLetter() }

    if (numbers.length == input.length || letters.length == input.length) {
      return false to input
    }

    return true to "$numbers $letters"
  }

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
    val log = StringBuilder(emptyString)
    log.appendLine("Starting Replacement for ${inspectionData.streetNumber} ${inspectionData.streetName} (ID: ${inspectionData.homeId})")

    // Get owner from OwnerDataService
    val owner = runCatching {
      log.appendLine("Getting inspection property owner.")
      if (isServiceBased) {
        ownerDataService!!.owners.getValue(inspectionData.homeId)
      } else if (isListBased) {
        ownerList!!.first { it.homeId == inspectionData.homeId }
      } else {
        throw IllegalStateException("DocsWordReplacer should be either list or service based, neither has been set on this mapper.")
      }
    }.getOrElse {
      log.appendLine("Error caught while getting owner.${if (it.message != null) "\nMessage: ${it.message}" else emptyString}")
      return Result(inspection = inspectionData, thrown = it, textLog = log.toString())
    }

    // Get the Docs file for the correct template
    val original = runCatching {
      log.appendLine("Getting ${if (inspectionData.isGood) "NO VIOLATION" else "VIOLATION"} document template.")
      if (inspectionData.isGood) {
        docsService.noViolationTemplate
      } else {
        docsService.violationTemplate
      }
    }.getOrElse {
      log.appendLine("Error caught while getting template.${if (it.message != null) "\nMessage: ${it.message}" else emptyString}")
      return Result(inspection = inspectionData, owner = owner, thrown = it, textLog = log.toString())
    }

    // Try to get a valid parent folder
    log.appendLine("Trying to find parent folder name.")
    var parentFolder: DriveFile? = null
    val (couldSplit, parentName) = splitNumbersAndLetters(inspectionData.homeId)
    log.appendLine("CouldSplit: $couldSplit | ParentName: $parentName")
    if (couldSplit) {
      log.appendLine("Could split the numbers and letters of ID, trying to find valid parent folder.")
      parentFolder = folders.firstOrNull { it.name.contains(parentName) }
      if (parentFolder == null) {
        log.appendLine("No valid parent folder found, refreshing folders...")
        refreshFolders()
        parentFolder = folders.firstOrNull { it.name.contains(parentName) }
      }

      log.appendLine("Parent folder ${if (parentFolder == null) "WAS NOT" else "WAS"} found. ParentFolder: $parentFolder")
    }

    // Make the DriveFile copy for the letter going to the property address
    val toProp = runCatching {
      log.appendLine("Making copy for property address.")
      driveService.copyFile(original.documentId, "${inspectionData.homeId} - To Property") {
        if (parentFolder != null) {
          this.parents = listOf(parentFolder.id)
        }
      }
    }.getOrElse {
      log.appendLine("Error caught while making drive file copy.${if (it.message != null) "\nMessage: ${it.message}" else emptyString}")
      return Result(inspection = inspectionData, owner = owner, thrown = it, textLog = log.toString())
    }

    // Get a Docs Document from the DriveFile
    val toPropDoc = runCatching {
      log.appendLine("Getting Docs file for property address copy.")
      docsService.getDocument(toProp.id)
    }.getOrElse {
      log.appendLine("Error caught while getting Docs document.${if (it.message != null) "\nMessage: ${it.message}" else emptyString}")
      return Result(inspection = inspectionData, owner = owner, thrown = it, textLog = log.toString())
    }

    // Create the word replacement requests in batch form
    val batch1 = runCatching {
      log.appendLine("Creating batch update request for property address copy.")
      createReplacementsFor(inspectionData, owner, false)
    }.getOrElse {
      log.appendLine("Error caught while creating first update batch.${if (it.message != null) "\nMessage: ${it.message}" else emptyString}")
      return Result(inspection = inspectionData, owner = owner, toPropDoc, thrown = it, textLog = log.toString())
    }

    // Execute the batch update request and save the response
    val resp1 = runCatching {
      log.appendLine("Executing update batch for property address copy.")
      docsService.executeRequests(toPropDoc.documentId, batch1)
    }.getOrElse {
      log.appendLine("Error caught while executing first update batch.${if (it.message != null) "\nMessage: ${it.message}" else emptyString}")
      return Result(inspection = inspectionData, owner = owner, toPropDoc, thrown = it, textLog = log.toString())
    }

    // If the owner doesn't have a alternate address we're done
    if (!owner.hasAltAddress()) {
      log.appendLine("Owners do not have alternate address, returning successfully.")
      return Result(inspection = inspectionData, owner = owner, toPropDoc, resp1)
    }

    // Create the DriveFile copy for the alternate address letter
    val toAlt = runCatching {
      log.appendLine("Making copy for alternate address.")
      driveService.copyFile(original.documentId, "${inspectionData.homeId} - To Alternate") {
        if (parentFolder != null) {
          this.parents = listOf(parentFolder.id)
        }
      }
    }.getOrElse {
      log.appendLine("Error caught while creating alt drive copy.${if (it.message != null) "\nMessage: ${it.message}" else emptyString}")
      return Result(inspection = inspectionData, owner = owner, toPropDoc, resp1, thrown = it, textLog = log.toString())
    }

    // Get the Docs Document for the alternate address copy
    val toAltDoc = this.runCatching {
      log.appendLine("Getting Docs file for alternate address copy.")
      docsService.getDocument(toAlt.id)
    }.getOrElse {
      log.appendLine("Error caught while getting alt Docs document.${if (it.message != null) "\nMessage: ${it.message}" else emptyString}")
      return Result(inspection = inspectionData, owner = owner, toPropDoc, resp1, thrown = it, textLog = log.toString())
    }

    // Create the word replacement batch for the alternate letter
    val batch2 = runCatching {
      log.appendLine("Creating batch update request for alternate address copy.")
      createReplacementsFor(inspectionData, owner, true)
    }.getOrElse {
      log.appendLine("Error caught while creating alt update batch.${if (it.message != null) "\nMessage: ${it.message}" else emptyString}")
      return Result(inspection = inspectionData, owner = owner, toPropDoc, resp1, true, toAltDoc, thrown = it, textLog = log.toString())
    }

    // Execute the word replacement requests for the alternate document
    val resp2 = runCatching {
      log.appendLine("Executing update batch for alternate address copy.")
      docsService.executeRequests(toAltDoc.documentId, batch2)
    }.getOrElse {
      log.appendLine("Error caught while executing alt update batch.${if (it.message != null) "\nMessage: ${it.message}" else emptyString}")
      return Result(inspection = inspectionData, owner = owner, toPropDoc, resp1, true, toAltDoc, thrown = it, textLog = log.toString())
    }

    // Fin
    log.appendLine("Run complete.")
    return Result(inspection = inspectionData, owner = owner, toPropDoc, resp1, true, toAltDoc, resp2, textLog = log.toString())
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
