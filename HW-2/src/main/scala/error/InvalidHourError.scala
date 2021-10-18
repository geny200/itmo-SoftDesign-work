package error

object InvalidHourError extends PostStatisticError {
  override def message: String = "The number of hours must be greater than zero"
}
