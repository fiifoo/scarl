package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.entity.{ActiveStatusId, CreatureId, Item, PassiveStatusId}
import io.github.fiifoo.scarl.core.test_assets.{TestActiveStatus, TestCreatureFactory, TestItemFactory, TestStatus}
import io.github.fiifoo.scarl.core.world.ConduitId
import io.github.fiifoo.scarl.core.{IdSeq, Location, State}
import org.scalatest._

class ConduitEntryMutationSpec extends FlatSpec with Matchers {

  val creature = CreatureId(2)

  "ConduitEntryMutation" should "transfer creature (with other mutations)" in {
    val initial = createInitial()

    var state = ConduitEntryMutation(creature, ConduitId(1))(initial)
    val traveler = state.tmp.conduitEntry.get._2

    state = RemovableEntityMutation(creature)(state)
    state = RemoveEntitiesMutation()(state)
    state = ResetConduitEntryMutation()(state)
    state = ConduitExitMutation(traveler, Location(0, 0))(state)

    state should ===(initial)
  }

  private def createInitial(): State = {
    var s = State()

    s = TestCreatureFactory.generate(s, 3)
    s = TestItemFactory.generate(s, 2, CreatureId(1))
    s = TestItemFactory.generate(s, 2, CreatureId(2))
    s = TestItemFactory.generate(s, 2, CreatureId(3))

    val item = s.entities.values.collectFirst {
      case item: Item if item.container == creature => item
    }.get

    s = NewEntityMutation(TestActiveStatus(ActiveStatusId(s.idSeq.value), s.tick, creature))(s)
    s = NewEntityMutation(TestActiveStatus(ActiveStatusId(s.idSeq.value + 1), s.tick + 50, creature))(s)
    s = NewEntityMutation(TestStatus(PassiveStatusId(s.idSeq.value + 2), creature))(s)
    s = NewEntityMutation(TestStatus(PassiveStatusId(s.idSeq.value + 3), item.id))(s)
    s = IdSeqMutation(IdSeq(s.idSeq.value + 3))(s)

    s
  }
}
