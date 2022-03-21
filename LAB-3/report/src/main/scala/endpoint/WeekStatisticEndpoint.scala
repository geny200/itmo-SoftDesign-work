package endpoint

import cats.implicits.{catsSyntaxEitherId, toFunctorOps}
import endpoint.StatisticEndpoint.statisticEndpoint
import error.ApplicationError
import model.{DayOfWeekAvg, WeekDay}
import module.{FunctorModule, ReportModule}
import sttp.tapir.json.tethysjson.jsonBody
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.{PublicEndpoint, query}

import java.time.DayOfWeek

trait WeekStatisticEndpoint[F[_], R] {
  def weekEndpoint: ServerEndpoint[R, F]
}

trait WeekStatisticEndpointImpl[F[_], R] extends WeekStatisticEndpoint[F, R] {
  this: ReportModule[F] with FunctorModule[F] =>

  private lazy val week: PublicEndpoint[WeekDay, ApplicationError, DayOfWeekAvg, Any] =
    statisticEndpoint
      .in("week")
      .in(
        query[String]("day")
          .map(day => WeekDay(DayOfWeek.valueOf(day)))(weekDay => weekDay.toJava.toString)
          .description("День недели")
          .example(WeekDay.example)
      )
      .out(
        jsonBody[DayOfWeekAvg]
          .description("Статистика посещений по дням")
          .example(DayOfWeekAvg.example)
      )
      .description("Получить статистику посещений за выбранный день недели")

  private def logic(weekDay: WeekDay): F[Either[ApplicationError, DayOfWeekAvg]] =
    reportClient.getStatByDay(weekDay).map(_.asRight)

  override lazy val weekEndpoint: ServerEndpoint[R, F] =
    week.serverLogic(logic)
}
