package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.entity.{Actor, ActorId, CreatureId}
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import org.scalatest._

class ActorQueueSpec extends FlatSpec with Matchers {

  class QueueHelper() {
    var queue = ActorQueue()

    def enqueue(id: Int, tick: Int): QueueHelper = {
      queue = queue.enqueue(getActor(id, tick))

      this
    }

    def dequeue(): ActorId = {
      val (actor, next) = queue.dequeue.get
      queue = next

      actor
    }

    def head: ActorId = {
      queue.headOption.get
    }
  }

  def getActor(id: Int, tick: Int): Actor = {
    TestCreatureFactory.create(id = CreatureId(id), tick = tick)
  }

  "ActorQueue" should "implement queue for actors" in {
    val queue = new QueueHelper()
      .enqueue(1, 5)
      .enqueue(2, 10)
      .enqueue(3, 2)
      .enqueue(4, 1)
      .enqueue(5, 500)

    queue.dequeue() should ===(CreatureId(4))
    queue.dequeue() should ===(CreatureId(3))
    queue.dequeue() should ===(CreatureId(1))
    queue.dequeue() should ===(CreatureId(2))
    queue.dequeue() should ===(CreatureId(5))
  }

  it should "tell current head correctly" in {
    val queue = new QueueHelper()
      .enqueue(1, 5)
      .enqueue(2, 10)
      .enqueue(3, 2)

    queue.head should ===(CreatureId(3))
    queue.dequeue() should ===(CreatureId(3))
    queue.head should ===(CreatureId(1))
    queue.dequeue() should ===(CreatureId(1))
    queue.head should ===(CreatureId(2))
    queue.dequeue() should ===(CreatureId(2))
  }

  it should "dequeue smallest actor ids first for same queue numbers" in {
    val queue = new QueueHelper()
      .enqueue(2, 1)
      .enqueue(1, 1)
      .enqueue(3, 1)

    queue.dequeue() should ===(CreatureId(1))
    queue.dequeue() should ===(CreatureId(2))
    queue.dequeue() should ===(CreatureId(3))
  }

  it should "tell if queue is empty or not" in {
    val queue = new QueueHelper().enqueue(1, 1)

    queue.queue.isEmpty should ===(false)
    queue.dequeue()
    queue.queue.isEmpty should ===(true)
  }
}
