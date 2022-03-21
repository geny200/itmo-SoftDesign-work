package error

import sttp.tapir.Schema
import tethys.derivation.semiauto.{jsonReader, jsonWriter}
import tethys.{JsonReader, JsonWriter}

final case class ApplicationError(errorMessage: String)

object ApplicationError {
  implicit val applicationErrorWriter: JsonWriter[ApplicationError] = jsonWriter
  implicit val applicationErrorReader: JsonReader[ApplicationError] = jsonReader

  implicit val applicationErrorSchema: Schema[ApplicationError] =
    Schema
      .derived[ApplicationError]
      .description("Сообщение об ошибке")
}
