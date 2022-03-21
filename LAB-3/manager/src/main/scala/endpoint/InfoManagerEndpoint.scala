package endpoint

import cats.implicits.toFunctorOps
import endpoint.ManagerEndpoint.managerEndpoint
import error.ApplicationError
import event.Event.{localDateReader, localDateWriter}
import model.PassId
import module.{FunctorModule, ManagerModule}
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.{PublicEndpoint, query}

import java.time.LocalDate

trait InfoManagerEndpoint[F[_], R] {
  def infoEndpoint: ServerEndpoint[R, F]
}

trait InfoManagerEndpointImpl[F[_], R] extends InfoManagerEndpoint[F, R] {
  this: ManagerModule[F] with FunctorModule[F] =>

  private lazy val getInfo: PublicEndpoint[PassId, ApplicationError, LocalDate, Any] =
    managerEndpoint.get
      .in("info")
      .in(
        query[Long]("id")
          .mapTo[PassId]
          .description("Идентификатор абонемента")
          .example(PassId.example)
      )
      .out(
        jsonBody[LocalDate]
          .example(LocalDate.now())
          .description("Дата истечения абонемента")
      )
      .description("Получить информацию о дате истечения абонемента по его идентификатору")

  private def logic(
      passId: PassId
  ): F[Either[ApplicationError, LocalDate]] =
    managerService.info(passId).map(_.toRight(ApplicationError("Pass not found")))

  override lazy val infoEndpoint: ServerEndpoint[R, F] =
    getInfo.serverLogic(logic)
}
