package io.github.fiifoo.scarl.generate

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{CreatureId, Faction, FactionId}
import io.github.fiifoo.scarl.core.mutation.NewFactionMutation

object Generate {
  def apply(): (State, CreatureId) = {
    val (s1, us, them) = factions(State())
    val (s2, player) = creatures(s1, us, them)
    val s3 = walls(s2)

    (s3, player)
  }

  private def factions(s: State): (State, FactionId, FactionId) = {
    val us = FactionId("us")
    val them = FactionId("them")

    val s1 = NewFactionMutation(Faction(us, Set(them)))(s)
    val s2 = NewFactionMutation(Faction(them, Set(us)))(s1)

    (s2, us, them)
  }

  private def creatures(s: State, us: FactionId, them: FactionId): (State, CreatureId) = {
    val player = CreatureId(s.nextEntityId)
    val s1 = CreatureFactory().generate(s, 2, us)
    val s2 = CreatureFactory().generate(s1, 100, them)

    (s2, player)
  }

  private def walls(s: State): State = {
    WallFactory().generate(s, 500)
  }
}
