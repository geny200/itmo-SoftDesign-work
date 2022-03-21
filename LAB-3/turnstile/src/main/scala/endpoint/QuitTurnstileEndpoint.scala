package endpoint

import cats.implicits.{catsSyntaxEitherId, toFunctorOps}
import endpoint.TurnstileEndpoint.turnstileEndpoint
import error.ApplicationError
import model.PassId
import module.{FunctorModule, TurnstileModule}
import sttp.tapir.PublicEndpoint
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.server.ServerEndpoint
import tethys.readers.FieldName
import tethys.readers.tokens.TokenIterator
import tethys.writers.tokens.TokenWriter
import tethys.{JsonReader, JsonWriter}

trait QuitTurnstileEndpoint[F[_], R] {
  def quitEndpoint: ServerEndpoint[R, F]
}

trait QuitTurnstileEndpointImpl[F[_], R] extends QuitTurnstileEndpoint[F, R] {
  this: TurnstileModule[F] with FunctorModule[F] =>

  private lazy val quit: PublicEndpoint[PassId, ApplicationError, Unit, Any] =
    turnstileEndpoint.post
      .in("quit")
      .in(
        jsonBody[PassId]
          .description("Идентификатор абонемента")
          .example(PassId.example)
      )
      .out(
        jsonBody[Unit]
          .example(())
          .description("Выход произведён")
      )
      .description("Записать выход по идентификатору абонемента")

  private def logic(
      passId: PassId
  ): F[Either[ApplicationError, Unit]] =
    turnstileService.quit(passId).map(_.asRight[ApplicationError])

  override lazy val quitEndpoint: ServerEndpoint[R, F] =
    quit.serverLogic(logic)

  private implicit val unitWriter: JsonWriter[Unit] = (_: Unit, _: TokenWriter) => ()
  private implicit val unitReader: JsonReader[Unit] = new JsonReader[Unit] {
    override def read(it: TokenIterator)(implicit fieldName: FieldName): Unit = ()
  }
}
