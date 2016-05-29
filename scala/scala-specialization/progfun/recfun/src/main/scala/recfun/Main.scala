package recfun

import scala.annotation.tailrec

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
    * Exercise 1
    */
  def pascal(c: Int, r: Int): Int = {
    r match {
      case 0 => 1
      case _ =>
        c match {
          case col if col == 0 => pascal(c, r - 1)
          case col if col == r => pascal(c - 1, r - 1)
          case _ => pascal(c - 1, r - 1) + pascal(c, r - 1)
        }
    }
  }

  /**
    * Exercise 2
    */
  def balance(chars: List[Char]): Boolean = {

    @tailrec
    def balanceCount(chars: List[Char], count: Int): Boolean = {
      count match {
        case c if c < 0 => false
        case _ =>
          chars match {
            case chs if chs.isEmpty => count == 0
            case _ =>
              chars.head match {
                case '(' => balanceCount(chars.tail, count + 1)
                case ')' => balanceCount(chars.tail, count - 1)
                case _ => balanceCount(chars.tail, count)
              }
          }
      }
    }
    balanceCount(chars, 0)
  }

  /**
    * Exercise 3
    */
  
  def countChange(money: Int, coins: List[Int]): Int = {

    money match {
      case 0 => 1
      case m if m < 0 => 0
      case m if m > 0 =>
        (for (coin <- coins)
          yield countChange(money - coin,
            coins.slice(coins.indexOf(coin), coins.length)
          )).sum
    }

  }
}
