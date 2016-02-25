import static ch.qos.logback.classic.Level.*
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.status.NopStatusListener

statusListener(NopStatusListener)

appender("main", ConsoleAppender) {
  encoder(PatternLayoutEncoder) { pattern = "%d %-5level %logger{35} - %msg%n" }
}

root(WARN, ["main"])
logger("pie", INFO, ["main"], false)
