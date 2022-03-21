package model

import sttp.tapir.Schema
import tethys.{JsonReader, JsonWriter}

import java.time.DayOfWeek

sealed trait WeekDay {
  def toJava: DayOfWeek
}

case object MONDAY extends WeekDay {
  override def toJava: DayOfWeek = DayOfWeek.MONDAY
}
case object TUESDAY extends WeekDay {
  override def toJava: DayOfWeek = DayOfWeek.TUESDAY
}
case object WEDNESDAY extends WeekDay {
  override def toJava: DayOfWeek = DayOfWeek.WEDNESDAY
}
case object THURSDAY extends WeekDay {
  override def toJava: DayOfWeek = DayOfWeek.THURSDAY
}
case object FRIDAY extends WeekDay {
  override def toJava: DayOfWeek = DayOfWeek.FRIDAY
}
case object SATURDAY extends WeekDay {
  override def toJava: DayOfWeek = DayOfWeek.SATURDAY
}
case object SUNDAY extends WeekDay {
  override def toJava: DayOfWeek = DayOfWeek.SUNDAY
}

object WeekDay {
  def apply(dayOfWeek: DayOfWeek): WeekDay =
    dayOfWeek match {
      case DayOfWeek.MONDAY    => MONDAY
      case DayOfWeek.TUESDAY   => TUESDAY
      case DayOfWeek.WEDNESDAY => WEDNESDAY
      case DayOfWeek.THURSDAY  => THURSDAY
      case DayOfWeek.FRIDAY    => FRIDAY
      case DayOfWeek.SATURDAY  => SATURDAY
      case DayOfWeek.SUNDAY    => SUNDAY
    }

  val example: WeekDay = MONDAY
}
