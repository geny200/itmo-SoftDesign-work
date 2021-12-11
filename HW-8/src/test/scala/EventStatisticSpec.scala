import clock.StableClock
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import statistic.{Event, EventsStatistic, MapEventsStatistic}

import java.time.{Instant, ZoneId}

class EventStatisticSpec extends AnyFlatSpec with Matchers with MockFactory {

  trait TestClock {
    val baseInstant: Instant = Instant.parse("2007-10-03T12:00:00.00Z")
    val clock: StableClock = StableClock(baseInstant, ZoneId.of("Z"))
    val statistic: EventsStatistic = MapEventsStatistic(clock, Map(), 60)

    val eventName1 = "one"
    val eventName2 = "two"
  }

  "Statistic" should "be empty" in new TestClock {
    statistic.getAllEventStatistic.isEmpty shouldEqual true
  }

  it should "be empty for new event" in new TestClock {
    statistic
      .getEventStatisticByName(eventName1)
      .count shouldEqual 0
  }

  it should "not be empty for an existing event" in new TestClock {
    statistic
      .incEvent(eventName1)
      .getEventStatisticByName(eventName1)
      .count shouldEqual 1
  }

  it should "return correct rpm for one event" in new TestClock {
    statistic
      .incEvent(eventName1)
      .getEventStatisticByName(eventName1)
      .rpm shouldEqual (1d / 60d)
  }

  it should "return correct rpm for many events" in new TestClock {
    statistic
      .incEvent(eventName2)
      .incEvent(eventName2)
      .incEvent(eventName1)
      .getEventStatisticByName(eventName2)
      .rpm shouldEqual (2d / 60d)
  }

  it should "return all events" in new TestClock {
    val allEvents: Iterable[Event] = statistic
      .incEvent(eventName2)
      .incEvent(eventName2)
      .incEvent(eventName1)
      .getAllEventStatistic

    allEvents.find(_.name == eventName1) shouldEqual Some(Event(eventName1, 1))
    allEvents.find(_.name == eventName2) shouldEqual Some(Event(eventName2, 2))
    allEvents.size shouldEqual 2
  }

  it should "return zeroes rpm for period more than hour" in new TestClock {
    val stat: EventsStatistic = statistic
      .incEvent(eventName2)
      .incEvent(eventName2)
      .incEvent(eventName1)

    // don't do like that in Scala, better use mock[Clock]
    clock.setInstances(baseInstant.plusSeconds(60 * 60))

    val allEvents: Iterable[Event] = stat.getAllEventStatistic

    allEvents.find(_.name == eventName1) shouldEqual Some(Event(eventName1, 0))
    allEvents.find(_.name == eventName2) shouldEqual Some(Event(eventName2, 0))
    allEvents.size shouldEqual 2
  }
}
