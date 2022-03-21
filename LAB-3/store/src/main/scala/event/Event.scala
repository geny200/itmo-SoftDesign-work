package event

import model.PassId
import tethys.derivation.semiauto.{jsonReader, jsonWriter}
import tethys.readers.FieldName
import tethys.readers.tokens.TokenIterator
import tethys.writers.tokens.TokenWriter
import tethys.{JsonObjectWriter, JsonReader, JsonWriter}

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, LocalDateTime, ZoneId}

sealed trait Event {
  def passId: PassId
  def time: LocalDateTime
}

final case class UpdatePass(passId: PassId, time: LocalDateTime, expiration: LocalDate)
    extends Event
final case class UseInPass(passId: PassId, time: LocalDateTime)  extends Event
final case class UseOutPass(passId: PassId, time: LocalDateTime) extends Event

object Event {
  private val formatterTime: DateTimeFormatter =
    DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())

  private val formatterDate: DateTimeFormatter =
    DateTimeFormatter.ISO_LOCAL_DATE.withZone(ZoneId.systemDefault())

  implicit val localDateTimeWriter: JsonWriter[LocalDateTime] = new JsonWriter[LocalDateTime] {
    override def write(value: LocalDateTime, tokenWriter: TokenWriter): Unit =
      tokenWriter.writeString(value.format(formatterTime))
  }

  implicit val localDateTimeReader: JsonReader[LocalDateTime] = new JsonReader[LocalDateTime] {
    override def read(it: TokenIterator)(implicit fieldName: FieldName): LocalDateTime =
      LocalDateTime.parse(it.string(), formatterDate)
  }

  implicit val localDateWriter: JsonWriter[LocalDate] = new JsonWriter[LocalDate] {
    override def write(value: LocalDate, tokenWriter: TokenWriter): Unit =
      tokenWriter.writeString(value.format(formatterDate))
  }

  implicit val localDateReader: JsonReader[LocalDate] = new JsonReader[LocalDate] {
    override def read(it: TokenIterator)(implicit fieldName: FieldName): LocalDate =
      LocalDate.parse(it.string(), formatterDate)
  }
}
