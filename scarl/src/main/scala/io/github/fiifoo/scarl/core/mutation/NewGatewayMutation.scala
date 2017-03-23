package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core._

case class NewGatewayMutation(location: Location) extends Mutation {
  def apply(s: State): State = {
    s.copy(
      gateways = s.gateways + location
    )
  }
}
