package model

import cats.implicits.toFunctorOps
import cats.{Functor, Id}
import sttp.tapir.Schema
import tethys.derivation.semiauto.{jsonReader, jsonWriter}
import tethys.{JsonReader, JsonWriter}

case class CustomizeSubject(
    title: String,
    price: Currency,
    description: String
)

object CustomizeSubject {
  def apply[F[_]: Functor: Convertor](
      subject: Subject,
      currency: BaseCurrency
  ): F[CustomizeSubject] =
    currency
      .fromCoin(subject.price)
      .map(
        CustomizeSubject(
          subject.title,
          _,
          subject.description
        )
      )

  implicit val customizeSubjectWriter: JsonWriter[CustomizeSubject] = jsonWriter
  implicit val customizeSubjectReader: JsonReader[CustomizeSubject] = jsonReader

  implicit val subjectSchema: Schema[CustomizeSubject] =
    Schema
      .derived[CustomizeSubject]
      .description("Товар")
      .encodedExample(example)

  lazy val example: CustomizeSubject = {
    implicit val convertor: Convertor[Id] = Convertor.example
    CustomizeSubject[Id](Subject.example, User.example.currency)
  }
}
