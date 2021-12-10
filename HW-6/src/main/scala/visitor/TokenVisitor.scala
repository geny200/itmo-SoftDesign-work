package visitor

import error.visitor.VisitorError
import token.{Brace, NumberToken, Operation, Token}

trait TokenVisitor {
  def visit(token: NumberToken): Either[VisitorError, TokenVisitor]
  def visit(token: Brace): Either[VisitorError, TokenVisitor]
  def visit(token: Operation): Either[VisitorError, TokenVisitor]

  def visitAll(tokens: Seq[Token]): Either[VisitorError, TokenVisitor] =
    tokens
      .foldLeft[Either[VisitorError, TokenVisitor]](
        Right(this)
      )((visitor, token) => visitor.flatMap(token.accept))
}
