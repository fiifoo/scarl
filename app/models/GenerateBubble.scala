package models

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.{CreatureKindId, Kinds, WidgetKindId}
import io.github.fiifoo.scarl.generate.Generator

object GenerateBubble {

  val generate = new Generator()

  def apply(factions: Map[FactionId, Faction], kinds: Kinds): (State, CreatureId) = {
    val s0 = State(kinds = kinds)
    val s1 = generate.factions(s0, factions)
    val s2 = generate.walls(s1, 500, kinds.walls.values.head)
    val s3 = generate.creatures(s2, 1, kinds.creatures(CreatureKindId("avatar-of-justice")))
    val s4 = generate.widgets(s3, 10, kinds.widgets(WidgetKindId("chaos-portal-widget")))
    val s5 = generate.widgets(s4, 10, kinds.widgets(WidgetKindId("activate-healing-altar-widget")))

    generate.player(s5, kinds.creatures(CreatureKindId("hero")))
  }
}
