package me.serdukoff.algs.mincut

object Sheet1 {

  import scala.io.Source
  
	implicit val sourceFile = Source.fromURL(getClass.getResource("/kargerMinCut.txt"))
                                                  //> sourceFile  : scala.io.BufferedSource = non-empty iterator
	
	val graph = new Graph                     //> graph  : me.serdukoff.algs.mincut.Graph = me.serdukoff.algs.mincut.Graph@90f
                                                  //| 25dc
	
	graph.graphView                           //> res0: Array[(Int, Int)] = Array((51,55), (55,57), (49,54), (49,52), (49,53),
                                                  //|  (49,53), (51,57), (49,51), (49,53), (49,56), (55,56), (49,55), (49,53), (49
                                                  //| ,52), (49,54), (49,57), (54,57), (49,57), (49,56), (49,57), (49,54), (49,50)
                                                  //| , (49,51), (49,51), (49,52), (49,52), (49,51), (49,50), (52,55), (49,55), (4
                                                  //| 9,55), (49,55), (49,57), (49,56), (49,50), (49,55), (49,54), (51,54), (49,51
                                                  //| ), (49,53), (49,52), (49,54), (49,50), (49,50), (49,56), (52,56), (49,50), (
                                                  //| 49,51), (49,51), (49,52), (49,57), (49,55), (49,53), (49,57), (52,57), (49,5
                                                  //| 1), (49,54), (49,54), (49,51), (49,52), (50,57), (49,55), (49,54), (49,57), 
                                                  //| (49,53), (49,51), (49,53), (49,55), (49,52), (49,55), (49,51), (49,52), (49,
                                                  //| 57), (49,52), (49,55), (49,56), (49,50), (49,53), (49,53), (49,56), (49,55),
                                                  //|  (49,56), (49,52), (49,57), (49,51), (49,50), (49,55), (49,50), (49,53), (51
                                                  //| ,53), (50,51), (49,57), (49,51), (49,53), (49,54), (49,50), (49,56), (49,55)
                                                  //| , (49,53), (51,57), (49,
                                                  //| Output exceeds cutoff limit.
}