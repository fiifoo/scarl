package io.github.fiifoo.scarl.core.creature


case class Traits(events: Option[Events] = None,
                  flying: Boolean = false,
                  immobile: Boolean = false,
                  missile: Option[Missile] = None,
                  solitary: Boolean = false,
                 )
