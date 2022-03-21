package endpoint

import cats.implicits.{catsSyntaxEitherId, toFunctorOps}
import endpoint.ManagerEndpoint.managerEndpoint
import error.ApplicationError
import model.{ExtendRequest, PassId}
import module.{FunctorModule, ManagerModule}
import sttp.tapir.PublicEndpoint
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.server.ServerEndpoint

import java.time.LocalDate

trait ExtendManagerEndpoint[F[_], R] {
  def extendEndpoint: ServerEndpoint[R, F]
}

trait ExtendManagerEndpointImpl[F[_], R] extends ExtendManagerEndpoint[F, R] {
  this: ManagerModule[F] with FunctorModule[F] =>

  private lazy val extend: PublicEndpoint[(ExtendRequest), ApplicationError, Unit, Any] =
    managerEndpoint.post
      .in("extend")
      .in(
        jsonBody[ExtendRequest]
          .description("Идентификатор абонемента и новая дата истечения")
          .example(ExtendRequest.example)
      )
      .description("Продлить абонемент")

  private def logic(
      passId: PassId,
      expiration: LocalDate
  ): F[Either[ApplicationError, Unit]] =
    managerService.extend(passId, expiration).map(_.asRight)

  override lazy val extendEndpoint: ServerEndpoint[R, F] =
    extend.serverLogic(extendRequest => logic(extendRequest.passId, extendRequest.expiration))
}
