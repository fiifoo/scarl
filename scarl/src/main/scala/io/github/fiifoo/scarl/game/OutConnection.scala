package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.entity.Creature
import io.github.fiifoo.scarl.core.kind.Kinds
import io.github.fiifoo.scarl.core.{Location, State}

class OutConnection(val player: Player,
                    send: OutMessage => Unit
                   ) {

  def apply(s: State,
            area: AreaId,
            messages: List[String],
            kinds: Option[Kinds] = None,
            statistics: Option[Statistics] = None
           ): Unit = {
    val message = OutMessage(
      area,
      calculateMessageFov(s, area),
      messages,
      s.entities.get(player.creature) map (_.asInstanceOf[Creature]),
      kinds,
      statistics
    )

    send(message)
  }

  private var previousArea: Option[AreaId] = None
  private var previousMessageFov: Map[Location, LocationEntities] = Map()

  private def calculateMessageFov(s: State, area: AreaId): OutMessage.Fov = {
    val previous: Map[Location, LocationEntities] = if (previousArea.contains(area)) previousMessageFov else Map()

    val next = (player.fov map { location => location -> LocationEntities(s, location) }).toMap
    val delta = next filterNot (n => previous.contains(n._1) && previous(n._1) == n._2)
    val shouldHide = (previous -- next.keys filter (_._2.creature.isDefined)).keys

    previousArea = Some(area)
    previousMessageFov = next

    OutMessage.Fov(delta, shouldHide)
  }
}
