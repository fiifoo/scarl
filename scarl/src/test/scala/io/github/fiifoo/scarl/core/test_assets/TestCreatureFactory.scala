package fi.fiifoo.scarl.core.test_assets

import fi.fiifoo.scarl.core.entity.{Creature, CreatureId}
import fi.fiifoo.scarl.core.mutation.NewEntityMutation
import fi.fiifoo.scarl.core.{Location, State}

object TestCreatureFactory {

  def create(id: CreatureId = CreatureId(0),
             tick: Int = 1,
             location: Location = Location(0, 0),
             health: Int = 0,
             damage: Int = 0
            ): Creature = Creature(id, location, tick, health, damage)

  def generate(s: State, count: Int = 1, prototype: Creature = create()): State = {

    (0 until count).foldLeft(s)((s, i) => {
      val creature = prototype.copy(CreatureId(s.nextEntityId))

      NewEntityMutation(creature)(s)
    })
  }
}
