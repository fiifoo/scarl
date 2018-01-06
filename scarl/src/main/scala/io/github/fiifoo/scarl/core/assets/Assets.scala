package io.github.fiifoo.scarl.core.assets

import io.github.fiifoo.scarl.core.communication.{Communication, CommunicationId}
import io.github.fiifoo.scarl.core.creature.{Faction, FactionId, Progression, ProgressionId}
import io.github.fiifoo.scarl.core.item.{KeyKind, KeyKindId}
import io.github.fiifoo.scarl.core.kind.Kinds

case class Assets(combatPower: CombatPower = CombatPower(),
                  communications: Map[CommunicationId, Communication] = Map(),
                  factions: Map[FactionId, Faction] = Map(),
                  keys: Map[KeyKindId, KeyKind] = Map(),
                  kinds: Kinds = Kinds(),
                  progressions: Map[ProgressionId, Progression] = Map(),
                 )
