package io.github.fiifoo.scarl.core.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.mutation.RemoveEntitiesMutation
import io.github.fiifoo.scarl.core.test_assets._
import org.scalatest._

class EffectResolverSpec extends FlatSpec with Matchers {

  def resolve(s: State, effects: List[Effect]): State = EffectResolver(s, effects)._1

  "EffectResolver" should "resolve damage to creature" in {
    val initial = 10
    val add = 5

    var s = TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(damage = initial))
    val creature = CreatureId(1)

    val effect = TestDamageEffect(creature, add)
    s = resolve(s, List(effect))

    creature(s).damage should ===(initial + add)
  }

  it should "resolve damage to multiple creatures" in {
    val initial = 10
    val add = 5

    var s = TestCreatureFactory.generate(State(), 2, TestCreatureFactory.create(damage = initial))
    val c1 = CreatureId(1)
    val c2 = CreatureId(2)

    val e1 = TestDamageEffect(c1, add)
    val e2 = TestDamageEffect(c2, add)
    s = resolve(s, List(e1, e2))

    c1(s).damage should ===(initial + add)
    c2(s).damage should ===(initial + add)
  }

  it should "resolve damage to same creature multiple times" in {
    val initial = 10
    val add = 5

    var s = TestCreatureFactory.generate(State(), 1, TestCreatureFactory.create(damage = initial))
    val creature = CreatureId(1)

    val e1 = TestDamageEffect(creature, add)
    val e2 = TestDamageEffect(creature, add)
    s = resolve(s, List(e1, e2))

    creature(s).damage should ===(initial + add + add)
  }

  it should "resolve chained effects" in {
    var s = TestCreatureFactory.generate(State())
    val creature = CreatureId(1)

    val effect = TestKaboomEffect(creature)
    s = resolve(s, List(effect))

    creature(s).damage should ===(effect.damage)
    creature(s).location should ===(effect.location)
  }

  it should "resolve entity add" in {
    var s = TestCreatureFactory.generate(State())
    val creature = CreatureId(1)

    val effect = TestActorStatusEffect(creature)

    s.entities.size should ===(1)
    s = resolve(s, List(effect))
    s.entities.size should ===(2)
    s.entities(ActiveStatusId(2)) should ===(TestActiveStatus(ActiveStatusId(2), 1, creature))
  }

  it should "resolve entity removal" in {
    var s = TestCreatureFactory.generate(State())
    val creature = CreatureId(1)

    val effect = TestEntityRemoveEffect(creature)

    creature(s)
    s = resolve(s, List(effect))
    s.tmp.removableEntities.size should ===(1)
    s = RemoveEntitiesMutation()(s)
    intercept[Exception] {
      creature(s)
    }
    s.tmp.removableEntities.size should ===(0)
  }

  it should "resolve entity removal with other effects to removed entity" in {
    var s = TestCreatureFactory.generate(State())
    val creature = CreatureId(1)

    val e1 = TestEntityRemoveEffect(creature)
    val e2 = TestDamageEffect(creature, 5)

    creature(s)
    s = resolve(s, List(e1, e2))
    s = RemoveEntitiesMutation()(s)
    intercept[Exception] {
      creature(s)
    }
  }

}
