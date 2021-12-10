package error.visitor

object MissingBraceError extends VisitorError {
  override def message: String = "The opening bracket is missing"
}
