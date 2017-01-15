package io.github.fiifoo.scarl.core.entity

import org.scalatest._

class ActorQueueSpec extends FlatSpec with Matchers {

  "ActorQueue" should "implement queue for actor ids using queue number" in {
    val queue = new ActorQueue()
      .enqueue(CreatureId(1), 5)
      .enqueue(CreatureId(2), 10)
      .enqueue(CreatureId(3), 2)
      .enqueue(CreatureId(4), 1)
      .enqueue(CreatureId(5), 500)

    queue.dequeue() should ===(CreatureId(4))
    queue.dequeue() should ===(CreatureId(3))
    queue.dequeue() should ===(CreatureId(1))
    queue.dequeue() should ===(CreatureId(2))
    queue.dequeue() should ===(CreatureId(5))
  }

  it should "tell current head correctly" in {
    val queue = new ActorQueue()
      .enqueue(CreatureId(1), 5)
      .enqueue(CreatureId(2), 10)
      .enqueue(CreatureId(3), 2)

    queue.head should ===(CreatureId(3))
    queue.dequeue() should ===(CreatureId(3))
    queue.head should ===(CreatureId(1))
    queue.dequeue() should ===(CreatureId(1))
    queue.head should ===(CreatureId(2))
    queue.dequeue() should ===(CreatureId(2))
  }

  it should "dequeue smallest actor ids first for same queue numbers" in {
    val queue = new ActorQueue()
      .enqueue(CreatureId(2), 1)
      .enqueue(CreatureId(1), 1)
      .enqueue(CreatureId(3), 1)

    queue.dequeue() should ===(CreatureId(1))
    queue.dequeue() should ===(CreatureId(2))
    queue.dequeue() should ===(CreatureId(3))
  }

  it should "tell if queue is empty or not" in {
    val queue = new ActorQueue().enqueue(CreatureId(1), 1)

    queue.isEmpty should ===(false)
    queue.dequeue()
    queue.isEmpty should ===(true)
  }
}
