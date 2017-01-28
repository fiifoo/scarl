package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.PickItemAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.{Action, Tactic}
import io.github.fiifoo.scarl.core.entity._

case class TestPickItemTactic(actor: CreatureId) extends Tactic {

  def apply(s: State): (Tactic, Action) = {
    val items = s.entities filter (_._2.isInstanceOf[Item]) map (_._2.asInstanceOf[Item])
    val here = items filter (item => {
      s.entities(item.container).isInstanceOf[Container] && item.container.asInstanceOf[ContainerId](s).location == actor(s).location
    })
    val item = here.head

    (this, PickItemAction(item))
  }
}
