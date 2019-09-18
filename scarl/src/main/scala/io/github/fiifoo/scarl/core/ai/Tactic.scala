package io.github.fiifoo.scarl.core.ai

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.CreatureId

import scala.util.Random

object Tactic {
  type Result = (Tactic, Action)
}

trait Tactic {
  val intentions: List[(Intention, Priority.Value)]

  def valid(s: State, actor: CreatureId): Boolean = true

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    if (!valid(s, actor)) {
      return None
    }

    mergeIntentions(s, actor) foreach (x => {
      val (intention, _) = x

      intention(s, actor, random) foreach (result => {
        return Some(result)
      })
    })

    None
  }

  def mergeIntentions(s: State, actor: CreatureId): List[(Intention, Priority.Value)] = {
    val brains = s.factions.brains.get(actor(s).faction) flatMap (_.intentions.get(actor))

    brains map (brains => {
      (brains ::: intentions) sortWith ((a, b) => a._2 > b._2)
    }) getOrElse {
      intentions
    }
  }
}
