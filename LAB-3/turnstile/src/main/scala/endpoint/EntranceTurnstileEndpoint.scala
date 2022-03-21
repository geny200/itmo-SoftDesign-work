package endpoint

import cats.implicits.{catsSyntaxEitherId, toFunctorOps}
import endpoint.TurnstileEndpoint.turnstileEndpoint
import error.ApplicationError
import model.PassId
import module.{FunctorModule, TurnstileModule}
import sttp.tapir.PublicEndpoint
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.server.ServerEndpoint

trait EntranceTurnstileEndpoint[F[_], R] {
  def entranceEndpoint: ServerEndpoint[R, F]
}

trait EntranceTurnstileEndpointImpl[F[_], R] extends EntranceTurnstileEndpoint[F, R] {
  this: TurnstileModule[F] with FunctorModule[F] =>

  private lazy val entrance: PublicEndpoint[PassId, ApplicationError, Boolean, Any] =
    turnstileEndpoint.post
      .in("entrance")
      .in(
        jsonBody[PassId]
          .description("Идентификатор абонемента")
          .example(PassId.example)
      )
      .out(
        jsonBody[Boolean]
          .example(true)
          .description("Разрешение на вход")
      )
      .description("Запросить разрешение на вход по идентификатору абонемента")

  private def logic(
      passId: PassId
  ): F[Either[ApplicationError, Boolean]] =
    turnstileService.entrance(passId).map(_.asRight[ApplicationError])

  override lazy val entranceEndpoint: ServerEndpoint[R, F] =
    entrance.serverLogic(logic)
}
