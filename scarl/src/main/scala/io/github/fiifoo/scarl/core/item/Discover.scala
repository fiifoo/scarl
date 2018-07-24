package io.github.fiifoo.scarl.core.item

sealed trait Discover

object Discover {

  case object Everyone extends Discover

  case object Nobody extends Discover

  case object Triggerer extends Discover

}
