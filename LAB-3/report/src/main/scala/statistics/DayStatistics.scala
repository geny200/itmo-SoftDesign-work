package statistics

import cats.implicits.catsSyntaxApplicativeId
import model.{DayOfWeekAvg, WeekDay}

import java.time.{DayOfWeek, LocalDateTime}

case class DayStatistics private (days: Map[DayOfWeek, Long], lastUpdate: LocalDateTime) {
  def increaseByDay(day: DayOfWeek, lastUpdate: LocalDateTime): DayStatistics =
    DayStatistics(days.updatedWith(day)(_.map(_.+(1)).getOrElse(1L).pure[Option]), lastUpdate)

  def increase(newDays: Seq[LocalDateTime], lastUpdate: LocalDateTime): DayStatistics =
    newDays.map(_.getDayOfWeek).foldLeft(this) { (stat, day) =>
      stat.increaseByDay(day, lastUpdate)
    }

  def avgByDay(dayOfWeek: WeekDay): DayOfWeekAvg =
    DayOfWeekAvg(
      days
        .get(dayOfWeek.toJava)
        .map(count => count.toDouble / days.values.sum.toDouble)
        .getOrElse(0)
    )
}

object DayStatistics {
  def apply(): DayStatistics =
    DayStatistics(Map.empty, LocalDateTime.MIN)
}
