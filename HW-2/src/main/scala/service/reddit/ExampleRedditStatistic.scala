package service.reddit

import service.rest.HttpGetByHost

object ExampleRedditStatistic {
  def main(args: Array[String]): Unit = {
    val serv = new RedditPostStatistic(HttpGetByHost("https://api.pushshift.io/"))
    println(serv.get("rome", 3).toString)
  }
}
