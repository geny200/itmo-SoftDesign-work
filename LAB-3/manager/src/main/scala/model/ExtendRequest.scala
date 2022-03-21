package model

import sttp.tapir.Schema
import tethys.derivation.semiauto.jsonWriter
import tethys.{JsonReader, JsonWriter}

import java.time.LocalDate

case class ExtendRequest(
    passId: PassId,
    expiration: LocalDate
)

object ExtendRequest {
  import event.Event.localDateWriter

  implicit val extendRequestWriter: JsonWriter[ExtendRequest] = jsonWriter
  implicit val extendRequestReader: JsonReader[ExtendRequest] =
    JsonReader.builder
      .addField[PassId]("passId")
      .addField[String]("expiration")
      .buildReader { (passId, localDate) =>
        ExtendRequest(passId, LocalDate.parse(localDate))
      }

  implicit val subjectSchema: Schema[ExtendRequest] =
    Schema
      .derived[ExtendRequest]
      .description("Данные для продления абонемента")

  val example: ExtendRequest = ExtendRequest(PassId.example, LocalDate.now())
}
