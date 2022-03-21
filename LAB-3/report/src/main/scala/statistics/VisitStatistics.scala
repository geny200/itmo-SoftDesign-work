package statistics

import model.VisitAvg

import java.time.{Duration, LocalDateTime}

case class VisitStatistics private (
    passes: Long,
    visits: Long,
    duration: Duration,
    lastUpdate: LocalDateTime
) {
  def increase(
      newPasses: Long,
      newVisits: Long,
      newDuration: Duration,
      newLastUpdate: LocalDateTime
  ): VisitStatistics =
    VisitStatistics(
      passes + newPasses,
      visits + newVisits,
      duration.plus(newDuration),
      newLastUpdate
    )

  def avg: VisitAvg =
    if (passes > 0)
      VisitAvg(visits / passes, duration.dividedBy(passes))
    else
      VisitAvg(0, Duration.ZERO)
}

object VisitStatistics {
  def apply(): VisitStatistics =
    VisitStatistics(0, 0, Duration.ZERO, LocalDateTime.MIN)
}
