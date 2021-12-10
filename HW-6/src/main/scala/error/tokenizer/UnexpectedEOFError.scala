package error.tokenizer

object UnexpectedEOFError extends TokenizerError {
  override def message: String = s"Unexpected character eof"
}
