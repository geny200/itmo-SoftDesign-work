package error.tokenizer

case class UnexpectedCharacterError(symbol: Char) extends TokenizerError {
  override def message: String = s"Unexpected character - $symbol"
}
