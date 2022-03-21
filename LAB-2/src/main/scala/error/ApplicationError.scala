package error

import sttp.tapir.Schema
import tethys.derivation.auto.jsonReaderMaterializer
import tethys.{JsonReader, JsonWriter}

sealed trait ApplicationError {
  def errorMessage: String
}

case object UserNotFoundError extends ApplicationError {
  override def errorMessage: String = "User not found"
}

case object UserAlreadyExistError extends ApplicationError {
  override def errorMessage: String = "User already exist"
}

case object ProductFoundError extends ApplicationError {
  override def errorMessage: String = "Product not found"
}

case object ProductAlreadyExistError extends ApplicationError {
  override def errorMessage: String = "Product already exist"
}

object ApplicationError {
  implicit val applicationErrorReader: JsonReader[ApplicationError] =
    implicitly[JsonReader[ApplicationError]]
  implicit val applicationErrorWriter: JsonWriter[ApplicationError] =
    JsonWriter
      .obj[ApplicationError]
      .addField("errorMessage")(_.errorMessage)

  implicit val applicationErrorSchema: Schema[ApplicationError] =
    Schema
      .derived[ApplicationError]
      .description("Данные с информацией об ошибке")
}
