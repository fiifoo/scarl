package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.entity.Creature.Stats
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId, FactionId}
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation
import io.github.fiifoo.scarl.core.{Location, State}

object TestCreatureFactory {

  def create(id: CreatureId = CreatureId(0),
             kind: CreatureKindId = CreatureKindId("creature"),
             faction: FactionId = FactionId("people"),
             tick: Int = 1,
             location: Location = Location(0, 0),
             health: Int = 0,
             damage: Int = 0
            ): Creature = Creature(id, kind, faction, location, tick, damage, Stats(health, 1000, 10, 10, 0))

  def generate(s: State, count: Int = 1, prototype: Creature = create()): State = {

    (0 until count).foldLeft(s)((s, _) => {
      val creature = prototype.copy(CreatureId(s.nextEntityId))

      NewEntityMutation(creature)(s)
    })
  }
}
