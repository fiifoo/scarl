package fi.fiifoo.scarl.status

import fi.fiifoo.scarl.core.entity._
import fi.fiifoo.scarl.core.mutation.NewEntityMutation
import fi.fiifoo.scarl.core.test_assets.TestCreatureFactory
import fi.fiifoo.scarl.core.{Location, State}
import fi.fiifoo.scarl.effect.{DamageEffect, RemoveStatusEffect, TickEffect}
import org.scalatest._

class BurnStatusSpec extends FlatSpec with Matchers {

  "BurnStatus" should "burn burning creature" in {
    val creature = TestCreatureFactory.create(CreatureId(1))
    val status = BurnStatus(ActiveStatusId(2), 0, creature.id)
    val s = NewEntityMutation(status)(NewEntityMutation(creature)(State()))

    status.activate(s) should ===(List(
      TickEffect(status.id, status.interval),
      DamageEffect(creature.id, status.damage)
    ))
  }

  it should "burn creature in burning location" in {
    val creature = TestCreatureFactory.create(CreatureId(1))
    val container = Container(ContainerId(2), Location(0, 0))
    val status = BurnStatus(ActiveStatusId(3), 0, container.id)
    val s = NewEntityMutation(status)(NewEntityMutation(container)(NewEntityMutation(creature)(State())))

    status.activate(s) should ===(List(
      TickEffect(status.id, status.interval),
      DamageEffect(creature.id, status.damage)
    ))
  }

  it should "burn creatures in other locations" in {
    val creature = TestCreatureFactory.create(CreatureId(1), location = Location(0, 1))
    val container = Container(ContainerId(2), Location(0, 0))
    val status = BurnStatus(ActiveStatusId(3), 0, container.id)
    val s = NewEntityMutation(status)(NewEntityMutation(container)(NewEntityMutation(creature)(State())))

    status.activate(s) should ===(List(TickEffect(status.id, status.interval)))
  }

  it should "not burn non-creatures in burning location" in {
    val wall = Wall(WallId(1), Location(0, 0))
    val container = Container(ContainerId(2), Location(0, 0))
    val status = BurnStatus(ActiveStatusId(3), 0, container.id)
    val s = NewEntityMutation(status)(NewEntityMutation(container)(NewEntityMutation(wall)(State())))

    status.activate(s) should ===(List(TickEffect(status.id, status.interval)))
  }

  it should "remove itself if tick reaches given expire time" in {
    val creature = TestCreatureFactory.create(CreatureId(1))
    val status1 = BurnStatus(ActiveStatusId(2), 0, creature.id, Some(100))
    val status2 = BurnStatus(ActiveStatusId(3), 100, creature.id, Some(100))
    val s = NewEntityMutation(status2)(NewEntityMutation(status1)(NewEntityMutation(creature)(State())))

    status1.activate(s) should ===(List(
      TickEffect(status1.id, status1.interval),
      DamageEffect(creature.id, status1.damage)
    ))
    status2.activate(s) should ===(List(
      RemoveStatusEffect(status2.id),
      DamageEffect(creature.id, status1.damage)
    ))
  }

}
