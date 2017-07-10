package cse.recentfeatures.newin2_10

import org.scalatest.path
import scala.language.dynamics

/**
  * Created by dnwiebe on 7/9/17.
  */

class F_DynamicTest extends path.FunSpec {

  describe ("Scala has no methodMissing, but it does have Dynamic.") {
    describe ("Given a class that extends marker trait Dynamic and has selectDynamic defined") {
      class DynamicSelector extends Dynamic {
        def selectDynamic (name: String): Int = name.length
      }

      val subject = new DynamicSelector ()

      it ("you can access phantom instance fields") {
        assert (subject.foo === 3)
        assert (subject.bar === 3)
        assert (subject.bamboozle === 9)
        assert (subject.whoppity_doo === 12)
      }
    }

    describe ("Given a class that extends marker trait Dynamic and has updateDynamic defined") {
      class DynamicUpdater extends Dynamic {
        var latestCall: (String, Any) = null
        def updateDynamic (name: String)(value: Any): Unit = latestCall = (name, value)
      }

      val subject = new DynamicUpdater ()

      it ("you can set phantom instance fields") {
        subject.wimpity = "glomp"
        assert (subject.latestCall === ("wimpity", "glomp"))
        subject.flarper = 476
        assert (subject.latestCall === ("flarper", 476))
        subject.bleegle = false
        assert (subject.latestCall === ("bleegle", false))
      }
    }

    describe ("Given a class that extends marker trait Dynamic and has applyDynamic defined") {
      class DynamicApplyer extends Dynamic {
        def applyDynamic (name: String)(values: Any*): Any = (name, values)
      }

      val subject = new DynamicApplyer ()

      it ("you can call phantom methods") {
        assert (subject.blonk ("wopple", 23) === ("blonk", Seq ("wopple", 23)))
        assert (subject ("gloob") === ("apply", Seq ("gloob")))
      }
    }
  }
}
