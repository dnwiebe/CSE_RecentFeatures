package cse.recentfeatures.newin2_10

import org.scalatest.path

/**
  * Created by dnwiebe on 7/9/17.
  */

class A_ImplicitClassesTest extends path.FunSpec {

  implicit class IntEnhancer (self: Int) {
    def isEven: Boolean = (self % 2) == 0
    def halve: Option[Int] = if (isEven) Some (self / 2) else None
  }

  describe ("The implicit modifier is now available on classes") {
    it ("to make possible bundles of extension methods") {
      assert (14.isEven === true)
      assert (21.isEven === false)
      assert (14.halve === Some (7))
      assert (21.halve === None)
    }

    it ("Desugared equivalents") {
      assert (new IntEnhancer (14).isEven === true)
      assert (new IntEnhancer (21).isEven === false)
      assert (new IntEnhancer (14).halve === Some (7))
      assert (new IntEnhancer (21).halve === None)
    }
  }
}
