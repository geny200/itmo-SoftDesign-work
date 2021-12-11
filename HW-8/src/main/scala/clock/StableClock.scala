package clock

import java.time.{Instant, ZoneId}

case class StableClock(
    var instant: Instant, // don't do like that in Scala, better use mock[Clock]
    zone: ZoneId
) extends MyClock {
  override def getZone: ZoneId = zone

  def setInstances(newInstant: Instant): Unit =
    instant = newInstant
}
