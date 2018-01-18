package io.github.fiifoo.scarl.core.creature

import io.github.fiifoo.scarl.core.ai.Strategy

case class Faction(id: FactionId,
                   strategy: Option[Strategy] = None,
                   enemies: Set[FactionId] = Set()
                  )
