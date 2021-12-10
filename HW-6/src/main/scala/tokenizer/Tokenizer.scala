package tokenizer

import cats.data.StateT
import cats.implicits.{catsSyntaxApply, toSemigroupKOps}
import error.tokenizer.{
  ExpectedEOFError,
  TokenizerError,
  UnexpectedCharacterError,
  UnexpectedEOFError
}
import parser.Parser
import token._

import scala.language.postfixOps

object Tokenizer {

  private def token(
      tokenName: String,
      token: Token
  ): StateT[Either[TokenizerError, *], Iterable[Char], Token] =
    Parser.sequence[Char, Token, TokenizerError](tokenName, token)(
      UnexpectedCharacterError
    )(
      UnexpectedEOFError
    )

  private def digit: StateT[Either[TokenizerError, *], Iterable[Char], Int] =
    Parser
      .satisfy[Char, TokenizerError](_.isDigit)(UnexpectedCharacterError)(
        UnexpectedEOFError
      )
      .map(_ - '0')

  private def number: StateT[Either[TokenizerError, *], Iterable[Char], Token] =
    Parser
      .some(digit)
      .map(_.foldLeft(0)((acc, value) => acc * 10 + value))
      .map(NumberToken)

  private def anyToken
      : StateT[Either[TokenizerError, *], Iterable[Char], Token] =
    token("+", Addition) <+>
      token("-", Subtraction) <+>
      token("*", Multiplication) <+>
      token("/", Division) <+>
      token("(", OpenBracket) <+>
      token(")", CloseBracket) <+>
      number

  def parse(expression: String): Either[TokenizerError, Seq[Token]] =
    (Parser.some(anyToken) <* Parser.eof(ExpectedEOFError))
      .runA(expression.filterNot(_.isWhitespace))
}
