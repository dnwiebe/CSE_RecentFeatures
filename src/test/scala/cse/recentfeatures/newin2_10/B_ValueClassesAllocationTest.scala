package cse.recentfeatures.newin2_10

import org.scalatest.path

/**
  * Created by dnwiebe on 7/8/17.
  */
case class NormalClass (x: Int)
case class ValueClass (x: Int) extends AnyVal

class B_ValueClassesAllocationTest extends path.FunSpec {
  private val runtime = Runtime.getRuntime
  private val allocationSize = 20000

  describe ("When the heap size is measured") {
    pending
    val heapSizeBeforeNormal = runtime.freeMemory

    describe ("and then some objects of a normal class are allocated") {
      val normalObjects = (0 until allocationSize).map {i => NormalClass (i)}

      describe ("and the heap size measured again") {
        val heapSizeAfterNormal = runtime.freeMemory ()
        val normalConsumption = heapSizeBeforeNormal - heapSizeAfterNormal

        it ("we see that some memory has been consumed") {
          checkConsumption (normalObjects, normalConsumption, "normal")
        }

        describe ("and then some objects of a value class are allocated") {
          val valueObjects = (0 until allocationSize).map {i => ValueClass (i)}

          describe ("and the heap size measured again") {
            val heapSizeAfterValue = runtime.freeMemory ()
            val valueConsumption = heapSizeAfterNormal - heapSizeAfterValue

            it ("we see that less memory has been consumed") {
              checkConsumption (valueObjects, valueConsumption, "value")

              assert (valueConsumption < normalConsumption)
            }
          }
        }
      }
    }
  }

  private def checkConsumption (objects: Seq[_], consumption: Long, kind: String): Unit = {
    println (s"$consumption $kind bytes consumed")
    assert (objects.size === allocationSize)
    assert (consumption > 0)
  }
}
