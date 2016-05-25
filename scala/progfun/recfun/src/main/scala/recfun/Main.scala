package recfun
import common._

object Main {
  def main(args: Array[String]) {
    println("Ways = " + countChange(300,List(5,10,20,50,100,200,500)))
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c==0 && r==0) 1
    else if (c==0) pascal(0,r-1)
    else if (c==r) pascal(c-1,r-1)
    else pascal(c-1,r-1) + pascal(c,r-1)

  }


  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    def diff (count: Int, tail: List[Char]): Int = {
      if (count<0 || tail.isEmpty)
        count
      else if (tail.head == '(')
        diff(count+1,tail.tail)
      else if (tail.head == ')')
        diff(count-1,tail.tail)
      else diff(count,tail.tail)
    }
    diff(0,chars) == 0
  }

  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = {

    def addLeaf(sum: Int, coinsTail: List[Int]): Int = {

      def addCoin(tail: List[Int]): Int = {
        if (tail.isEmpty)
          0
        else
          addLeaf(sum+tail.head,tail) + addCoin(tail.tail)
      }

      if (coinsTail.isEmpty || sum>money)
        0
      else if (sum==money)
        1
      else
        addLeaf(sum+coinsTail.head,coinsTail) + addCoin(coinsTail.tail)
    }

    if (coins.isEmpty || money==0)
      0
    else addLeaf(0,coins)
}
}
