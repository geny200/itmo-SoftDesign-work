package statistic

import scala.concurrent.duration.DurationInt

case class Event(name: String, count: Int) {
  def rpm: Double =
    count.toDouble / 1.hours.toMinutes
}
