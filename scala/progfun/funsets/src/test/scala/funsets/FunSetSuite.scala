package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 * - run the "test" command in the SBT console
 * - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {


  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   * - test
   * - ignore
   * - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   * val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
  }

  trait TestSetsEvenWith1Odd {
    val s1 = singletonSet(2)
    val s2 = union(s1, singletonSet(4))
    val s3 = union(s2, singletonSet(6))
    val s4 = union(s2, singletonSet(7))
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3". 
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }

  }

  test("union contains all elements") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect contains only common elements of 2 sets") {
    new TestSets {
      val s12 = union(s1, s2)
      val s23 = union(s2, s3)
      val sIntersection = intersect(s12, s23)
      assert(contains(sIntersection, 2), "Intersection 2")
    }
  }

  test("diff between two sets sets") {
    new TestSets {
      val s12 = union(s1, s2)
      val s23 = union(s2, s3)
      val sDiff = diff(s12, s23)
      assert(!contains(sDiff, 2), "2 shouldn't be in Diff result")
      assert(!contains(sDiff, 3), "3 shouldn't be in Diff result")
      assert(contains(sDiff, 1), "1 should be in Diff result")
    }
  }

  test("filter sets by predicate") {
    new TestSets {
      val s12 = union(s1, s2)
      val sUnion = union(s12, s3)
      val sFiltered = filter(sUnion, (f: Int) => f > 2)
      assert(!contains(sFiltered, 2), "2 doesn't match predicate")
      assert(contains(sFiltered, 3), "3 matches predicate")
    }
  }

  test("forall sets by predicate p%2==0") {
    new TestSetsEvenWith1Odd {
      val unionSetEven = union(union(s1, s2),s3)
      val unionEvenSetWith1Odd = union(union(s1, s2),s3)
      assert(forall(unionSetEven, (x: Int) => x % 2 == 0), "s1, s2 and s3 are all even")
      assert(!forall(union(unionEvenSetWith1Odd,s4), (x: Int) => x % 2 == 0), "set has an odd element: " + FunSets.toString(unionEvenSetWith1Odd))
    }
  }

  test("check if odd numbers exist in sets by predicate p%2==1") {
    new TestSetsEvenWith1Odd {
      val unionEvenSet = union(union(s1, s2),s3)
      val unionEvenSetWith1Odd = union(union(s1, s2),s3)
      assert(!exists(unionEvenSet, (x: Int) => x % 2 == 1), "union set has only even numbers: " + FunSets.toString(unionEvenSet))
      assert(exists(union(unionEvenSetWith1Odd,s4), (x: Int) => x % 2 == 1), "set has an odd element: " + FunSets.toString(unionEvenSetWith1Odd))
    }
  }


  test("mapped set where f(a)=a*3 was applied to elements of original set") {
    new TestSets {
      val mappedSet = map(union(union(s1, s2),s3),(a: Int)=> a*3)
      assert(contains(mappedSet,3))
      assert(contains(mappedSet,6))
      assert(contains(mappedSet,9))
    }
  }
}
