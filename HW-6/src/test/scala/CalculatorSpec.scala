import error.tokenizer.UnexpectedEOFError
import executor.{CalcResult, Calculator}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CalculatorSpec extends AnyFlatSpec with Matchers {
  "Calculator" should "return error for empty input" in {
    Calculator.evaluate("") shouldEqual Left(UnexpectedEOFError)
  }

  it should "return error for invalid input" in {
    Calculator.evaluate("abc").isLeft shouldEqual true
  }

  it should "return error for invalid input with valid prefix" in {
    Calculator.evaluate("2 + 2 + abc").isLeft shouldEqual true
  }

  it should "return error for invalid expression (missing operand)" in {
    Calculator.evaluate("2 + 2 + ").isLeft shouldEqual true
  }

  it should "return error for invalid expression (extra bracket)" in {
    Calculator.evaluate("(2 + 2").isLeft shouldEqual true
  }

  it should "calculate 2 + 2" in {
    Calculator.evaluate("2 + 2") shouldEqual Right(CalcResult("2 2 + ", 4))
  }

  it should "calculate \" 15    +2    \"" in {
    Calculator.evaluate(" 15    +2    ") shouldEqual Right(
      CalcResult("15 2 + ", 17)
    )
  }

  it should "calculate 2 + 2 * 2" in {
    Calculator.evaluate("2 + 2 * 2") shouldEqual Right(
      CalcResult("2 2 2 * + ", 6)
    )
  }

  it should "calculate 1000 + 100 * 2 / 2" in {
    Calculator.evaluate(" 1000 + 100 * 2 / 2 ") shouldEqual Right(
      CalcResult("1000 100 2 * 2 / + ", 1100)
    )
  }

  it should "calculate (1000 + 100) * 2 / 2" in {
    Calculator.evaluate("(1000 + 100) * 2 / 2") shouldEqual Right(
      CalcResult("1000 100 + 2 * 2 / ", 1100)
    )
  }
}
