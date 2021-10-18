package service.statistic

import collection.Traverse.traverse
import error._
import service.rest.RestGetApi

/**
 * Trait for getting statistics of posts.
 *
 * @author Geny200
 */
trait RestPostStatistic extends PostStatistic {
  /**
   * Returns the number of posts in each hour in range [1; hours].
   *
   * @param tag   Hashtag that will be used for the search.
   * @param hours The number of hours for which you need to find a post chart.
   *              [[Int]] value in the range [1, 24],
   */
  override def get(tag: String, hours: Int): Either[BaseError, List[Int]] =
    for {
      dayHour <- Either.cond(0 < hours && hours < 24, hours, InvalidHourError)

      responses <- traverse(
        (0 until dayHour)
          .map(new TimeInterval(_))
          .map(toRequest(tag, _))
          .map(restGet.get(_))
      )

      result <- traverse(
        responses.map(parse)
      )
    } yield result

  def toRequest(tag: String, interval: TimeInterval): String

  def parse(response: String): Either[ParseError, Int]

  protected def restGet: RestGetApi
}
