package clock

import java.time.{Clock, Instant, ZoneId}

trait MyClock extends Clock {
  override def withZone(zone: ZoneId): Clock = {
    val current: Clock = this
    new MyClock {
      override def getZone: ZoneId = zone

      override def instant(): Instant = current.instant
    }
  }
}
