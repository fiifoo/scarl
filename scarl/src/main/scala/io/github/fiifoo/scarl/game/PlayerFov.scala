package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, Selectors, State}
import io.github.fiifoo.scarl.geometry.Fov

case class PlayerFov(locations: Set[Location] = Set(),
                     delta: Map[Location, LocationEntities] = Map(),
                     shouldHide: Set[Location] = Set(),
                     previous: Map[Location, LocationEntities] = Map()
                    ) {

  def next(s: State, player: CreatureId): PlayerFov = {
    val stats = Selectors.getCreatureStats(s)(player)
    val locations = Fov(s)(player(s).location, stats.sight.range)

    val next = (locations map { location => location -> LocationEntities(s, player, location) }).toMap
    val delta = next filterNot (n => previous.contains(n._1) && previous(n._1) == n._2)
    val shouldHide = (previous -- next.keys filter (_._2.creature.isDefined)).keys.toSet

    PlayerFov(locations, delta, shouldHide, next)
  }
}
