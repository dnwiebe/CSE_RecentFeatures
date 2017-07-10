package cse.recentfeatures.newin2_10

import org.scalatest.path

/**
  * Created by dnwiebe on 7/9/17.
  */

class E_StringInterpolationTest extends path.FunSpec {
  describe ("You can still concatenate strings like in Java") {
    val noun = "robots"
    val attribute = "loyalty"

    val result = "The great thing about " + noun + " is their " + attribute + "."

    it ("and get what you expect") {
      assert (result === "The great thing about robots is their loyalty.")
    }
  }

  describe ("But you can also interpolate them like in Ruby and Python") {
    val noun = "robots"
    val attribute = "loyalty"

    val result = s"The great thing about $noun is their $attribute."

    it ("and still get what you expect") {
      assert (result === "The great thing about robots is their loyalty.")
    }
  }

  describe ("You can also do computations in the interpolations") {
    val noun = "stobor"
    val attribute = "loyalty"

    val result = s"The great thing about ${noun.reverse} is their ${attribute.toUpperCase}!"

    it ("if you use braces") {
      assert (result === "The great thing about robots is their LOYALTY!")
    }
  }
}
