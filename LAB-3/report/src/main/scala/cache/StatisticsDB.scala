package cache

import statistics.{DayStatistics, VisitStatistics}

trait StatisticsDB[F[_]] {
  def getVisitsStatistic: F[VisitStatistics]
  def getDayStatistic: F[DayStatistics]
}

object StatisticsDB {
  def apply[F[_]](implicit cache: StatisticsDB[F]): StatisticsDB[F] = cache
}
