package io.github.fiifoo.scarl.action

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.communication.CommunicationId
import io.github.fiifoo.scarl.core.effect.Effect
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.effect.{CommunicateEffect, TickEffect}

case class CommunicateAction(target: CreatureId, communication: Option[CommunicationId] = None) extends Action {
  val cost = 100

  def apply(s: State, actor: CreatureId): List[Effect] = {
    List(
      TickEffect(actor, cost),
      CommunicateEffect(actor, target, communication)
    )
  }
}
