package io.github.fiifoo.scarl.ai.intention

import io.github.fiifoo.scarl.ai.tactic.EscapeTactic
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Intention
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location

import scala.util.Random

case class EscapeIntention(source: Location, destination: Location, waiting: Boolean = false) extends Intention {

  def apply(s: State, actor: CreatureId, random: Random): Option[Result] = {
    if (actor(s).location != destination) {
      Utils.travel(s, actor, destination, displace = true, wait = !waiting) map (action => {
        (EscapeTactic(source, destination, Utils.waited(action)), action)
      })
    } else {
      None
    }
  }
}
