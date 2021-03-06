package io.github.fiifoo.scarl.game.api

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature._
import io.github.fiifoo.scarl.core.entity.Selectors.{getCreatureConditionStatuses, getCreatureStanceStatuses}
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
    val stances = getCreatureStanceStatuses(s)(creature) map (x => {
      StanceInfo(x.stance.key, x.duration, x.stance.duration)
    })

    CreatureInfo(
      values.id,
      values.kind,
      values.faction,
      values.stats,

      values.character,
      values.locked,
      values.traits.missile,
      values.usable,

      values.damage,
      values.location,
      values.party,
      values.resources,

      conditions,
      stances,
    )
  }

  case class ConditionInfo(key: String, strength: Int)

  case class StanceInfo(key: String, duration: Option[Int], initialDuration: Int)

}

case class CreatureInfo(id: CreatureId,
                        kind: CreatureKindId,
                        faction: FactionId,
                        stats: Stats,

                        character: Option[Character],
                        locked: Option[Lock],
                        missile: Option[Missile],
                        usable: Option[CreaturePower],

                        damage: Double,
                        location: Location,
                        party: Party,
                        resources: Resources,

                        conditions: Set[CreatureInfo.ConditionInfo],
                        stances: Set[CreatureInfo.StanceInfo],
                       )
