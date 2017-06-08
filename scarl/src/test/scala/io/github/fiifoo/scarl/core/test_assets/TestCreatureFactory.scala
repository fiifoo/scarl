package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.creature.Stats.{Melee, Sight}
import io.github.fiifoo.scarl.core.creature.{Character, FactionId, Stats}
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}
import io.github.fiifoo.scarl.core.kind.{CreatureKind, CreatureKindId}
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation
import io.github.fiifoo.scarl.core.{Location, State}

object TestCreatureFactory {

  val defaultStats = Stats(melee = Melee(attack = 1000, damage = 10), defence = 10, sight = Sight(5))
  val defaultKind = CreatureKind(
    id = CreatureKindId("creature"),
    name = "Creature",
    display = 'c',
    color = "white",
    faction = FactionId("people"),
    stats = defaultStats,
    character = None
  )

  def create(id: CreatureId = CreatureId(0),
             kind: CreatureKindId = defaultKind.id,
             faction: FactionId = FactionId("people"),
             tick: Int = 1,
             location: Location = Location(0, 0),
             health: Int = 0,
             damage: Int = 0,
             character: Option[Character] = None
            ): Creature = {
    Creature(
      id,
      kind,
      faction,
      location,
      tick,
      damage,
      defaultStats.copy(health = health),
      character
    )
  }

  def generate(s: State, count: Int = 1, prototype: Creature = create()): State = {

    val result = (0 until count).foldLeft(s)((s, _) => {
      val creature = prototype.copy(CreatureId(s.nextEntityId))

      NewEntityMutation(creature)(s)
    })

    if (result.kinds.creatures.isEmpty) {
      result.copy(kinds = result.kinds.copy(
        creatures = Map(defaultKind.id -> defaultKind)
      ))
    } else {
      result
    }
  }
}
