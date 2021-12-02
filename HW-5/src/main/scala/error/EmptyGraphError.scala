package error

object EmptyGraphError extends AppError {
  override def message: String = "Empty graph"
}
