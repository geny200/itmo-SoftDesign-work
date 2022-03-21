package client

import cache.StatisticsDB
import cats.Functor
import cats.implicits.toFunctorOps
import model.{DayOfWeekAvg, VisitAvg, WeekDay}

trait ReportClient[F[_]] {
  def getStatByDay(day: WeekDay): F[DayOfWeekAvg]
  def getVisitStat: F[VisitAvg]
}

class ReportClientImpl[F[_]: Functor: StatisticsDB] extends ReportClient[F] {

  def getStatByDay(day: WeekDay): F[DayOfWeekAvg] =
    for {
      statistic <- StatisticsDB[F].getDayStatistic
    } yield statistic.avgByDay(day)

  def getVisitStat: F[VisitAvg] =
    for {
      statistic <- StatisticsDB[F].getVisitsStatistic
    } yield statistic.avg
}
