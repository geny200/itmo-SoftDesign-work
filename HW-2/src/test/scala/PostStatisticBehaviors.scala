import error.{ConnectionError, InvalidHourError, InvalidStringError}
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import service.rest.RestGetApi
import service.statistic.{RestPostStatistic, TimeInterval}

trait PostStatisticBehaviors {
  this: AnyFlatSpec with Matchers with MockFactory =>

  def validPostStatistic(testedPostStatistic: RestGetApi => RestPostStatistic): Unit = {
    trait TestErrorPostStatistic {
      val tag = "rome"
      val interval: TimeInterval = new TimeInterval(0)
      val restMock: RestGetApi = mock[RestGetApi]

      val postStatistic: RestPostStatistic = testedPostStatistic(restMock)
    }

    it should
      "return an error when the hours is negative" in
      new TestErrorPostStatistic {
        postStatistic.get("1242", -1) shouldEqual Left(InvalidHourError)
      }

    it should
      "return an error when the hours is zero" in
      new TestErrorPostStatistic {
        postStatistic.get("1242", 0) shouldEqual Left(InvalidHourError)
      }

    it should
      "return an error when the hours grater than 24" in
      new TestErrorPostStatistic {
        postStatistic.get("1242", 25) shouldEqual Left(InvalidHourError)
      }

    it should
      "return an error for rest api error" in
      new TestErrorPostStatistic {
        (restMock.get _)
          .expects(postStatistic.toRequest(tag, interval))
          .returns(Left(ConnectionError))
          .once()

        postStatistic.get(tag, 1) shouldEqual Left(ConnectionError)
      }

    it should
      "return an error for parse error" in
      new TestErrorPostStatistic {
        (restMock.get _)
          .expects(postStatistic.toRequest(tag, interval))
          .returns(Right(""))
          .once()

        postStatistic.get(tag, 1) shouldEqual Left(InvalidStringError)
      }
  }
}