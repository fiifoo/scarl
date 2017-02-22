package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.entity.Creature
import io.github.fiifoo.scarl.core.kind.Kinds

object OutMessage {

  case class Fov(delta: Map[Location, LocationEntities],
                 shouldHide: Iterable[Location]
                )

}

case class OutMessage(fov: OutMessage.Fov,
                      messages: List[String],
                      player: Option[Creature],
                      kinds: Option[Kinds] = None,
                      statistics: Option[Statistics] = None
                     )
