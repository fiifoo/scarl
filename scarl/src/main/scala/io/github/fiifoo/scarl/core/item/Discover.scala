package io.github.fiifoo.scarl.core.item

sealed trait Discover

case object DiscoverTriggerer extends Discover

case object DiscoverEveryone extends Discover
