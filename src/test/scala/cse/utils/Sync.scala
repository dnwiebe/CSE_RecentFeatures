package cse.utils

import java.util.concurrent.CountDownLatch

/**
  * Created by dnwiebe on 7/9/17.
  */

class Sync {
  private val latch = new CountDownLatch (1)
  def await (): Unit = latch.await ()
  def signal (): Unit = latch.countDown()
}
