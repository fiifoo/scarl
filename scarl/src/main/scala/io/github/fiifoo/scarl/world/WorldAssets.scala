package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.template.{Template, TemplateId}
import io.github.fiifoo.scarl.area.theme.{Theme, ThemeId}
import io.github.fiifoo.scarl.area.{Area, AreaId}
import io.github.fiifoo.scarl.core.assets.{Assets, CombatPower}
import io.github.fiifoo.scarl.core.communication.{Communication, CommunicationId}
import io.github.fiifoo.scarl.core.creature._
import io.github.fiifoo.scarl.core.item.{KeyKind, KeyKindId}
import io.github.fiifoo.scarl.core.kind.Kinds

case class WorldAssets(areas: Map[AreaId, Area] = Map(),
                       combatPower: CombatPower = CombatPower(),
                       communications: Map[CommunicationId, Communication] = Map(),
                       equipmentCombatPower: CombatPower.Equipment = Map(),
                       factions: Map[FactionId, Faction] = Map(),
                       keys: Map[KeyKindId, KeyKind] = Map(),
                       kinds: Kinds = Kinds(),
                       progressions: Map[ProgressionId, Progression] = Map(),
                       templates: Map[TemplateId, Template] = Map(),
                       themes: Map[ThemeId, Theme] = Map(),
                      ) {

  def instance(): Assets = {
    Assets(
      combatPower,
      communications,
      equipmentCombatPower,
      factions,
      keys,
      kinds,
      progressions
    )
  }
}
