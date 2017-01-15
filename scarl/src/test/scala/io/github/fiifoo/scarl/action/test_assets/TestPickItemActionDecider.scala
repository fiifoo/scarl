package io.github.fiifoo.scarl.action.test_assets

import io.github.fiifoo.scarl.action.PickItemAction
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.{Action, ActionDecider}
import io.github.fiifoo.scarl.core.entity.{Container, ContainerId, Creature, Item}

object TestPickItemActionDecider extends ActionDecider {

  def apply(s: State, actor: Creature): Action = {
    val items = s.entities filter (_._2.isInstanceOf[Item]) map (_._2.asInstanceOf[Item])
    val here = items filter (item => {
      s.entities(item.container).isInstanceOf[Container] && item.container.asInstanceOf[ContainerId](s).location == actor.location
    })
    val item = here.head

    PickItemAction(item)
  }
}
