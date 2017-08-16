package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.action.{Behavior, PassTactic}
import io.github.fiifoo.scarl.core.creature.Stats.{Melee, Sight}
import io.github.fiifoo.scarl.core.creature.{Character, FactionId, Party, Stats}
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
    behavior = PassTactic,
    stats = defaultStats,
    character = None
  )

  def create(id: CreatureId = CreatureId(0),
             kind: CreatureKindId = defaultKind.id,
             faction: FactionId = FactionId("people"),
             behavior: Behavior = PassTactic,
             tick: Int = 1,
             location: Location = Location(0, 0),
             health: Int = 0,
             damage: Int = 0,
             dead: Boolean = false,
             character: Option[Character] = None
            ): Creature = {
    Creature(
      id,
      kind,
      faction,
      solitary = true,
      party = Party(id),
      behavior,
      location,
      tick,
      damage,
      stats = defaultStats.copy(health = health),
      dead,
      owner = None,
      character
    )
  }

  def generate(s: State, count: Int = 1, prototype: Creature = create()): State = {

    val result = (0 until count).foldLeft(s)((s, _) => {
      val id = CreatureId(s.nextEntityId)
      val creature = prototype.copy(id = id, party = Party(id))

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
