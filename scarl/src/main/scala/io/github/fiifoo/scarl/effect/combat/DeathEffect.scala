package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Party
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.{CreatureDeadMutation, RemovableEntityMutation}
import io.github.fiifoo.scarl.effect.creature.{ChangePartyEffect, GainExperienceEffect}
import io.github.fiifoo.scarl.effect.interact.PowerUseEffect
import io.github.fiifoo.scarl.rule.GainExperienceRule

case class DeathEffect(target: CreatureId,
                       location: Location,
                       parent: Option[Effect] = None
                      ) extends Effect {

  def apply(s: State): EffectResult = {
    if (target(s).dead) {
      return EffectResult()
    }

    val mutations = List(
      CreatureDeadMutation(target),
      RemovableEntityMutation(target)
    )

    val eventEffect = target(s).traits.events flatMap (_.death) map (power => {
      PowerUseEffect(Some(target), target, power, requireResources = false, Some(this))
    })

    val experienceEffect = GainExperienceRule(s, this) map (x => {
      val (creature, experience) = x

      GainExperienceEffect(creature, experience, Some(this))
    })

    val effects = List(
      eventEffect,
      experienceEffect
    ).flatten ::: changeLeaderEffects(s)

    EffectResult(mutations, effects)
  }

  private def changeLeaderEffects(s: State): List[Effect] = {
    val members = s.index.partyMembers.get(Party(target)) map (_ - target)

    (members flatMap (members => {
      getNextLeader(s, members) map (leader => {
        members.toList map (member => ChangePartyEffect(member, Party(leader), Some(this)))
      })
    })) getOrElse List()
  }

  private def getNextLeader(s: State, members: Set[CreatureId]): Option[CreatureId] = {
    (members filterNot (_ (s).dead)).headOption
  }
}
