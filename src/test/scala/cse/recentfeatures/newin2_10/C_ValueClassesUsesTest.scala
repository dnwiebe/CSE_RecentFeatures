package cse.recentfeatures.newin2_10

import org.scalatest.path

/**
  * Created by dnwiebe on 7/9/17.
  */

object C_TypeSafetyTest {
  case class Meters (count: Double) extends AnyVal {
    def + (other: Meters): Meters = Meters (this.count + other.count)
  }
}

class C_TypeSafetyTest extends path.FunSpec {
  import C_TypeSafetyTest._

  describe ("Value classes can be used for type safety without allocation") {
    describe ("These two are compiled to exactly the same code") {
      val typeUnsafeSum = 3.3 + 4.4 + 5.5
      val typeSafeSum = Meters (3.3) + Meters (4.4) + Meters (5.5)

      it ("but one is just a Double and the other is specifically typed") {
        assert (typeUnsafeSum === 13.2)
        assert (typeSafeSum === Meters (13.2))
      }
    }
  }
}



object C_ExtensionMethodsTest {
  implicit class Grammarian (val self: String) extends AnyVal {
    def adverbify: String = self + "ly"
  }
}

class C_ExtensionMethodsTest extends path.FunSpec {
  import C_ExtensionMethodsTest._

  describe ("When extension-method classes extend AnyVal") {
    val result = "continual".adverbify

    it ("they don't need to actually be allocated") {
      assert (result === "continually")
    }
  }
}