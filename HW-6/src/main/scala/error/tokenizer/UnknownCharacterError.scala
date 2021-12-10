package error.tokenizer

case class UnknownCharacterError(symbol: Char) extends TokenizerError {
  override def message: String = s"Unknown character - $symbol"
}
