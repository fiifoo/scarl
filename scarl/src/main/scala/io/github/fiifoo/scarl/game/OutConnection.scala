package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.entity.Creature
import io.github.fiifoo.scarl.core.{Location, State}

class OutConnection(val player: Player,
                    send: OutMessage => Unit
                   ) {

  def apply(s: State, messages: List[String]): Unit = {
    val message = OutMessage(
      calculateMessageFov(s),
      messages,
      s.entities.get(player.creature) map (_.asInstanceOf[Creature])
    )

    send(message)
  }

  private var previousMessageFov: Map[Location, LocationEntities] = Map()

  private def calculateMessageFov(s: State): OutMessage.Fov = {
    val next = (player.fov map { location => location -> LocationEntities(s, location) }).toMap
    val delta = next filterNot (n => previousMessageFov.contains(n._1) && previousMessageFov(n._1) == n._2)
    val shouldHide = (previousMessageFov -- next.keys filter (_._2.creature.isDefined)).keys
    previousMessageFov = next

    OutMessage.Fov(delta, shouldHide)
  }
}
