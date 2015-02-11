package patmat

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import patmat.Huffman._

@RunWith(classOf[JUnitRunner])
class HuffmanSuite extends FunSuite {
  trait TestTrees {
    val t1 = Fork(Leaf('a', 2), Leaf('b', 3), List('a', 'b'), 5)
    val t2 = Fork(Fork(Leaf('a', 2), Leaf('b', 3), List('a', 'b'), 5), Leaf('d', 4), List('a', 'b', 'd'), 9)
  }

  test("weight of a larger tree") {
    new TestTrees {
      assert(weight(t1) === 5)
    }
  }

  test("chars of a larger tree") {
    new TestTrees {
      assert(chars(t2) === List('a', 'b', 'd'))
      makeCodeTree(t1, t2)
    }
  }

  test("string2chars(\"hello, world\")") {
    assert(string2Chars("hello, world") === List('h', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd'))
  }

  test("times(List('a','b','d'))") {
    assert(times(List('a', 'b', 'd')) === List(('a', 1), ('b', 1), ('d', 1)))
  }

  test("times(List('a','a','d'))") {
    assert(times(List('a', 'a', 'd')) === List(('a', 2), ('d', 1)))
  }

  test("times(List('a','a','a'))") {
    assert(times(List('a', 'a', 'a')) === List(('a', 3)))
  }

  test("makeOrderedLeafList(List('a','a','d'))") {
    assert(makeOrderedLeafList(times(List('a', 'a', 'd'))) === List(Leaf('d', 1), Leaf('a', 2)))
  }

  test("singleton makeOrderedLeafList(List('a','a','d'))") {
    assert(!singleton(makeOrderedLeafList(times(List('a', 'a', 'd')))))
  }

  test("combine of some leaf list") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 1))
    assert(combine(leaflist) === List(Leaf('x', 1), Fork(Leaf('e', 1), Leaf('t', 2), List('e', 't'), 3)))
  }

  test("until of trees") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    assert(until(singleton, combine)(leaflist) === Fork(Fork(Leaf('e', 1), Leaf('t', 2), List('e', 't'), 3), Leaf('x', 4), List('e', 't', 'x'), 7))
  }

  test("createCodeTable of chars") {
    val leaflist = List('e', 'x', 't', 'x', 'x', 't', 'x')
    assert(createCodeTree(leaflist) === Fork(Fork(Leaf('e', 1), Leaf('t', 2), List('e', 't'), 3), Leaf('x', 4), List('e', 't', 'x'), 7))
  }

  test("decode and encode a secret") {
    new TestTrees {
      assert(encode(frenchCode)(decodedSecret) === secret)
    }
  }

  test("codeBits") {
    new TestTrees {
      val codeTable: CodeTable = List(('a', List(0, 0, 1)))
      assert(codeBits(codeTable)('b') == Nil)
    }
  }

  test("mergeTables") {
    new TestTrees {
      val codeTable1: CodeTable = List(('a', List(0, 0, 1)), ('b', List(1, 1, 0)))
      val codeTable2: CodeTable = List(('c', List(0, 0, 1)), ('d', List(1, 1, 0)))
      assert(mergeCodeTables(codeTable1, codeTable2) === List(('a', List(0, 0, 1)), ('b', List(1, 1, 0)), ('c', List(0, 0, 1)), ('d', List(1, 1, 0))))
    }
  }

  test("convert") {
    new TestTrees {
      val leaflist = List('e', 'x', 't', 'x', 'x', 't', 'x')
      val codeTable: CodeTable = List(('e', List(0, 0)), ('t', List(0, 1)), ('x', List(1)))
      assert(convert(createCodeTree(leaflist)) === codeTable)
    }
  }

  test("decode and quick encode a secret") {
    new TestTrees {
      val leaflist = List('e', 'x', 't', 'x', 'x', 't', 'x')
      val codeTable: CodeTable = List(('e', List(0, 0)), ('t', List(0, 1)), ('x', List(1)))
      assert(quickEncode(frenchCode)(decodedSecret) === secret)
    }
  }
}
