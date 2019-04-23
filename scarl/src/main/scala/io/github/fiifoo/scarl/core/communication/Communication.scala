package io.github.fiifoo.scarl.core.communication

import io.github.fiifoo.scarl.core.Text

case class Communication(id: CommunicationId,
                         message: Text,
                         repeatable: Boolean = false
                        )
