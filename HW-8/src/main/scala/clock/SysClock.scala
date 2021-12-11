package clock

import java.time.{Clock, Instant, ZoneId}

class SysClock extends MyClock {
  override def getZone: ZoneId = Clock.systemUTC().getZone
  override def instant: Instant = Clock.systemUTC().instant()
}
