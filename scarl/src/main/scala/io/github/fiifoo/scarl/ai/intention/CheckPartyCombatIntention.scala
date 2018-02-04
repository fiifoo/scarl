package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Intention
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}

import scala.util.Random

object CheckPartyCombatIntention extends Intention {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    Utils.findPartyEnemy(s, actor) flatMap travel(s, actor, random)
  }

  private def travel(s: State, actor: CreatureId, random: Random)(enemy: Creature): Option[Result] = {
    TravelIntention(enemy.location)(s, actor, random)
  }
}
