package model.id

import sttp.tapir.Schema
import tethys.derivation.semiauto.{jsonReader, jsonWriter}
import tethys.{JsonReader, JsonWriter}

case class UserId(id: Long)

object UserId {
  implicit val userIdWriter: JsonWriter[UserId] = jsonWriter
  implicit val userIdReader: JsonReader[UserId] = jsonReader

  implicit val userIdSchema: Schema[UserId] =
    Schema
      .derived[UserId]
      .description("Уникальный идентификатор пользователя")

  val example: UserId = UserId(42)
}
