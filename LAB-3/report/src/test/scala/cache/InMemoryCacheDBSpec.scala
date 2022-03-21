package cache

import cats.effect.Ref
import event.{UpdatePass, UseInPass}
import model.PassId
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import statistics.VisitStatistics
import store.InMemoryDB
import time.LocalTime
import zio.Runtime.default.unsafeRun
import zio.Task
import zio.interop.catz.asyncRuntimeInstance
import zio.interop.catz.implicits.rts

import java.time.{Duration, LocalDateTime}

class InMemoryCacheDBSpec extends AnyFlatSpec with Matchers {
  "cache" should "update from event store" in {
    unsafeRun(for {
      db         <- InMemoryDB[Task]()
      cache      <- InMemoryCacheDB[Task](db)
      nullVisits <- cache.getVisitsStatistic
      _          <- db.addEvent(UpdatePass(passId0, localTime, localTime.toLocalDate))
      _          <- db.addEvent(UseInPass(passId0, localTime))
      oneVisits  <- cache.getVisitsStatistic
    } yield {
      nullVisits shouldBe VisitStatistics().increase(0, 0, Duration.ZERO, startTime)
      oneVisits shouldBe VisitStatistics().increase(1, 1, Duration.ZERO, localTime)
    })
  }

  private val count: Ref[Task, Boolean] = Ref.unsafe[Task, Boolean](true)

  implicit val timeInstance: LocalTime[Task] = new LocalTime[Task] {
    override def localDate: Task[LocalDateTime] =
      for {
        flag <- count.get
        time <- Task.apply(if (flag) startTime else localTime)
        _    <- count.update(_ => false)
      } yield time
  }

  private lazy val startTime = LocalDateTime.parse("2007-12-03T10:15:30")
  private lazy val localTime = LocalDateTime.parse("2007-12-03T10:16:30")
  private lazy val passId0   = PassId(0)
}
