package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.template.{Template, TemplateId}
import io.github.fiifoo.scarl.area.theme.{Theme, ThemeId}
import io.github.fiifoo.scarl.area.{Area, AreaId}
import io.github.fiifoo.scarl.core.assets.{Assets, CombatPower}
import io.github.fiifoo.scarl.core.communication.{Communication, CommunicationId}
import io.github.fiifoo.scarl.core.creature._
import io.github.fiifoo.scarl.core.item.Recipe.RecipeId
import io.github.fiifoo.scarl.core.item.{KeyKind, KeyKindId, Recipe}
import io.github.fiifoo.scarl.core.kind.Kinds

case class WorldAssets(areas: Map[AreaId, Area] = Map(),
                       catalogues: WorldCatalogues = WorldCatalogues(),
                       combatPower: CombatPower = CombatPower(),
                       communications: Map[CommunicationId, Communication] = Map(),
                       factions: Map[FactionId, Faction] = Map(),
                       keys: Map[KeyKindId, KeyKind] = Map(),
                       kinds: Kinds = Kinds(),
                       progressions: Map[ProgressionId, Progression] = Map(),
                       recipes: Map[RecipeId, Recipe] = Map(),
                       regions: Map[RegionId, Region] = Map(),
                       sites: Map[SiteId, Site] = Map(),
                       templates: Map[TemplateId, Template] = Map(),
                       themes: Map[ThemeId, Theme] = Map(),
                       worlds: Map[WorldId, World] = Map(),
                      ) {

  def instance(): Assets = {
    Assets(
      catalogues.instance(),
      combatPower,
      communications,
      factions,
      keys,
      kinds,
      progressions,
      recipes
    )
  }
}
