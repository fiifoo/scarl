package io.github.fiifoo.scarl.effect.combat

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Party
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.{CreatureDeadMutation, CreaturePartyMutation, Mutation, RemovableEntityMutation}
import io.github.fiifoo.scarl.effect.creature.GainExperienceEffect
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
    ) ::: leaderDeath(s)

    val eventEffect = target(s).traits.events flatMap (_.death) map (power => {
      PowerUseEffect(Some(target), target, power, requireResources = false, Some(this))
    })

    val experienceEffect = GainExperienceRule(s, this) map (x => {
      val (creature, experience) = x

      GainExperienceEffect(creature, experience, Some(this))
    })

    EffectResult(mutations, List(
      eventEffect,
      experienceEffect,
    ).flatten)
  }

  private def leaderDeath(s: State): List[Mutation] = {
    val members = s.index.partyMembers.get(Party(target)) map (_ - target)

    (members flatMap (members => {
      nextLeader(members) map (leader => {
        val mutations = members map (member => CreaturePartyMutation(member, Party(leader)))

        mutations.toList
      })
    })) getOrElse List()
  }

  private def nextLeader(members: Set[CreatureId]): Option[CreatureId] = {
    members.headOption
  }
}
