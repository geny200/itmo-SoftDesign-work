import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import service.reddit.RedditPostStatistic

class PostStatisticSpec
  extends AnyFlatSpec
    with Matchers
    with MockFactory
    with PostStatisticBehaviors {

  behavior of "Reddit post statistic"
  it should behave like validPostStatistic(new RedditPostStatistic(_))
}