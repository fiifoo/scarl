package io.github.fiifoo.scarl.power

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.Power.Resources
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.effect.interact.PowerUseEffect

case class PartyPower(description: Option[String] = None,
                      resources: Option[Resources] = None,
                      power: CreaturePower,
                     ) extends CreaturePower {
  def apply(s: State, creature: CreatureId, user: Option[CreatureId]): List[Effect] = {
    s.index.partyMembers.get(creature(s).party) map (members => {
      members.toList filter (!_ (s).dead) map (member => {
        PowerUseEffect(Some(member), member, this.power, requireResources = false)
      })
    }) getOrElse {
      Nil
    }
  }
}
