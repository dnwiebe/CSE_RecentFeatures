package cse.recentfeatures.newin2_10

import org.scalatest.path

/**
  * Created by dnwiebe on 7/9/17.
  */

class G_MethodDependentTypesTest extends path.FunSpec {

  describe ("Method dependent types were taken off the experimental list in 2.10") {

    def method1[T] (param1: String, param2: T, param3: Int): param2.type = {
      param2
    }

    describe ("...but I don't really understand what they're good for, because this is just as good") {

      def method2[T] (param1: String, param2: T, param3: Int): T = {
        param2
      }

      it ("...see?") {
        assert (method1 ("String", true, 47) === true)
        assert (method2 ("String", true, 47) === true)
      }
    }
  }
}
