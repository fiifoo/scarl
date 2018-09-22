package io.github.fiifoo.scarl.core.assets

import io.github.fiifoo.scarl.core.communication.{Communication, CommunicationId}
import io.github.fiifoo.scarl.core.creature.{Faction, FactionId, Progression, ProgressionId}
import io.github.fiifoo.scarl.core.item.Recipe.RecipeId
import io.github.fiifoo.scarl.core.item.{KeyKind, KeyKindId, Recipe}
import io.github.fiifoo.scarl.core.kind.Kinds

case class Assets(catalogues: Catalogues = Catalogues(),
                  combatPower: CombatPower = CombatPower(),
                  communications: Map[CommunicationId, Communication] = Map(),
                  factions: Map[FactionId, Faction] = Map(),
                  keys: Map[KeyKindId, KeyKind] = Map(),
                  kinds: Kinds = Kinds(),
                  progressions: Map[ProgressionId, Progression] = Map(),
                  recipes: Map[RecipeId, Recipe] = Map(),
                 )
