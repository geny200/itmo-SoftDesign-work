package error.visitor

object ToManyArgumentsError extends VisitorError {
  override def message: String = "To many arguments"
}
