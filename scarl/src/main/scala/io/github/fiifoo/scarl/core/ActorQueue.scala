package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.ActorQueue._
import io.github.fiifoo.scarl.core.entity.{Actor, ActorId}

import scala.collection.SortedSet

object ActorQueue {
  type Item = (ActorId, Int)

  implicit val ordering = Ordering.by(orderBy)

  private def orderBy(x: (ActorId, Int)): (Int, Int) = (x._2, x._1.value)

  def apply(s: State): ActorQueue = {
    val queue = (s.entities.values collect {
      case actor: Actor => (actor.id, actor.tick)
    }).to[SortedSet]

    ActorQueue(queue)
  }
}

case class ActorQueue(queue: SortedSet[Item] = SortedSet[Item]()) {

  def enqueue(actor: Actor): ActorQueue = {
    this.copy(queue + (actor.id -> actor.tick))
  }

  def dequeue: Option[(ActorId, ActorQueue)] = {
    queue.headOption map (item => {
      val actor = item._1
      val next = queue - item

      (actor, this.copy(next))
    })
  }

  def remove(actor: Actor): ActorQueue = {
    this.copy(queue - (actor.id -> actor.tick))
  }

  def isEmpty: Boolean = queue.isEmpty

  def nonEmpty: Boolean = queue.nonEmpty

  def headOption: Option[ActorId] = queue.headOption map (_._1)
}
