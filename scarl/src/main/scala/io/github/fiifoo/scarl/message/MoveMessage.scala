package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.Selectors.{getContainerItems, getLocationEntities}
import io.github.fiifoo.scarl.core.entity.{ContainerId, CreatureId, ItemId}
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.MoveEffect

class MoveMessage(player: () => CreatureId,
                  fov: () => Set[Location]
                 ) extends MessageBuilder[MoveEffect] {

  def apply(s: State, effect: MoveEffect): Option[String] = {
    val target = effect.target

    if (target == player()) {
      val entities = getLocationEntities(s)(effect.location)
      val containers = entities collect { case c: ContainerId => c }
      val items = containers flatMap getContainerItems(s)
      val messages = items map (item => s"${kind(s, item)}.")

      if (messages.nonEmpty) {
        Some(messages.mkString(" "))
      } else {
        None
      }
    } else {
      None
    }
  }

  private def kind(s: State, item: ItemId): String = {
    s.kinds.items(item(s).kind).name
  }
}
