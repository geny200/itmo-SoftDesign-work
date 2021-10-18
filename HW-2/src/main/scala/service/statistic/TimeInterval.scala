package service.statistic

case class TimeInterval private(to: Int, from: Int) {
  def this(to: Int) {
    this(to, to + 1)
  }
}
