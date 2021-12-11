package statistic

import java.time.{Clock, LocalTime}

case class MapEventsStatistic(
    clock: Clock,
    statistic: Map[String, Seq[LocalTime]],
    minutePeriod: Int
) extends EventsStatistic {

  override def incEvent(name: String): MapEventsStatistic = {
    val now: LocalTime = LocalTime.now(clock)
    val eventList =
      statistic
        .get(name)
        .map(filterPeriod(_, now))
        .map(now +: _)
        .getOrElse(Seq(now))

    copy(statistic = statistic.updated(name, eventList))
  }

  override def getEventStatisticByName(name: String): Event =
    statistic
      .get(name)
      .map(filterPeriod(_, LocalTime.now(clock)))
      .map(list => Event(name, list.length))
      .getOrElse(Event(name, 0))

  override def printStatistic(): Unit =
    getAllEventStatistic
      .foreach(event => println(s"${event.name} - ${event.rpm} rpm"))

  override def getAllEventStatistic: Iterable[Event] = {
    val now = LocalTime.now(clock)
    statistic
      .map(tuple =>
        Event(
          tuple._1,
          filterPeriod(tuple._2, now).length
        )
      )
  }

  private def filterPeriod(
      events: Seq[LocalTime],
      now: LocalTime
  ): Seq[LocalTime] =
    events
      .filter(time =>
        time
          .plusMinutes(minutePeriod)
          .isAfter(now)
      )
}

object MapEventsStatistic {
  def apply(clock: Clock): MapEventsStatistic =
    MapEventsStatistic(clock, Map(), 60)
}
