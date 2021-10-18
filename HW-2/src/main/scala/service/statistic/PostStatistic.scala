package service.statistic

import error.BaseError

/**
 * Trait for getting statistics of posts.
 *
 * @author Geny200
 */
trait PostStatistic {
  /**
   * Returns the number of posts in each hour in range [1; hours].
   *
   * @param tag   Hashtag that will be used for the search.
   * @param hours The number of hours for which you need to find a post chart.
   *              [[Int]] value in the range [1, 24],
   */
  def get(tag: String, hours: Int): Either[BaseError, List[Int]]
}
