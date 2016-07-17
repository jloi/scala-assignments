package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


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
   *   val s1 = singletonSet(1)
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

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect contains only elements in both set") {
    new TestSets {
      def between0And4: Set = x => x >= 0 && x < 5
      def between2And7: Set = x => x >= 2 && x < 8
      val s = intersect(between0And4, between2And7)
      assert(!contains(s, 0), "No intersect 1")
      assert(contains(s, 2), "Intersect 2")
      assert(contains(s, 3), "Intersect 3")
      assert(contains(s, 4), "Intersect 4")
      assert(!contains(s, 5), "No intersect 5")
    }
  }

  test("diff contains only elements in one set but not the other") {
    new TestSets {
      def between0And4: Set = x => x >= 0 && x < 5
      def between2And7: Set = x => x >= 2 && x < 8
      val s = diff(between0And4, between2And7)
      assert(contains(s, 0), "Diff 1")
      assert(contains(s, 1), "Diff 2")
      assert(!contains(s, 2), "No diff 3")
      assert(!contains(s, 3), "No diff 4")
      assert(!contains(s, 4), "No diff 5")
    }
  }

  test("filter contains only even elements in the set") {
    new TestSets {
      def between0And8: Set = x => x >= 0 && x <= 8
      val s = filter(between0And8, x => x % 2 == 0)
      assert(contains(s, 0), "filter 1")
      assert(contains(s, 2), "filter 2")
      assert(contains(s, 4), "filter 3")
      assert(contains(s, 8), "filter 4")
      assert(!contains(s, 1), "No filter 5")
      assert(!contains(s, 3), "No filter 6")
      assert(!contains(s, 5), "No filter 7")
      assert(!contains(s, 7), "No filter 8")
    }
  }

  test("forall elements in s that satisfies p") {
    new TestSets {
      def between0And8: Set = x => x >= 0 && x <= 8
      assert(!forall(between0And8, x => x < 8), "forall 1")
      assert(!forall(between0And8, x => x >= 2 && x <= 8), "forall 2")
      assert(!forall(between0And8, x => x < -bound), "forall 3")
      assert(!forall(between0And8, x => x > bound), "forall 4")
      //assert(!forall(filter(between0And8, x => x == -1), x => x == -1), "forall 5")
      assert(forall(between0And8, x => x < 9), "forall 6")
    }
  }

  test("exists s that satisfies p") {
    new TestSets {
      def between0And8: Set = x => x >= 0 && x <= 8
      assert(!exists(between0And8, x => x > 8), "exists 1")
      assert(!exists(between0And8, x => x < -5), "exists 2")
      assert(!exists(between0And8, x => x == -bound), "exists 5")
      assert(exists(between0And8, x => x < 9), "exists 1")
      assert(exists(between0And8, x => x >= 2 && x <= 5), "exists 3")
      assert(exists(between0And8, x => x == 0 ), "exists 4")
      assert(exists(between0And8, x => x > -1 && x < 9), "exists 5")
    }
  }

  test("map set") {
    new TestSets {
      def s: Set = x => x >= 1 && x <= 5
      var ms: Set = map(s, x => x * 2)
      assert("{2,4,6,8,10}" equals FunSets.toString(ms), "map 1")
      assert(!ms(1), "map 2")
      ms = map(s, x => x + 5)
      assert("{6,7,8,9,10}" equals FunSets.toString(ms), "map 3")
      assert(!ms(5), "map 4")
    }
  }
}
