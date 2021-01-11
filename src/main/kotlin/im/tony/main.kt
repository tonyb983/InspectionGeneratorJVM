package im.tony

import im.tony.data.InspectionData
import im.tony.data.OwnerData
import im.tony.google.DocsWordReplacer
import im.tony.google.services.DocsService
import im.tony.google.services.DriveService
import im.tony.google.services.SheetsService
import tornadofx.launch
import java.io.File
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

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

private const val defaultLogFilePath = "S:\\MiscLogs\\InspectionGenerator\\logfile"

@ExperimentalTime
public fun main(args: Array<String>) {
  val newLine = System.lineSeparator()
  val timeSource = TimeSource.Monotonic
  val logFileName = if (args.isNotEmpty()) args[0] else "$defaultLogFilePath-${timeSource.markNow().elapsedNow().toInt(DurationUnit.SECONDS)}.txt"
  println("Starting console based inspection runner.")

  val inspections = inspectionMap.values
  println("${inspections.size} inspections.")
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
  println("Starting generation.")

  for (inspection in inspections) {
    println("Inspection for ${inspection.homeId}")
    val result = mapper.runOn(inspection)

    if (result.thrown.isNotEmpty()) {
      println("Result from ${inspection.homeId} contains thrown errors:$newLine${result.thrown.joinToString(newLine)}")
    }

    if (hasLog) {
      logFile.writeText(result.printResult() + newLine)
    }

    println("Generation Complete.")
  }
}
