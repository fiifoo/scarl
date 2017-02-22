package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.core.kind.CreatureKindId

case class Statistics(deaths: Map[CreatureKindId, Int] = Map())
