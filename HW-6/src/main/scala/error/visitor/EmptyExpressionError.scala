package error.visitor

object EmptyExpressionError extends VisitorError {
  override def message: String = "Empty expression"
}
