package io.github.fiifoo.scarl.ai

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.assets.Assets
import io.github.fiifoo.scarl.core.creature.{Faction, FactionId}
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import org.scalatest._

class SeekEnemySpec extends FlatSpec with Matchers {

  "SeekEnemy" should "find closest enemy" in {
    val faction = Faction(FactionId("people"), Set(FactionId("people")))
    val assets = Assets(factions = Map(faction.id -> faction))

    val c1 = TestCreatureFactory.create(CreatureId(1), faction = faction.id)
    val c2 = TestCreatureFactory.create(CreatureId(2), faction = faction.id, location = Location(5, 0))
    val c3 = TestCreatureFactory.create(CreatureId(3), faction = faction.id, location = Location(2, 0))

    val s = NewEntityMutation(c3)(NewEntityMutation(c2)(NewEntityMutation(c1)(State(assets = assets))))

    SeekEnemy(s, CreatureId(1)) should ===(Some(c3))
  }
}
