package endpoint

import cats.implicits.{catsSyntaxEitherId, toFunctorOps}
import endpoint.ManagerEndpoint.managerEndpoint
import error.ApplicationError
import model.PassId
import module.{FunctorModule, ManagerModule}
import sttp.tapir.PublicEndpoint
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.server.ServerEndpoint

trait CreateManagerEndpoint[F[_], R] {
  def createEndpoint: ServerEndpoint[R, F]
}

trait CreateManagerEndpointImpl[F[_], R] extends CreateManagerEndpoint[F, R] {
  this: ManagerModule[F] with FunctorModule[F] =>

  private lazy val create: PublicEndpoint[PassId, ApplicationError, Unit, Any] =
    managerEndpoint.post
      .in("create")
      .in(
        jsonBody[PassId]
          .description("Идентификатор абонемента")
          .example(PassId.example)
      )
      .description("Создать абонемент")

  private def logic(
      passId: PassId
  ): F[Either[ApplicationError, Unit]] =
    managerService.create(passId).map(_.asRight)

  override lazy val createEndpoint: ServerEndpoint[R, F] =
    create.serverLogic(logic)
}
