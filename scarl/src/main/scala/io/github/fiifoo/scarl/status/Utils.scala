package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Selectors.getFactionHostileFactions
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId}

object Utils {

  def isSmartWidgetEnemy(s: State, widget: ContainerId)(target: CreatureId): Boolean = {
    val widgetValues = widget(s)

    val faction = widgetValues.owner map (owner => {
      owner(s) map (_.faction)
    }) getOrElse {
      widgetValues.faction
    }

    faction map (faction => {
      val enemies = getFactionHostileFactions(s)(faction)

      enemies.contains(target(s).faction)
    }) getOrElse {
      true
    }
  }
}
