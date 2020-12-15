package im.tony.ui

import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import tornadofx.*
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.*

public data class GlobalLoggerSetup(
  val setupName: String,
  val overwriteLoggerSettings: Boolean,
  val level: Level? = null,
  val filter: Filter? = null,
) {
  public constructor(
    overwriteLoggerSettings: Boolean,
    level: Level? = null,
    filter: Filter? = null,
  ) : this("Unknown Setup", overwriteLoggerSettings, level, filter)

  public companion object {
    public val Default: GlobalLoggerSetup = GlobalLoggerSetup(
      overwriteLoggerSettings = false,
      setupName = "Default Setup",
      level = Level.OFF,
      filter = null,
    )

    public val Main: GlobalLoggerSetup = GlobalLoggerSetup(
      overwriteLoggerSettings = true,
      setupName = "Main Logger Setup",
      level = Level.ALL,
      filter = null,
    )
  }
}

public object UiServices {
  private val ignoredSenders: List<String> = listOf("javafx")
  private var maxSize: Int = 400
  private var shrinkFactor: Double = 0.35

  public var isInit: Boolean = false
    private set

  public var isStopped: Boolean = true
    private set

  public var logger: Logger by singleAssign(SingleAssignThreadSafetyMode.SYNCHRONIZED)
    private set

  public var messages: ObservableList<String> = observableListOf()
  public var records: ObservableList<LogRecord> = observableListOf()

  private val listener = ListChangeListener<Any?> {
    if (it.next() && it.wasAdded() && it.list.size > maxSize) {
      it.list.remove(0, (maxSize * shrinkFactor).toInt())
    }
  }

  private var handler = object : Handler() {
    private var isClosed: Boolean = false

    private fun addRecord(log: LogRecord): Unit {
      records.add(log)
      messages.add("[${DateTimeFormatter.ISO_INSTANT.format(log.instant)}] ${log.loggerName ?: ""} (TID:${log.threadID}) - ${log.message ?: ""}")
    }

    /**
     * Publish a `LogRecord`.
     *
     *
     * The logging request was made initially to a `Logger` object,
     * which initialized the `LogRecord` and forwarded it here.
     *
     *
     * The `Handler`  is responsible for formatting the message, when and
     * if necessary.  The formatting should include localization.
     *
     * @param  record  description of the log event. A null record is
     * silently ignored and is not published
     */
    override fun publish(record: LogRecord?): Unit {
      if (!isInit || isStopped || isClosed || record == null) {
        return
      }

      if (record.level.intValue() < 501 && record.loggerName.contains("javafx")) {
        return
      }

      addRecord(record)
    }

    /**
     * Flush any buffered output.
     */
    override fun flush(): Unit {}

    /**
     * Close the `Handler` and free all associated resources.
     *
     *
     * The close method will perform a `flush` and then close the
     * `Handler`.   After close has been called this `Handler`
     * should no longer be used.  Method calls may either be silently
     * ignored or may throw runtime exceptions.
     *
     * @exception  SecurityException  if a security manager exists and if
     * the caller does not have `LoggingPermission("control")`.
     */
    override fun close(): Unit {
      isClosed = true
    }
  }

  public fun init(logger: Logger, loggerSetup: GlobalLoggerSetup = GlobalLoggerSetup.Main) {
    if (isInit) {
      return
    }

    isInit = true
    isStopped = false

    val initLogs: MutableList<LogRecord> = mutableListOf()

    initLogs.add(LogRecord(
      Level.CONFIG,
      "UiServices.init called with logger '${logger.name}'. " +
        "Logger ${if (logger.parent == null) "does not" else "does"} have a parent, " +
        "and ${if (logger.useParentHandlers) "does" else "does not"} use it's handlers."
    ).apply { loggerName = "UiServices Init" })

    var l = logger

    while (l.useParentHandlers && l.parent != null) {
      l = l.parent
      initLogs.add(LogRecord(Level.CONFIG, "Found parent logger '${l.name}'.").apply { loggerName = "UiServices Init" })
    }

    this.logger = l
    this.logger.apply {
      if (!loggerSetup.overwriteLoggerSettings) {
        return@apply
      }

      if (loggerSetup.level != null) {
        this.level = loggerSetup.level
      }

      if (loggerSetup.filter != null) {
        this.filter = loggerSetup.filter
      }
    }

    this.records.addListener(listener)
    this.messages.addListener(listener)
    this.logger.addHandler(handler)

    initLogs.add(LogRecord(Level.CONFIG, "UiServices Initialized. Log level is ${this.logger.level}.").apply { loggerName = "UiServices Init" })

    initLogs.forEach { this.logger.log(it) }
  }

  public fun stop() {
    this.records.removeListener(listener)
    this.messages.removeListener(listener)
    logger.removeHandler(handler)
    isStopped = true
  }

  public fun log(level: Level, message: String, sender: String? = null): Unit =
    logger.log(LogRecord(level, message).apply { if (sender != null) loggerName = sender })

  public fun log(level: Level, sender: String? = null, message: () -> String): Unit =
    logger.log(LogRecord(level, message.invoke()).apply { if (sender != null) loggerName = sender })

  public fun log(log: LogRecord): Unit = logger.log(log)
}
