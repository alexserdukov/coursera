package me.serdukoff.algs.quicksort

import scala.io.Source._

/**
 * Created by alex on 07.02.15.
 */
class QuickSortRoutine {

  var comparisons = 0;

  def sort(list: Array[Int])(implicit pivotF: Array[Int] => Int): Array[Int] = {

    def pivot = list(0)

    def swap(index1: Int)(index2: Int): Unit = index2 match
      {
        case `index1` =>
        case `index2` => {
          val temp1 = list(index1)
          list(index1) = list(index2)
          list(index2) = temp1
        }

      }

    list.length match {
      case 0 => list
      case 1 => list
      case _ => {

        swap(0)(pivotF(list).asInstanceOf[Int])

        var j = 0
        var i = 0
        comparisons = comparisons + list.length - 1

        list foreach { el =>
          if (el < pivot) {
            swap(i+1)(j)
            i = i + 1
          }
          j = j + 1
        }

        swap(0)(i)

        (sort(list.slice(0, i).asInstanceOf[Array[Int]]) :+ list(i)) ++ sort(list.slice(i + 1, list.length).asInstanceOf[Array[Int]])
      }
    }
  }

}

object QuickSortRoutine extends App {

  val toSort: List[Int] = fromURL(getClass.getResource("/QuickSort.txt")).getLines().toList.map(rec => rec.toInt)

  val routine1 = new QuickSortRoutine
  routine1.sort(toSort.toArray)((arr: Array[Int]) => 0) mkString (",")
  println("Pivot is 0 = " + routine1.comparisons)

  val routine2 = new QuickSortRoutine
  routine2.sort(toSort.toArray)((arr: Array[Int]) => arr.length - 1) mkString (",")
  println("Pivot is last = " + routine2.comparisons)

  val routine3 = new QuickSortRoutine

  def median(list: Array[Int]) = {
    val median = if (list.length % 2 == 0) list.length / 2 - 1 else list.length / 2
    val pivotList = List((list(0), 0), (list(median), median), (list(list.length - 1), list.length - 1)).sortWith(_._1 < _._1)
    pivotList(1)._2
  }

  routine3.sort(toSort.toArray)((arr: Array[Int]) => median(arr)) mkString (",")
  println("Pivot is median = " + routine3.comparisons)
}
