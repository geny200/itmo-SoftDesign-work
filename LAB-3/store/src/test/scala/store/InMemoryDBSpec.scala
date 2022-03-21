package store

import event.{UseInPass, UseOutPass}
import model.PassId
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.Runtime.default.unsafeRun
import zio.Task
import zio.interop.catz.asyncRuntimeInstance
import zio.interop.catz.implicits.rts

import java.time.LocalDateTime

class InMemoryDBSpec extends AnyFlatSpec with Matchers {
  "event store" should "add events" in {
    unsafeRun(for {
      db     <- InMemoryDB[Task]()
      _      <- db.addEvent(UseInPass(passId0, localTime))
      events <- db.getWithFilter(_ => true)
    } yield events shouldBe Seq(UseInPass(passId0, localTime)))
  }

  it should "save order" in {
    unsafeRun(for {
      db     <- InMemoryDB[Task]()
      _      <- db.addEvent(UseInPass(passId0, localTime))
      _      <- db.addEvent(UseOutPass(passId0, localTime))
      events <- db.getWithFilter(_ => true)
    } yield events shouldBe Seq(UseOutPass(passId0, localTime), UseInPass(passId0, localTime)))
  }

  "filter" should "correctly filtered" in {
    unsafeRun(for {
      db     <- InMemoryDB[Task]()
      _      <- db.addEvent(UseInPass(passId0, localTime))
      _      <- db.addEvent(UseOutPass(passId1, localTime))
      events <- db.getEvents(passId1)
    } yield events shouldBe Seq(UseOutPass(passId1, localTime)))
  }

  private lazy val localTime = LocalDateTime.parse("2007-12-03T10:15:30")
  private lazy val passId0   = PassId(0)
  private lazy val passId1   = PassId(1)
}
