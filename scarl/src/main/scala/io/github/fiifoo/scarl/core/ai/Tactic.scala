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

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    intentions foreach (x => {
      val (intention, _) = x

      intention(s, actor, random) foreach (result => {
        return Some(result)
      })
    })

    None
  }
}
