package statistic

trait EventsStatistic {
  def incEvent(name: String): EventsStatistic

  def getEventStatisticByName(name: String): Event
  def getAllEventStatistic: Iterable[Event]
  def printStatistic(): Unit
}
