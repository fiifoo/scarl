package io.github.fiifoo.scarl.area

import io.github.fiifoo.scarl.area.template.TemplateId
import io.github.fiifoo.scarl.area.theme.ThemeId
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.world.RegionId

case class Area(id: AreaId,
                region: RegionId,
                template: TemplateId,
                theme: ThemeId,
                owner: Option[FactionId] = None,
                power: Map[CombatPower.Category, (Int, Int)] = Map(),
               )
