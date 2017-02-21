package models

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.widget.{DelayedTransformingWidget, HealLocationWidget, SummonCreatureWidget, TriggeredTransformingWidget}

object Data {

  def factions: Map[FactionId, Faction] = {
    val justice = Faction(
      id = FactionId("justice"),
      enemies = Set(FactionId("chaos"))
    )
    val chaos = Faction(
      id = FactionId("chaos"),
      enemies = Set(FactionId("justice"))
    )

    Map(
      justice.id -> justice,
      chaos.id -> chaos
    )
  }

  def kinds: Kinds = {
    Kinds(
      creatures,
      items,
      terrains,
      walls,
      widgets
    )
  }

  private def creatures = {
    val hero = CreatureKind(CreatureKindId("hero"), "Hero", '@', "yellow", FactionId("justice"), Creature.Stats(
      health = 20,
      attack = 20,
      defence = 20,
      damage = 10,
      armor = 5
    ))

    val avatarOfJustice = CreatureKind(CreatureKindId("avatar-of-justice"), "Avatar of Justice", 'A', "white", FactionId("justice"), Creature.Stats(
      health = 1000,
      attack = 20,
      defence = 20,
      damage = 10,
      armor = 5
    ))

    val houndOfChaos = CreatureKind(CreatureKindId("hound-of-chaos"), "Hound of Chaos", 'c', "purple", FactionId("chaos"), Creature.Stats(
      health = 10,
      attack = 10,
      defence = 10,
      damage = 10,
      armor = 5
    ))

    Map(
      hero.id -> hero,
      avatarOfJustice.id -> avatarOfJustice,
      houndOfChaos.id -> houndOfChaos
    )
  }

  private def items = {
    val portal = ItemKind(ItemKindId("chaos-portal"), "Chaos portal", 'O', "red")
    val greyAltar = ItemKind(ItemKindId("grey-altar"), "Grey altar", 'T', "grey")
    val whiteAltar = ItemKind(ItemKindId("white-altar"), "White altar", 'T', "white")
    val shiningAltar = ItemKind(ItemKindId("shining-altar"), "Shining altar", 'T', "yellow")

    Map(
      portal.id -> portal,
      greyAltar.id -> greyAltar,
      whiteAltar.id -> whiteAltar,
      shiningAltar.id -> shiningAltar
    )
  }

  private def terrains = {
    val stone = TerrainKind(TerrainKindId("stone-floor"), "Stone floor", '.', "lightgray")

    Map(
      stone.id -> stone
    )
  }

  private def walls = {
    val stone = WallKind(WallKindId("stone-wall"), "Stone wall", '#', "darkgray")

    Map(
      stone.id -> stone
    )
  }

  private def widgets = {
    val portal = SummonCreatureWidget(
      id = WidgetKindId("chaos-portal-widget"),
      item = ItemKindId("chaos-portal"),
      summon = CreatureKindId("hound-of-chaos"),
      interval = 1000
    )

    val activateAltar = TriggeredTransformingWidget(
      id = WidgetKindId("activate-healing-altar-widget"),
      item = ItemKindId("white-altar"),
      transformTo = WidgetKindId("active-healing-altar-widget"),
      transformDescription = Some("Altar shines in golden light.")
    )

    val activeAltar = HealLocationWidget(
      id = WidgetKindId("active-healing-altar-widget"),
      item = ItemKindId("shining-altar"),
      amount = 5,
      interval = 100,
      duration = Some(400),
      transformTo = Some(WidgetKindId("inactive-healing-altar-widget")),
      transformDescription = Some("Altar goes dark.")
    )

    val inactiveAltar = DelayedTransformingWidget(
      id = WidgetKindId("inactive-healing-altar-widget"),
      item = ItemKindId("grey-altar"),
      transformTo = WidgetKindId("activate-healing-altar-widget"),
      delay = 5000
    )

    Map(
      portal.id -> portal,
      activateAltar.id -> activateAltar,
      activeAltar.id -> activeAltar,
      inactiveAltar.id -> inactiveAltar
    )
  }
}
