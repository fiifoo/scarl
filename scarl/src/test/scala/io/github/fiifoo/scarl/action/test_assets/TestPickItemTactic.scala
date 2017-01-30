package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.PickItemAction
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.{Location, State}

case class TestPickItemTactic(actor: CreatureId) extends Tactic {

  def apply(s: State): (Tactic, Action) = {

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
