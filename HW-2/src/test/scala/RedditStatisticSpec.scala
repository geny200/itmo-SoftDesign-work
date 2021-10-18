import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.Action
import com.xebialabs.restito.server.StubServer
import data.{Post, Root}
import error.BaseError
import org.json4s.jackson.JsonMethods.{pretty, render}
import org.json4s.jackson.Serialization
import org.json4s.{Extraction, Formats, FullTypeHints}
import org.scalamock.scalatest.MockFactory
import org.scalatest.Status
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import service.reddit.RedditPostStatistic
import service.rest.HttpGetByHost
import service.statistic.RestPostStatistic

class RedditStatisticSpec
  extends AnyFlatSpec
    with Matchers
    with MockFactory {

  class TestStubPostStatistic(port: Int) {
    val server = new StubServer(port)
    val tag = "rome"

    val postStatistic: RestPostStatistic =
      new RedditPostStatistic(HttpGetByHost(s"http://localhost:$port/"))

    implicit val formats: AnyRef with Formats = {
      Serialization.formats(FullTypeHints(List(classOf[Status])))
    }

    val responseData: Root = Root(List(Post("title", "body")))
    val responseJson: String = pretty(render(Extraction.decompose(responseData)))
    whenHttp(server).`match`().`then`(Action.stringContent(s"$responseJson"))
  }

  "Reddit post statistic" should "call api" in new TestStubPostStatistic(8089) {
    server.run()
    val response: Either[BaseError, List[Int]] = postStatistic.get(tag, 1)
    server.stop()

    response shouldEqual Right(List(1))
  }

  it should "return list for range" in new TestStubPostStatistic(8089) {
    server.run()
    val response: Either[BaseError, List[Int]] = postStatistic.get(tag, 4)
    server.stop()

    response shouldEqual Right(List(1, 1, 1, 1))
  }

  it should "calculate the sum for many posts in the hour" in new TestStubPostStatistic(8089) {
    override val responseJson: String =
      pretty(render(
        Extraction.decompose(
          Root(
            List(Post("title1", "body1"),
              Post("title2", "body2"))
          ))
      ))
    whenHttp(server).`match`().`then`(Action.stringContent(s"$responseJson"))

    server.run()
    val response: Either[BaseError, List[Int]] = postStatistic.get(tag, 4)
    server.stop()

    response shouldEqual Right(List(2, 2, 2, 2))
  }
}