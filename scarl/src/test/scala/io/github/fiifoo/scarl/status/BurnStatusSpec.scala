package io.github.fiifoo.scarl.status

import io.github.fiifoo.scarl.core.effect.RemoveEntityEffect
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.WallKindId
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import io.github.fiifoo.scarl.core.{State, Time}
import io.github.fiifoo.scarl.effect.TickEffect
import io.github.fiifoo.scarl.effect.combat.DamageEffect
import io.github.fiifoo.scarl.status.test_assets.BurnStatus
import org.scalatest._

class BurnStatusSpec extends FlatSpec with Matchers {

  "BurnStatus" should "burn burning creature" in {
    val creature = TestCreatureFactory.create(CreatureId(1))
    val status = BurnStatus(ActiveStatusId(2), 0, creature.id)
    val s = NewEntityMutation(status)(NewEntityMutation(creature)(State()))

    status(s) should ===(List(
      TickEffect(status.id),
      DamageEffect(creature.id, status.damage)
    ))
  }

  it should "burn creature in burning location" in {
    val creature = TestCreatureFactory.create(CreatureId(1))
    val container = Container(ContainerId(2), Location(0, 0))
    val status = BurnStatus(ActiveStatusId(3), 0, container.id)
    val s = NewEntityMutation(status)(NewEntityMutation(container)(NewEntityMutation(creature)(State())))

    status(s) should ===(List(
      TickEffect(status.id),
      DamageEffect(creature.id, status.damage)
    ))
  }

  it should "not burn creatures in other locations" in {
    val creature = TestCreatureFactory.create(CreatureId(1), location = Location(0, 1))
    val container = Container(ContainerId(2), Location(0, 0))
    val status = BurnStatus(ActiveStatusId(3), 0, container.id)
    val s = NewEntityMutation(status)(NewEntityMutation(container)(NewEntityMutation(creature)(State())))

    status(s) should ===(List(TickEffect(status.id)))
  }

  it should "not burn non-creatures in burning location" in {
    val wall = Wall(WallId(1), WallKindId("wall"), Location(0, 0))
    val container = Container(ContainerId(2), Location(0, 0))
    val status = BurnStatus(ActiveStatusId(3), 0, container.id)
    val s = NewEntityMutation(status)(NewEntityMutation(container)(NewEntityMutation(wall)(State())))

    status(s) should ===(List(TickEffect(status.id)))
  }

  it should "remove itself if tick reaches given expire time" in {
    val creature = TestCreatureFactory.create(CreatureId(1))
    val status1 = BurnStatus(ActiveStatusId(2), 0, creature.id, Some(Time.turn))
    val status2 = BurnStatus(ActiveStatusId(3), Time.turn, creature.id, Some(Time.turn))
    val s = NewEntityMutation(status2)(NewEntityMutation(status1)(NewEntityMutation(creature)(State())))

    status1(s) should ===(List(
      TickEffect(status1.id),
      DamageEffect(creature.id, status1.damage)
    ))
    status2(s) should ===(List(
      RemoveEntityEffect(status2.id),
      DamageEffect(creature.id, status1.damage)
    ))
  }

}
