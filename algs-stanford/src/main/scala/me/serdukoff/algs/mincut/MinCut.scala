package me.serdukoff.algs.mincut

import scala.io.Source
import scala.util.Random
import me.serdukoff.algs.graph.Graph

class MinCut(val graphView: Map[Int, Array[Int]]) {

  type Edge = (Int, Int)
  type Edges = Array[Edge]

  val count = graphView.keySet.size - 2
  val graph = Graph(graphView)
  val random = new Random(System.nanoTime())

  def compute(retries: Int): Int = {

    def find(graph: Graph, count: Int): Int = {
      if (count == 0){
        println("Iteration #" + retries + "ended")
        graph.size
      }
      else {
        val edge = graph.edges(random.nextInt(graph.size))
        find(graph.contract(edge), count - 1)
      }
    }

    if (retries == 0)
      find(graph, count)
    else
      Math.min(find(graph, count), compute(retries - 1))
  }
}

object MinCut extends App {

  def apply(url: String) = {
    val sourceFile = Source.fromURL(getClass.getResource(url))
    val vertices = sourceFile.getLines() map { line => (line.split("\\t+")) } toArray
    val graphView = vertices map ((x: Array[String]) => (x.head.toInt -> x.tail.map(el => el.toInt)))
    new MinCut(graphView.toMap)
  }

  val minCut = MinCut("/kargerMinCut.txt")
  print("MinCut = " + minCut.compute(200).toInt)

}