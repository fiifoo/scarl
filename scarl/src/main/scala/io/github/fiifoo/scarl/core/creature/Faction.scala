package io.github.fiifoo.scarl.core.creature

import io.github.fiifoo.scarl.core.ai.Strategy
import io.github.fiifoo.scarl.core.creature.Faction.Disposition

object Faction {

  sealed trait Disposition

  case object Friendly extends Disposition

  case object Neutral extends Disposition

  case object Hostile extends Disposition

}

case class Faction(id: FactionId,
                   strategy: Option[Strategy] = None,
                   dispositions: Map[FactionId, Disposition]
                  )
