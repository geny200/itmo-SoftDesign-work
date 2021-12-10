package executor

import error.AppError
import tokenizer.Tokenizer
import visitor.{CalcVisitor, ParserVisitor, PrintVisitor}

object Calculator {
  def evaluate(expression: String): Either[AppError, CalcResult] =
    for {
      tokens <- Tokenizer.parse(expression)

      polishNotation <-
        ParserVisitor()
          .visitAll(tokens)
          .map { case ParserVisitor(stack, polishNotation) =>
            ParserVisitor(stack, polishNotation).result
          }

      resultCalc <-
        CalcVisitor()
          .visitAll(polishNotation)
          .flatMap { case CalcVisitor(stack) => CalcVisitor(stack).result }

      stringExpr <-
        PrintVisitor()
          .visitAll(polishNotation)
          .map { case PrintVisitor(output) => PrintVisitor(output).result }

    } yield CalcResult(stringExpr, resultCalc.number)
}
