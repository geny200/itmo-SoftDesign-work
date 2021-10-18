package service.reddit

import error.{InvalidStringError, ParseError}
import org.json4s.jackson.JsonMethods
import service.rest.RestGetApi
import service.statistic.{RestPostStatistic, TimeInterval}

class RedditPostStatistic(getApi: RestGetApi)
  extends RestPostStatistic {

  override def parse(response: String): Either[ParseError, Int] =
    try {
      for {
        head <- JsonMethods
          .parse(response)
          .toOption
          .toRight(InvalidStringError)
      } yield head.children
        .map(_.children.length)
        .sum
    } catch {
      case _: com.fasterxml.jackson.core.JacksonException =>
        Left(InvalidStringError)
    }

  override def toRequest(tag: String, interval: TimeInterval): String =
    s"reddit/search/submission/?" +
      s"q=$tag&" +
      s"after=${interval.from}h&" +
      s"before=${interval.to}h&"

  override protected def restGet: RestGetApi = getApi

}
