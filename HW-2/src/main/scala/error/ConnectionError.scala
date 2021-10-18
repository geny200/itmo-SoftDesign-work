package error

object ConnectionError extends RestError {
  override def message: String = "Connection error"
}
