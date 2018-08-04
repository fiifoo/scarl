package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId}

object Utils {

  def isSmartWidgetEnemy(s: State, widget: ContainerId)(target: CreatureId): Boolean = {
    val faction = widget(s).owner map (owner => {
      // If widget owner dies, it will target everyone. Not logical or optimal but maybe good enough.
      owner(s) map (_.faction)
    }) getOrElse {
      s.area.owner
    }

    faction map (faction => {
      val enemies = s.assets.factions(faction).enemies

      enemies.contains(target(s).faction)
    }) getOrElse {
      true
    }
  }
}
