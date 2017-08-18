package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.communication.{Communication, CommunicationId}
import io.github.fiifoo.scarl.core.creature.{Faction, FactionId, Progression, ProgressionId}
import io.github.fiifoo.scarl.core.kind.Kinds
import io.github.fiifoo.scarl.core.power.Powers

case class Assets(combatPower: CombatPower = CombatPower(),
                  communications: Map[CommunicationId, Communication] = Map(),
                  factions: Map[FactionId, Faction] = Map(),
                  kinds: Kinds = Kinds(),
                  powers: Powers = Powers(),
                  progressions: Map[ProgressionId, Progression] = Map(),
                 )
