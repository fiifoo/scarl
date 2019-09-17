package io.github.fiifoo.scarl.game.player

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{CreatureId, Selectors}
import io.github.fiifoo.scarl.core.geometry.{Fov, Location}
import io.github.fiifoo.scarl.game.area.LocationEntities

object PlayerFov {
  def apply(s: State, player: CreatureId, previous: Option[PlayerFov]): PlayerFov = {
    val stats = Selectors.getCreatureStats(s)(player)
    val locations = Fov(s)(player(s).location, stats.sight.range)
    val entities = (locations map { location => location -> LocationEntities(s, player, location) }).toMap

    previous map (previous => {
      val delta = entities filterNot (n => previous.entities.contains(n._1) && previous.entities(n._1) == n._2)
      val shouldHide = (previous.entities -- entities.keys filter (_._2.creatures.nonEmpty)).keys.toSet

      PlayerFov(entities, locations, delta, shouldHide)
    }) getOrElse {
      PlayerFov(entities, locations, entities)
    }
  }
}

case class PlayerFov(entities: Map[Location, LocationEntities] = Map(),
                     locations: Set[Location] = Set(),
                     delta: Map[Location, LocationEntities] = Map(),
                     shouldHide: Set[Location] = Set()
                    )
