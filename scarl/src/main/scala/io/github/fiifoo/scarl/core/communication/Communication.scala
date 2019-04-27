package io.github.fiifoo.scarl.core.communication

import io.github.fiifoo.scarl.core.Text
import io.github.fiifoo.scarl.core.entity.{CreaturePower, ItemPower}

case class Communication(id: CommunicationId,
                         message: Text,
                         repeatable: Boolean = false,
                         creaturePower: Option[CreaturePower] = None,
                         itemPower: Option[ItemPower] = None,
                        )
