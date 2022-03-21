package cache

import cats.Monad
import cats.effect.{Async, Ref}
import cats.implicits.{toFlatMapOps, toFunctorOps}
import InMemoryCacheDB.{calculateDayOfWeek, calculateDuration, calculatePass, calculateVisits}
import event.{Event, UpdatePass, UseInPass, UseOutPass}
import statistics.{DayStatistics, VisitStatistics}
import store.GetEventLogDB
import time.LocalTime

import java.time._

class InMemoryCacheDB[F[_]: Monad: time.LocalTime: GetEventLogDB](
    localVisitStorage: Ref[F, VisitStatistics],
    localDayStorage: Ref[F, DayStatistics]
) extends StatisticsDB[F] {
  override def getVisitsStatistic: F[VisitStatistics] =
    for {
      currentStat <- localVisitStorage.get
      newEvents   <- GetEventLogDB[F].getWithFilter(_.time.isAfter(currentStat.lastUpdate))
      currentTime <- time.LocalTime[F].localDate

      newStat <- localVisitStorage.updateAndGet(
        _.increase(
          calculatePass(newEvents),
          calculateVisits(newEvents),
          calculateDuration(newEvents, currentTime),
          currentTime
        )
      )
    } yield newStat

  override def getDayStatistic: F[DayStatistics] =
    for {
      currentStat <- localDayStorage.get
      newEvents   <- GetEventLogDB[F].getWithFilter(_.time.isAfter(currentStat.lastUpdate))
      currentTime <- time.LocalTime[F].localDate

      newStat <- localDayStorage.updateAndGet(
        _.increase(
          calculateDayOfWeek(newEvents),
          currentTime
        )
      )
    } yield newStat
}

object InMemoryCacheDB {
  def apply[F[_]: Async: LocalTime](getEventLogDB: GetEventLogDB[F]): F[StatisticsDB[F]] = {
    implicit val eventStore: GetEventLogDB[F] = getEventLogDB

    for {
      visits <- Ref.of[F, VisitStatistics](VisitStatistics())
      days   <- Ref.of[F, DayStatistics](DayStatistics())
    } yield new InMemoryCacheDB(visits, days)
  }

  private def calculateDuration(events: Seq[Event], currentTime: LocalDateTime): Duration =
    Duration.ofSeconds(events.flatMap {
      case UseInPass(_, time) =>
        Some(currentTime.toEpochSecond(ZoneOffset.UTC) - time.toEpochSecond(ZoneOffset.UTC))
      case UseOutPass(_, time) =>
        Some(time.toEpochSecond(ZoneOffset.UTC) - currentTime.toEpochSecond(ZoneOffset.UTC))
      case _ => None
    }.sum)

  private def calculateVisits(events: Seq[Event]): Long =
    events.count {
      case _: UseInPass => true
      case _            => false
    }

  private def calculatePass(events: Seq[Event]): Long =
    events.count {
      case UpdatePass(_, time, expiration) if time.toLocalDate == expiration => true
      case _                                                                 => false
    }

  private def calculateDayOfWeek(events: Seq[Event]): Seq[LocalDateTime] =
    events
      .filter {
        case _: UseInPass => true
        case _            => false
      }
      .map(_.time)
}
