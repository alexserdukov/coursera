package calculator

sealed abstract class Expr

final case class Literal(v: Double) extends Expr

final case class Ref(name: String) extends Expr

final case class Plus(a: Expr, b: Expr) extends Expr

final case class Minus(a: Expr, b: Expr) extends Expr

final case class Times(a: Expr, b: Expr) extends Expr

final case class Divide(a: Expr, b: Expr) extends Expr

object Calculator {
  def computeValues(implicit namedExpressions: Map[String, Signal[Expr]]): Map[String, Signal[Double]] = {

    namedExpressions.map {
      case (key, value) =>
        (key, Signal {
          eval(value())
        })
    }.seq
  }

  def eval(expr: Expr)(implicit references: Map[String, Signal[Expr]]): Double = {
    expr match {
      case Literal(x) => x
      case Ref(s) => eval(getReferenceExpr(s))(references - s)
      case Plus(e1, e2) => eval(e1) + eval(e2)
      case Minus(e1, e2) => eval(e1) - eval(e2)
      case Times(e1, e2) => eval(e1) * eval(e2)
      case Divide(e1, e2) => eval(e1) / eval(e2)
      case _ => Double.NaN
    }
  }

  /** Get the Expr for a referenced variables.
    * If the variable is not known, returns a literal NaN.
    */
  private def getReferenceExpr(name: String)(implicit references: Map[String, Signal[Expr]]) = {
    references.get(name).fold[Expr] {
      Literal(Double.NaN)
    } { exprSignal =>
      exprSignal() match {
        case Ref(`name`) => Literal(Double.NaN)
        case _ => exprSignal()
      }

    }
  }
}
