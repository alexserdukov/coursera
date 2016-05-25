package me.serdukoff.algs.graph

import scala.io.Source
import scala.util.Random

class Graph(val edges: Array[(Int,Int)]) {

  def contract(edge: (Int,Int)): Graph = {
	  val filteredEdges = edges.filterNot (p => (p == edge) || (p.swap == edge))
	  val mappedEdges = filteredEdges.map(f => 
	    if (f._1 == edge._2) (edge._1, f._2) 
	    else if (f._2 == edge._2) (f._1, edge._1)
	    else f)  
	  new Graph(mappedEdges)
  }

  def size = edges.length
}

object Graph {

  def apply(graph: Map[Int, Array[Int]]): Graph = {
    
    val edges: Array[Set[Int]] = graph.flatMap(entry => {
      val out = entry._1
      val ins = entry._2
      ins.map(in => Set(out,in))
  }).toSet.toArray
    
    new Graph(edges.map(
        set => (set.head,set.last)
        ).toArray
    )
  }
  
  
}