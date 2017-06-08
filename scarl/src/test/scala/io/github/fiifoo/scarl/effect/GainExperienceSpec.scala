package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.{Character, Progression, ProgressionId, Stats}
import io.github.fiifoo.scarl.core.effect.EffectResolver
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import org.scalatest._

class GainExperienceSpec extends FlatSpec with Matchers {

  val resolve = new EffectResolver()

  val initialStats = TestCreatureFactory.defaultStats
  val addStats = Stats(health = 10)

  "GainExperienceEffect" should "add experience to creature" in {
    var (s, creature) = testStuff
    val effect = GainExperienceEffect(creature, 3)

    s = resolve(s, List(effect))
    creature(s).character.get.experience should ===(3)
    s = resolve(s, List(effect))
    creature(s).character.get.experience should ===(6)
  }

  it should "level up to creature" in {
    var (s, creature) = testStuff

    s = resolve(s, List(GainExperienceEffect(creature, 9)))
    creature(s).character.get.level should ===(1)
    creature(s).stats.health should ===(initialStats.health)
    s = resolve(s, List(GainExperienceEffect(creature, 1)))
    creature(s).character.get.level should ===(2)
    creature(s).stats.health should ===(initialStats.health + addStats.health)
  }

  it should "level up creature twice" in {
    var (s, creature) = testStuff
    val effect = GainExperienceEffect(creature, 50)

    s = resolve(s, List(effect, effect))
    creature(s).character.get.level should ===(3)
    creature(s).stats.health should ===(initialStats.health + addStats.health + addStats.health)
  }

  it should "not mistakenly level up creature twice with multiple effects" in {
    var (s, creature) = testStuff
    val effect = GainExperienceEffect(creature, 10)

    s = resolve(s, List(effect, effect))
    creature(s).character.get.level should ===(2)
    creature(s).character.get.experience should ===(20)
  }

  private def testStuff: (State, CreatureId) = {

    val progression = Progression(
      id = ProgressionId("some"),
      steps = Vector(
        Progression.Step(Progression.Requirements(10), addStats),
        Progression.Step(Progression.Requirements(50), addStats)
      )
    )

    val initial = State(progressions = Map(progression.id -> progression))
    val prototype = TestCreatureFactory.create(character = Some(Character(progression.id)))
    val state = TestCreatureFactory.generate(initial, 1, prototype)
    val creature = CreatureId(1)

    (state, creature)
  }
}
