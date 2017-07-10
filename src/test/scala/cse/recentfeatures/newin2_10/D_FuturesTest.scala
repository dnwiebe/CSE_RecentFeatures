package cse.recentfeatures.newin2_10

import cse.utils.Sync
import org.scalatest.path
import scala.concurrent._
import ExecutionContext.Implicits.global

/**
  * Created by dnwiebe on 7/9/17.
  */

class D_FuturesTest extends path.FunSpec {

  private object InterleavingService {
    def interleave[T] (a: List[T], b: List[T]): List[T] = {
      (a, b) match {
        case (Nil, Nil) => Nil
        case (_ :: _, Nil) => a
        case (Nil, _ :: _) => b
        case (ah :: at, bh :: bt) => ah :: bh :: interleave (at, bt)
      }
    }
  }

  describe ("This interleaving service") {
    val result = InterleavingService.interleave (List (11, 12, 13), List (-11, -12, -13))

    it ("acts synchronously just as expected") {
      assert (result === List (11, -11, 12, -12, 13, -13))
    }
  }

  describe ("But if it was a slow service that needed asynchronous handling") {
    describe ("you could start it up") {
      var meanwhileResult: String = null
      val result = Future {
        InterleavingService.interleave (List (11, 12, 13), List (-11, -12, -13))
      }

      describe ("do something else while waiting") {
        meanwhileResult = "MeanwhileResult"

        describe ("and then collect the pending results from the service") {
          val sync = new Sync
          var interleaved: List[Int] = null
          result.onComplete {completion =>
            interleaved = completion.get
            sync.signal ()
          }

          it ("and see what you were expecting") {
            assert (meanwhileResult === "MeanwhileResult")
            sync.await ()
            assert (interleaved === List (11, -11, 12, -12, 13, -13))
          }
        }
      }
    }
  }

  describe ("The service doesn't always have to succeed.") {
    describe ("Sometimes, it might fail with an exception.") {
      var meanwhileResult: String = null
      val result = Future {
        throw new UnsupportedOperationException ("What kind of a service do you think I am?")
      }

      describe ("We can still do something else while we're waiting") {
        meanwhileResult = "MeanwhileResult"

        describe ("but now we get an exception from the service instead of a result") {
          val sync = new Sync
          var failure: Throwable = null
          result.onComplete {completion =>
            failure = completion.failed.get
            sync.signal ()
          }

          it ("and see what we were expecting") {
            assert (meanwhileResult === "MeanwhileResult")
            sync.await ()
            assert (failure.getMessage === "What kind of a service do you think I am?")
          }
        }
      }
    }
  }

  describe ("But Futures can also be concatenated with map") {
    val intermediate = Future {
      InterleavingService.interleave (List (1, 2), List (3, 4))
    }

    val penultimate = intermediate.map {list =>
      InterleavingService.interleave (list, List (5, 6, 7, 8))
    }

    val ultimate = penultimate.map {list =>
      InterleavingService.interleave (list, List (9, 10, 11, 12, 13, 14, 15, 16))
    }

    it ("to produce a composed result") {
      val sync = new Sync ()
      ultimate.map {list =>
        assert (list === List (1, 9, 5, 10, 3, 11, 6, 12, 2, 13, 7, 14, 4, 15, 8, 16))
        sync.signal ()
      }
      sync.await ()
    }
  }

  describe ("They can also be comprehended with for") {
    val left = Future {
      InterleavingService.interleave (List (1, 2), List (3, 4))
    }

    val right = Future {
      InterleavingService.interleave (List (5, 6), List (7, 8))
    }

    val both: Future[List[Int]] = for {
      l <- left
      r <- right
    } yield InterleavingService.interleave (l, r)

    it ("and assembled in deterministic spatial order but random temporal order") {
      val sync = new Sync ()
      both.map {list =>
        assert (list === List (1, 5, 3, 7, 2, 6, 4, 8))
        sync.signal ()
      }
      sync.await ()
    }
  }
}
