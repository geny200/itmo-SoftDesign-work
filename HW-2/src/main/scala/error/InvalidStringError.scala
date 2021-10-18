package error

object InvalidStringError extends ParseError {
  override def message: String = "The string is not json"
}
