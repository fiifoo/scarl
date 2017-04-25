package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.EffectResolver
import io.github.fiifoo.scarl.core.entity.Creature.Stats
import io.github.fiifoo.scarl.core.entity.{CreatureId, Item, ItemId}
import io.github.fiifoo.scarl.core.equipment._
import io.github.fiifoo.scarl.core.kind.ItemKindId
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation
import io.github.fiifoo.scarl.core.test_assets.TestCreatureFactory
import org.scalatest._

class EquipItemEffectSpec extends FlatSpec with Matchers {

  val resolve = new EffectResolver()

  "EquipItemEffect" should "equip item" in {
    var (s, creature, items) = testStuff
    val effect = EquipItemEffect(creature, items("sword").id, MainHand)

    s = resolve(s, List(effect))
    s.equipments should ===(Map(
      creature -> Map(MainHand -> items("sword").id)
    ))
  }

  it should "unequip previous item in same slot" in {
    var (s, creature, items) = testStuff
    val e1 = EquipItemEffect(creature, items("sword").id, MainHand)
    val e2 = EquipItemEffect(creature, items("shield").id, OffHand)
    val e3 = EquipItemEffect(creature, items("otherSword").id, MainHand)

    s = resolve(s, List(e1, e2))
    s.equipments should ===(Map(
      creature -> Map(MainHand -> items("sword").id, OffHand -> items("shield").id)
    ))
    s = resolve(s, List(e3))
    s.equipments should ===(Map(
      creature -> Map(MainHand -> items("otherSword").id, OffHand -> items("shield").id)
    ))
  }

  it should "unequip equipped item from any slot" in {
    var (s, creature, items) = testStuff
    val e1 = EquipItemEffect(creature, items("sword").id, MainHand)
    val e2 = EquipItemEffect(creature, items("sword").id, OffHand)

    s = resolve(s, List(e1, e2))
    s.equipments should ===(Map(
      creature -> Map(OffHand -> items("sword").id)
    ))
  }

  it should "work correctly with item with multiple slots" in {
    var (s, creature, items) = testStuff
    val e1 = EquipItemEffect(creature, items("sword").id, MainHand)
    val e2 = EquipItemEffect(creature, items("shield").id, OffHand)
    val e3 = EquipItemEffect(creature, items("bigSword").id, MainHand)

    s = resolve(s, List(e1, e2, e3))
    s.equipments should ===(Map(
      creature -> Map(MainHand -> items("bigSword").id, OffHand -> items("bigSword").id)
    ))
    s = resolve(s, List(e3))
    s.equipments should ===(Map(
      creature -> Map(MainHand -> items("bigSword").id, OffHand -> items("bigSword").id)
    ))
  }

  it should "not equip item in incorrect slot" in {
    var (s, creature, items) = testStuff
    val effect = EquipItemEffect(creature, items("shield").id, MainHand)

    s = resolve(s, List(effect))
    s.equipments should ===(Map())
  }

  it should "equip item with multiple equipments correctly" in {
    var (s, creature, items) = testStuff
    val effect = EquipItemEffect(creature, items("armorOrSword").id, HeadArmor)

    s = resolve(s, List(effect))
    s.equipments should ===(Map(
      creature -> Map(HeadArmor -> items("armorOrSword").id)
    ))
  }

  it should "calculate equipment stats correctly" in {
    var (s, creature, items) = testStuff
    val e1 = EquipItemEffect(creature, items("sword").id, MainHand)
    val e2 = EquipItemEffect(creature, items("shield").id, OffHand)
    val e3 = EquipItemEffect(creature, items("bigSword").id, MainHand)
    val e4 = EquipItemEffect(creature, items("armorOrSword").id, HeadArmor)

    s = resolve(s, List(e1, e2, e3, e3, e4))
    s.index.equipmentStats should ===(Map(
      creature -> Stats(
        attack = 1,
        damage = 1,
        armor = 1
      )
    ))
  }

  private def testStuff: (State, CreatureId, Map[String, Item]) = {
    val initial = TestCreatureFactory.generate(State())
    val creature = CreatureId(1)

    val sword = Item(
      id = ItemId(2),
      kind = ItemKindId("fake"),
      container = creature,
      pickable = true,
      weapon = Some(Weapon(
        stats = Stats(attack = 1),
        twoHanded = false
      ))
    )

    val otherSword = Item(
      id = ItemId(3),
      kind = ItemKindId("fake"),
      container = creature,
      pickable = true,
      weapon = Some(Weapon(
        stats = Stats(attack = 1),
        twoHanded = false
      ))
    )

    val bigSword = Item(
      id = ItemId(4),
      kind = ItemKindId("fake"),
      container = creature,
      pickable = true,
      weapon = Some(Weapon(
        stats = Stats(attack = 1, damage = 1),
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
        stats = Stats(attack = 1),
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

    (state, creature, Map(
      "sword" -> sword,
      "otherSword" -> otherSword,
      "bigSword" -> bigSword,
      "shield" -> shield,
      "armorOrSword" -> armorOrSword
    ))
  }
}
