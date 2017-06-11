package io.github.fiifoo.scarl.core.power

case class Powers(creatures: Map[CreaturePowerId, CreaturePower] = Map(),
                  items: Map[ItemPowerId, ItemPower] = Map()
                 )
