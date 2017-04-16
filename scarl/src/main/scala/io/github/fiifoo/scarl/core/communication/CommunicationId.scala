package io.github.fiifoo.scarl.core.communication

import io.github.fiifoo.scarl.core.State

case class CommunicationId(value: String) {
  def apply(s: State): Communication = s.communications.data(this)
}
