package io.github.fiifoo.scarl.core.creature

import io.github.fiifoo.scarl.core.entity.CreaturePower

case class Events(death: Option[CreaturePower], hit: Option[CreaturePower])
