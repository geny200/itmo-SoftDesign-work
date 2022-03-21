package database

import model.id.SubjectId
import model.{Coin, Subject}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import zio.Runtime.default.unsafeRun
import zio.Task
import zio.interop.catz.asyncRuntimeInstance
import zio.interop.catz.implicits.rts

class InMemoryDBSpec extends AnyFlatSpec with Matchers {
  "in memory db" should "save new products" in {
    unsafeRun(for {
      db  <- InMemoryDB[Task]()
      _   <- db.addProduct(productCola)
      all <- db.getAllProducts
    } yield all shouldBe Seq(productCola, Subject.example))
  }

  it should "support parallel operations" in {
    unsafeRun(
      (for {
        db <- InMemoryDB[Task]()
        _ <- (
          Task.apply(Thread.sleep(1000))
            *> db.addProduct(productCola)
            *> Task.apply(Thread.sleep(1000))
            *> db.addProduct(productTable)
        ) <&> db.addProduct(productPepsi)
        all <- db.getAllProducts
      } yield all shouldBe Seq(productTable, productCola, productPepsi, Subject.example))
    )
  }

  private lazy val productCola: Subject =
    Subject(SubjectId(42), "Coca-cola", Coin(7800), "Coca-cola 1.5L")

  private lazy val productPepsi: Subject =
    Subject(SubjectId(43), "Pepsi-cola", Coin(5900), "Pepsi-cola 1.5L")

  private lazy val productTable: Subject =
    Subject(SubjectId(44), "Table", Coin(5899000), "Table for school")
}
