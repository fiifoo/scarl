package io.github.fiifoo.scarl.area

import io.github.fiifoo.scarl.area.template.TemplateId
import io.github.fiifoo.scarl.area.theme.ThemeId
import io.github.fiifoo.scarl.core.ai.Strategy
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.creature.Faction.Disposition
import io.github.fiifoo.scarl.core.creature.FactionId

case class Area(id: AreaId,
                template: TemplateId,
                theme: ThemeId,
                owner: Option[FactionId] = None,
                dispositions: Map[FactionId, Map[FactionId, Disposition]] = Map(),
                strategies: Map[FactionId, Strategy] = Map(),
               )
