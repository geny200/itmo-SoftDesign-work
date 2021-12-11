import clock.SysClock
import statistic.{EventsStatistic, MapEventsStatistic}

object Example extends App {
  val eventStatistic: EventsStatistic = MapEventsStatistic(new SysClock())
    .incEvent("hello")
    .incEvent("hello")
    .incEvent("world")

  val eventHello = eventStatistic.getEventStatisticByName("hello")

  println(
    s"${eventHello.name} - count = ${eventHello.count}; ${eventHello.rpm} rpm "
  )

  println("All statistic:")
  eventStatistic.printStatistic()
}
