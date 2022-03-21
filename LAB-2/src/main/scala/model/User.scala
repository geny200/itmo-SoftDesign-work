package model

import model.id.UserId
import sttp.tapir.Schema
import tethys.derivation.semiauto.{jsonReader, jsonWriter}
import tethys.{JsonReader, JsonWriter}

case class User(
    userId: UserId,
    login: String,
    name: String,
    currency: BaseCurrency
)

object User {
  implicit val userWriter: JsonWriter[User] = jsonWriter
  implicit val userReader: JsonReader[User] = jsonReader

  implicit val userSchema: Schema[User] =
    Schema
      .derived[User]
      .description("Пользователь")

  val example: User = User(UserId.example, "geny200", "Eugene", Euro)
}
