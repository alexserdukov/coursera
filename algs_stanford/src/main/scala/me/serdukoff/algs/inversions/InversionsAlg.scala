package me.serdukoff.algs.inversions

import scala.io.Source.fromURL

/**
 * Created by alex on 28.01.15.
 */
class InversionsAlg {

  var counter = 0L

  def msort[T](sortingF: (T, T) => Boolean)
              (xs: List[T]): List[T] = {

    def merge(size: Long, xs: List[T], ys: List[T]): List[T] =
      (xs, ys) match {
        case (Nil, _) => ys
        case (_, Nil) => xs
        case (x :: xs1, y :: ys1) =>
          if (sortingF(x, y)) x :: merge(size - 1, xs1, ys)
          else {
            counter = counter + size
            y :: merge(size, xs, ys1)
          }
      }

    val length = xs.length
    val n = length / 2
    if (n == 0) xs
    else {
      val (ys, zs) = xs splitAt n
      merge(ys.length, msort(sortingF)(ys), msort(sortingF)(zs))
    }
  }
}

object InversionsAlg extends App {

  val alg = new InversionsAlg
  val intSortAsc1 = alg.msort((x:Int, y: Int) => x < y) _
  val toSort: List[Int] = fromURL(getClass.getResource("/IntegerArray.txt")).getLines().toList.map(rec => rec.toInt)
  val t1 = System.currentTimeMillis
  intSortAsc1(toSort)
  val t2 = System.currentTimeMillis
  println(" Running time msort1 = "  + (t2 - t1) + " msecs")
  println(" counter = "  + alg.counter)
}

