package me.serdukoff.algs.graph

import scala.io.Source
import scala.util.Random

class Graph(graph: Map[Int, Array[Int]]) {

  type Edge = (Int, Int)
  type Edges = Array[Edge]

  val vertices = graph.keySet

  private[Graph] val edges: Edges = graph.flatMap(entry => entry._2.map(tail => (entry._1, tail))).toArray

  def contract(edge: Edge) = {

  }

}
object Graph {

  def apply(graph: Map[Int, Array[Int]]) {
    new Graph(graph)
  }
}

