package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.entity.Creature
import io.github.fiifoo.scarl.core.kind.Kinds

case class OutMessage(area: AreaId,
                      fov: PlayerFov,
                      messages: List[String],
                      player: Option[Creature],
                      kinds: Option[Kinds] = None,
                      statistics: Option[Statistics] = None
                     )
