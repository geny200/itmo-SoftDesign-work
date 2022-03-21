package model

import sttp.tapir.Schema
import tethys.derivation.semiauto.{jsonReader, jsonWriter}
import tethys.{JsonReader, JsonWriter}

final case class PassId(id: Long)

object PassId {
  implicit val passIdWriter: JsonWriter[PassId] = jsonWriter
  implicit val passIdReader: JsonReader[PassId] = jsonReader

  implicit val subjectSchema: Schema[PassId] =
    Schema
      .derived[PassId]
      .description("Идентификатор абонемента")

  val example: PassId = PassId(42)
}
