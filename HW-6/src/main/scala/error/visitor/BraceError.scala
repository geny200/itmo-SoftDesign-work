package error.visitor

object BraceError extends VisitorError {
  override def message: String = "Brackets cannot be visited"
}
