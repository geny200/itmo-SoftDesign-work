package error.visitor

object MissingArgumentError extends VisitorError {
  override def message: String = "There is no argument for the operation"
}
