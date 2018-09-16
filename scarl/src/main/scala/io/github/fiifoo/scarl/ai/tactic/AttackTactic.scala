package io.github.fiifoo.scarl.ai.tactic

import io.github.fiifoo.scarl.ai.intention.Utils.isEnemy
import io.github.fiifoo.scarl.ai.intention.{AttackIntention, ChargeIntention, PursueIntention}
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.{Intention, Priority, Tactic}
import io.github.fiifoo.scarl.core.entity.{CreatureId, SafeCreatureId}
import io.github.fiifoo.scarl.core.geometry._

case class AttackTactic(target: SafeCreatureId, destination: Location, enableMove: Boolean = true) extends Tactic {

  val intentions: List[(Intention, Priority.Value)] = if (enableMove) {
    List((
      AttackIntention(target),
      Priority.top
    ), (
      ChargeIntention(target),
      Priority.high
    ), (
      PursueIntention(target, destination),
      Priority.medium
    ))
  } else {
    List((
      AttackIntention(target, enableMove = false),
      Priority.top
    ))
  }

  override def valid(s: State, actor: CreatureId): Boolean = {
    target(s) exists (target => {
      isEnemy(s, actor)(target.id)
    })
  }
}
