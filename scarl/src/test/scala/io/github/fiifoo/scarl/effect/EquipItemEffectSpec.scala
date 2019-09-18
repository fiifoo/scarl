package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stats
import io.github.fiifoo.scarl.core.creature.Stats.Melee
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResolver}
import io.github.fiifoo.scarl.core.entity.{CreatureId, Item, ItemId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.item.Equipment._
import io.github.fiifoo.scarl.core.item._
import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import io.github.fiifoo.scarl.effect.interact.EquipItemEffect
import org.scalatest._

class EquipItemEffectSpec extends FlatSpec with Matchers {

  def resolve(s: State, effects: List[Effect]): State = EffectResolver(s, effects)._1

  "EquipItemEffect" should "equip item" in {
    var (s, creature, location, items) = testStuff
    val effect = EquipItemEffect(creature, items("sword").id, MainHand, location)

    s = resolve(s, List(effect))
    s.creature.equipments should ===(Map(
      creature -> Map(MainHand -> items("sword").id)
    ))
  }

  it should "unequip previous item in same slot" in {
    var (s, creature, location, items) = testStuff
    val e1 = EquipItemEffect(creature, items("sword").id, MainHand, location)
    val e2 = EquipItemEffect(creature, items("shield").id, OffHand, location)
    val e3 = EquipItemEffect(creature, items("otherSword").id, MainHand, location)

    s = resolve(s, List(e1, e2))
    s.creature.equipments should ===(Map(
      creature -> Map(MainHand -> items("sword").id, OffHand -> items("shield").id)
    ))
    s = resolve(s, List(e3))
    s.creature.equipments should ===(Map(
      creature -> Map(MainHand -> items("otherSword").id, OffHand -> items("shield").id)
    ))
  }

  it should "unequip equipped item from any slot" in {
    var (s, creature, location, items) = testStuff
    val e1 = EquipItemEffect(creature, items("sword").id, MainHand, location)
    val e2 = EquipItemEffect(creature, items("sword").id, OffHand, location)

    s = resolve(s, List(e1, e2))
    s.creature.equipments should ===(Map(
      creature -> Map(OffHand -> items("sword").id)
    ))
  }

  it should "work correctly with item with multiple slots" in {
    var (s, creature, location, items) = testStuff
    val e1 = EquipItemEffect(creature, items("sword").id, MainHand, location)
    val e2 = EquipItemEffect(creature, items("shield").id, OffHand, location)
    val e3 = EquipItemEffect(creature, items("bigSword").id, MainHand, location)

    s = resolve(s, List(e1, e2, e3))
    s.creature.equipments should ===(Map(
      creature -> Map(MainHand -> items("bigSword").id, OffHand -> items("bigSword").id)
    ))
    s = resolve(s, List(e3))
    s.creature.equipments should ===(Map(
      creature -> Map(MainHand -> items("bigSword").id, OffHand -> items("bigSword").id)
    ))
  }

  it should "not equip item in incorrect slot" in {
    var (s, creature, location, items) = testStuff
    val effect = EquipItemEffect(creature, items("shield").id, MainHand, location)

    s = resolve(s, List(effect))
    s.creature.equipments should ===(Map())
  }

  it should "equip item with multiple equipments correctly" in {
    var (s, creature, location, items) = testStuff
    val effect = EquipItemEffect(creature, items("armorOrSword").id, HeadArmor, location)

    s = resolve(s, List(effect))
    s.creature.equipments should ===(Map(
      creature -> Map(HeadArmor -> items("armorOrSword").id)
    ))
  }

  it should "calculate equipment stats correctly" in {
    var (s, creature, location, items) = testStuff
    val e1 = EquipItemEffect(creature, items("sword").id, MainHand, location)
    val e2 = EquipItemEffect(creature, items("shield").id, OffHand, location)
    val e3 = EquipItemEffect(creature, items("bigSword").id, MainHand, location)
    val e4 = EquipItemEffect(creature, items("armorOrSword").id, HeadArmor, location)

    s = resolve(s, List(e1, e2, e3, e3, e4))
    s.cache.equipmentStats should ===(Map(
      creature -> Stats(
        melee = Melee(attack = 1, damage = 1),
        armor = 1
      )
    ))
  }

  private def testStuff: (State, CreatureId, Location, Map[String, Item]) = {
    val initial = TestCreatureFactory.generate(State())
    val creature = CreatureId(1)

    val sword = Item(
      id = ItemId(2),
      kind = ItemKindId("fake"),
      container = creature,
      pickable = true,
      weapon = Some(Weapon(
        stats = Stats(melee = Melee(attack = 1)),
        twoHanded = false
      ))
    )

    val otherSword = Item(
      id = ItemId(3),
      kind = ItemKindId("fake"),
      container = creature,
      pickable = true,
      weapon = Some(Weapon(
        stats = Stats(melee = Melee(attack = 1)),
        twoHanded = false
      ))
    )

    val bigSword = Item(
      id = ItemId(4),
      kind = ItemKindId("fake"),
      container = creature,
      pickable = true,
      weapon = Some(Weapon(
        stats = Stats(melee = Melee(attack = 1, damage = 1)),
        twoHanded = true
      ))
    )

    val shield = Item(
      id = ItemId(5),
      kind = ItemKindId("fake"),
      container = creature,
      pickable = true,
      shield = Some(Shield(
        stats = Stats(defence = 1)
      ))
    )

    val armorOrSword = Item(
      id = ItemId(6),
      kind = ItemKindId("fake"),
      container = creature,
      pickable = true,
      armor = Some(Armor(
        stats = Stats(armor = 1),
        slot = HeadArmor
      )),
      weapon = Some(Weapon(
        stats = Stats(melee = Melee(attack = 1)),
        twoHanded = false
      ))
    )

    val state = List(
      sword,
      otherSword,
      bigSword,
      shield,
      armorOrSword
    ).foldLeft(initial)((state, item) => NewEntityMutation(item)(state))

    (
      state, creature, creature(state).location, Map(
      "sword" -> sword,
      "otherSword" -> otherSword,
      "bigSword" -> bigSword,
      "shield" -> shield,
      "armorOrSword" -> armorOrSword
    ))
  }
}
