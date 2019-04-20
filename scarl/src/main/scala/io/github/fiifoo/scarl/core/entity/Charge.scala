package io.github.fiifoo.scarl.core.entity

import io.github.fiifoo.scarl.core.kind.KindId

case class Charge(charges: Int,
                  transformTo: Option[KindId] = None,
                  transformDescription: Option[String] = None
                 )
