package error.tokenizer

object ExpectedEOFError extends TokenizerError {
  override def message: String = s"Expected eof"
}
