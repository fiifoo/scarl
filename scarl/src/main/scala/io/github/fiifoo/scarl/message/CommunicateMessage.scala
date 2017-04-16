package io.github.fiifoo.scarl.message

import io.github.fiifoo.scarl.core.communication.Message
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.effect.CommunicateEffect

class CommunicateMessage(player: () => CreatureId,
                         fov: () => Set[Location]
                        ) extends MessageBuilder[CommunicateEffect] {

  def apply(s: State, effect: CommunicateEffect): Option[String] = {
    val source = effect.source
    val target = effect.target

    if (target == player()) {
      val message = effect.communication map (_ (s)) collect {
        case message: Message => s"""${kind(s, source)} talks to you: "${message.value}""""
      } getOrElse {
        s"${kind(s, source)} does not respond."
      }

      Some(message)
    } else {
      None
    }
  }

  private def kind(s: State, creature: CreatureId): String = {
    s.kinds.creatures(creature(s).kind).name
  }
}
