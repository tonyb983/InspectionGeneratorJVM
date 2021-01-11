package im.tony

import im.tony.data.InspectionData
import im.tony.data.OwnerData
import im.tony.google.DocsWordReplacer
import im.tony.google.services.DocsService
import im.tony.google.services.DriveService
import im.tony.google.services.SheetsService
import tornadofx.launch
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.random.Random
import kotlin.time.ExperimentalTime

public fun main2() {
  launch<MyApp>()
}

val inspectionMap: Map<String, InspectionData> by lazy {
  SheetsService
    .inspectionData
    .map { anyArrays ->
      anyArrays
        .map { it as String }
        .toMutableList()
        .let { InspectionData.parse(it) }
    }
    .distinctBy { it.homeId }
    .associateBy { it.homeId }
    .toMap()
}

val ownerMap: Map<String, OwnerData> by lazy {
  SheetsService
    .topsData
    .map { anyArrays ->
      anyArrays
        .map { it as String }
        .toMutableList()
        .let { OwnerData.parse(it) }
    }
    .distinctBy { it.homeId }
    .associateBy { it.homeId }
    .toMap()
}

private const val defaultOutputPath = "S:\\MiscLogs\\InspectionGenerator"
private const val defaultLogFilePath = "$defaultOutputPath\\logfile"

@ExperimentalTime
public fun main(args: Array<String>) {
  val newLine = System.lineSeparator()
  val logFileName = if (args.isNotEmpty()) args[0] else "$defaultLogFilePath-${Random.nextInt(0, Int.MAX_VALUE)}.txt"
  println("Starting console based inspection runner.")

  val inspections = inspectionMap.values.distinct()
  val dupes = inspectionMap.values.subtract(inspections)
  println("${inspections.size} unique inspections, ${dupes.size} duplicates")
  val owners = ownerMap.values
  println("${owners.size} owners.")
  val mapper = runCatching { DocsWordReplacer(DriveService, DocsService, owners) }.getOrThrow()
  val logFile = File(logFileName)
  var hasLog = true
  if (!logFile.createNewFile() || !logFile.canWrite()) {
    println("Error, unable to create log file.")
    hasLog = false
  }

  println("Loaded ${inspections.size} inspections and ${owners.size} owners (${owners.size - inspections.size} inspections seem to be missing).")
  println("Log File '${logFile.name}' ${if (logFile.exists()) "exists ${if (logFile.canWrite()) "and can be written" else "but cannot be written"}" else "does not exist"}.")

  downloadPdfsFromFile("${defaultOutputPath}\\ids.txt", "${defaultOutputPath}\\pdfs")
  return

  // TODO - Add simple menu interface to choose which action to complete.
  val completed = generateInspections(inspections, mapper, hasLog, logFile)
  writeIds(completed)
  downloadPdfs(completed, defaultOutputPath)
}

private fun generateInspections(
  inspections: List<InspectionData>,
  mapper: DocsWordReplacer,
  hasLog: Boolean,
  logFile: File
): Collection<Pair<InspectionData, DocsWordReplacer.Result>> {
  val newLine = System.lineSeparator()
  println("Starting generation.")

  val completed = mutableListOf<Pair<InspectionData, DocsWordReplacer.Result>>()

  for (inspection in inspections) {
    println("Inspection for ${inspection.homeId}")
    val result = mapper.runOn(inspection)
    completed.add(inspection to result)

    if (result.thrown.isNotEmpty()) {
      println("Result from ${inspection.homeId} contains thrown errors:$newLine${result.thrown.joinToString(newLine)}")
    }

    if (hasLog) {
      logFile.appendText(result.printResult() + newLine)
    }

    println("Inspection for ${inspection.homeId} Complete.")
  }

  return completed
}

fun writeIds(files: Collection<Pair<InspectionData, DocsWordReplacer.Result>>) {
  File("${defaultOutputPath}\\ids.txt").apply { this.createNewFile() }.outputStream().use { file ->
    files.forEach { pair ->
      pair.second.propertyDoc?.let {
        file.write("${it.documentId}\n".toByteArray())
      }
      pair.second.altDoc?.let {
        file.write("${it.documentId}\n".toByteArray())
      }
    }
  }
}

fun downloadPdfs(files: Collection<Pair<InspectionData, DocsWordReplacer.Result>>, targetDirectory: String) {
  //for((inspection, result) in files) { }
  files.first { it.second.propertyDoc != null }.run {
    DriveService.downloadAsPdf(this.second.propertyDoc!!.documentId).use { bytes: ByteArrayOutputStream ->
      File("$targetDirectory\\${this.second.propertyDoc!!.title}.pdf").writeBytes(bytes.toByteArray())
    }
  }
}

private fun ByteArrayOutputStream.writeToFile(dir: String, fileName: String, ext: String) {
  val sep = File.separator
  val file = File("$dir$sep$fileName.$ext")
  if (file.exists()) {
    println("Error, file at '$dir$sep$fileName.$ext' already exists.")
    return
  }

  file.writeBytes(this.toByteArray())
}

fun downloadPdfsFromFile(fileName: String, outputDir: String) {
  val file = File(fileName)
  if (!file.exists()) {
    println("Error, unable to find input file '$fileName'.")
    return
  }

  if (!file.canRead()) {
    println("File '${file.name}' is not readable.")
    return
  }

  val dir = File(outputDir)
  if (!dir.exists()) {
    dir.mkdirs()
  }

  val ids = file.readLines()
  DriveService.allFiles.filter { ids.contains(it.id) }.apply { println("Working on ${this.size} files.") }.forEach {
    DriveService.downloadAsPdf(it.id).writeToFile(outputDir, it.name, "pdf")
  }
}
