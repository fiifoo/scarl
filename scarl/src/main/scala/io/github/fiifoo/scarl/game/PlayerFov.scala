package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.{Location, State}

case class PlayerFov(locations: Set[Location] = Set(),
                     delta: Map[Location, LocationEntities] = Map(),
                     shouldHide: Set[Location] = Set(),
                     previous: Map[Location, LocationEntities] = Map()
                    ) {

  def next(s: State, locations: Set[Location]): PlayerFov = {
    val next = (locations map { location => location -> LocationEntities(s, location) }).toMap
    val delta = next filterNot (n => previous.contains(n._1) && previous(n._1) == n._2)
    val shouldHide = (previous -- next.keys filter (_._2.creature.isDefined)).keys.toSet

    PlayerFov(locations, delta, shouldHide, next)
  }
}
