package br.fcv;

import org.scalatest._
import org.scalatest.Matchers._
import org.scalatest.junit.JUnitRunner

import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class FooTest extends FlatSpec with Matchers {

	"A Foo" should "sum properly" in {
		val foo = new Foo

		foo.sum(2, 2) should be (4)
		foo.sum(5, 3) should be (8)
	}

	"A Foo" should "diff properly" in {
		val foo = new Foo

		foo.diff(2, 2) should be (0)
		foo.diff(5, 3) should be (2)
		foo.diff(5, 7) should be (-2)
	}
}
