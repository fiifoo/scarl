package io.github.fiifoo.scarl.game.area

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature._
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureConditionStatuses
import io.github.fiifoo.scarl.core.entity.{CreatureId, CreaturePower}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Lock
import io.github.fiifoo.scarl.core.kind.CreatureKindId

object CreatureInfo {
  def apply(s: State)(creature: CreatureId): CreatureInfo = {
    val values = creature(s)
    val conditions = getCreatureConditionStatuses(s)(creature) map (x => {
      ConditionInfo(x.condition.key, x.strength)
    })

    CreatureInfo(
      values.id,
      values.kind,
      values.faction,
      values.location,
      values.damage,
      values.resources,
      values.stats,

      values.character,
      values.locked,
      values.missile,
      values.usable,

      conditions
    )
  }

  case class ConditionInfo(key: String, strength: Int)

}

case class CreatureInfo(id: CreatureId,
                        kind: CreatureKindId,
                        faction: FactionId,
                        location: Location,
                        damage: Double,
                        resources: Resources,
                        stats: Stats,

                        character: Option[Character],
                        locked: Option[Lock],
                        missile: Option[Missile],
                        usable: Option[CreaturePower],

                        conditions: Set[CreatureInfo.ConditionInfo],
                       )
