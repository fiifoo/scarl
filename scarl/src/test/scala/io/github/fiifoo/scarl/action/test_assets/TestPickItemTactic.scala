package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.PickItemAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Behavior
import io.github.fiifoo.scarl.core.ai.Tactic.Result
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Location

import scala.util.Random

case object TestPickItemTactic extends Behavior {

  def behavior(s: State, actor: CreatureId, random: Random): Result = {

    def getItemLocation(item: Item): Option[Location] = {
      item.container(s) match {
        case c: Container => Some(c.location)
        case _ => None
      }
    }

    val location = actor(s).location

    val here = s.entities.values collect {
      case i: Item if getItemLocation(i) contains location => i
    }

    (this, PickItemAction(here.head.id))
  }
}
