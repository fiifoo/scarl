package io.github.fiifoo.scarl.core.test_assets

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.ai.Behavior
import io.github.fiifoo.scarl.core.creature.Stats.{Health, Melee, Sight}
import io.github.fiifoo.scarl.core.creature.{Character, FactionId, Party, Stats, Traits}
import io.github.fiifoo.scarl.core.entity.{Creature, CreatureId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.{CreatureKind, CreatureKindId}
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}

object TestCreatureFactory {

  val defaultStats = Stats(speed = 100, melee = Melee(attack = 1000, damage = 10), defence = 10, sight = Sight(5))
  val defaultKind = CreatureKind(
    id = CreatureKindId("creature"),
    name = "Creature",
    display = 'c',
    color = "white",
    faction = FactionId("people"),
    behavior = TestPassTactic,
    stats = defaultStats,
    character = None
  )

  def create(id: CreatureId = CreatureId(0),
             kind: CreatureKindId = defaultKind.id,
             faction: FactionId = FactionId("people"),
             behavior: Behavior = TestPassTactic,
             tick: Int = 1,
             location: Location = Location(0, 0),
             health: Int = 0,
             damage: Int = 0,
             dead: Boolean = false,
             character: Option[Character] = None
            ): Creature = {
    Creature(
      id = id,
      kind = kind,
      faction = faction,
      traits = Traits(solitary = true),
      party = Party(id),
      behavior = behavior,
      location = location,
      tick = tick,
      damage = damage,
      stats = defaultStats.copy(health = Health(health)),
      dead = dead,
      owner = None,
      character = character,
    )
  }

  def generate(s: State, count: Int = 1, prototype: Creature = create()): State = {

    val result = (0 until count).foldLeft(s)((s, _) => {
      val (nextId, nextIdSeq) = s.idSeq()
      val id = CreatureId(nextId)
      val creature = prototype.copy(id = id, party = Party(id))

      NewEntityMutation(creature)(IdSeqMutation(nextIdSeq)(s))
    })

    if (result.assets.kinds.creatures.isEmpty) {
      result.copy(
        assets = result.assets.copy(
          kinds = result.assets.kinds.copy(
            creatures = Map(defaultKind.id -> defaultKind)
          )))
    } else {
      result
    }
  }
}
