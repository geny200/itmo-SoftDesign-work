package endpoint

import cats.implicits.{catsSyntaxEitherId, toFunctorOps}
import endpoint.StatisticEndpoint.statisticEndpoint
import error.ApplicationError
import model.VisitAvg
import module.{FunctorModule, ReportModule}
import sttp.tapir.PublicEndpoint
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.server.ServerEndpoint

trait VisitStatisticEndpoint[F[_], R] {
  def visitEndpoint: ServerEndpoint[R, F]
}

trait VisitStatisticEndpointImpl[F[_], R] extends VisitStatisticEndpoint[F, R] {
  this: ReportModule[F] with FunctorModule[F] =>

  private lazy val visits: PublicEndpoint[Unit, ApplicationError, VisitAvg, Any] =
    statisticEndpoint
      .in("visits")
      .out(
        jsonBody[VisitAvg]
          .description("Статистика посещений на человека")
          .example(VisitAvg.example)
      )
      .description("Получить статистику посещений")

  private def logic(): F[Either[ApplicationError, VisitAvg]] =
    reportClient.getVisitStat.map(x => x.asRight)

  override lazy val visitEndpoint: ServerEndpoint[R, F] =
    visits.serverLogic(_ => logic())
}
