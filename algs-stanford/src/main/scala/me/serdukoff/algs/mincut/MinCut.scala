package me.serdukoff.algs.mincut

import scala.io.Source
import scala.util.Random

class MinCut(graph: Map[Int,Array[Int]]) {

}

object MinCut extends App{

    val sourceFile = Source.fromURL(getClass.getResource("/kargerMinCut.txt"))
    val lines = sourceFile.getLines()
    val vertices = lines map { line => (line.split("\\t+")) } toArray
    val tuples  = vertices map ((x:Array[String]) => (x.head.toInt -> x.tail.map(el => el.toInt)))
    val graph = tuples.toMap
    
    print(graph.get(100).get.mkString(","))
    
 def apply(source: Source) = {
    val sourceFile = Source.fromURL(getClass.getResource("/kargerMinCut.txt"))
    val vertices = sourceFile.getLines() map { line => (line.split("\\t+")) } toArray
    val graph  = vertices map ((x:Array[String]) => (x.head.toInt -> x.tail.map(el => el.toInt)))
    new MinCut(graph.toMap)
  }
}