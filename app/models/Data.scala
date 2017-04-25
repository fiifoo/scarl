package models

import io.github.fiifoo.scarl.area.shape.Rectangle
import io.github.fiifoo.scarl.area.template.Template.FixedContent
import io.github.fiifoo.scarl.area.template.{FixedTemplate, RandomizedTemplate, Template, TemplateId}
import io.github.fiifoo.scarl.area.{Area, AreaId}
import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.Rng.WeightedChoices
import io.github.fiifoo.scarl.core.character.{Progression, ProgressionId}
import io.github.fiifoo.scarl.core.communication.{Communication, CommunicationId, Message}
import io.github.fiifoo.scarl.core.entity.Creature.{Sight, Stats}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.equipment.Weapon
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.widget.{DelayedTransformingWidget, HealLocationWidget, SummonCreatureWidget, TriggeredTransformingWidget}

object Data {

  def areas: Map[AreaId, Area] = {
    val first = Area(
      id = AreaId("first"),
      template = TemplateId("main"),
      conduits = List(
        (AreaId("second"), ItemKindId("stairs-down"), ItemKindId("stairs-up")),
        (AreaId("second"), ItemKindId("stairs-down"), ItemKindId("stairs-up"))
      )
    )
    val second = Area(
      id = AreaId("second"),
      template = TemplateId("main")
    )

    Map(
      first.id -> first,
      second.id -> second
    )
  }

  def communications: Map[CommunicationId, Communication] = {
    val greetJustice1 = Message(
      id = CommunicationId("greet-justice-1"),
      value = "I greet you. We will fight by your side."
    )
    val greetJustice2 = Message(
      id = CommunicationId("greet-justice-2"),
      value = "Justice prevails."
    )
    val responseJustice1 = Message(
      id = CommunicationId("response-justice-1"),
      value = "Let us strike at our foes."
    )
    val responseJustice2 = Message(
      id = CommunicationId("response-justice-2"),
      value = "Chaos will be undone."
    )

    Map(
      greetJustice1.id -> greetJustice1,
      greetJustice2.id -> greetJustice2,
      responseJustice1.id -> responseJustice1,
      responseJustice2.id -> responseJustice2
    )
  }

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

  def progressions: Map[ProgressionId, Progression] = {
    val add = Stats(
      health = 2,
      attack = 1,
      defence = 1,
      damage = 1
    )

    val steps = (1 to 10)
      .map(i => Progression.Step(Progression.Requirements(10 * i), add))
      .toVector

    val some = Progression(
      id = ProgressionId("some"),
      steps = steps
    )

    Map(
      some.id -> some
    )
  }

  def templates: Map[TemplateId, Template] = {
    val main = RandomizedTemplate(
      id = TemplateId("main"),
      shape = Rectangle(80, 25, 0),
      templates = List(
        (TemplateId("big-room"), 1, 1),
        (TemplateId("room"), 3, 4),
        (TemplateId("gateway-room"), 1, 1)
      ),
      border = Some(WallKindId("stone-wall")),
      fill = Some(WallKindId("stone-wall")),
      content = Template.RandomizedContent(
        creatures = List((CreatureKindId("hound-of-chaos"), 4, 7))
      )
    )
    val bigRoom = RandomizedTemplate(
      id = TemplateId("big-room"),
      shape = Rectangle(30, 20, 0),
      templates = List(
        (TemplateId("room"), 1, 1)
      ),
      entrances = List((Some(ItemKindId("opened-door")), 4, 4)),
      border = Some(WallKindId("stone-wall")),
      terrain = Some(TerrainKindId("rubble")),
      content = Template.RandomizedContent(
        creatures = List((CreatureKindId("avatar-of-justice"), 5, 5)),
        items = List(
          (ItemKindId("grey-altar"), 5, 5),
          (ItemKindId("long-sword"), 0, 2),
          (ItemKindId("claymore"), 0, 2)
        ),
        widgets = List(
          (WidgetKindId("chaos-portal-widget"), 4, 4),
          (WidgetKindId("activate-healing-altar-widget"), 3, 3)
        )
      )
    )
    val room = RandomizedTemplate(
      id = TemplateId("room"),
      shape = Rectangle(12, 10, 0.4),
      entrances = List((Some(ItemKindId("opened-door")), 1, 2)),
      border = Some(WallKindId("stone-wall")),
      content = Template.RandomizedContent(
        conduitLocations = (1, 1),
        creatures = List((CreatureKindId("hound-of-chaos"), 0, 2)),
        items = List(
          (ItemKindId("grey-altar"), 0, 1),
          (ItemKindId("long-sword"), 0, 1),
          (ItemKindId("claymore"), 0, 1)
        ),
        widgets = List(
          (WidgetKindId("chaos-portal-widget"), 0, 1),
          (WidgetKindId("activate-healing-altar-widget"), 0, 1)
        )
      )
    )
    val gatewayRoom = FixedTemplate(
      id = TemplateId("gateway-room"),
      shape = Rectangle(5, 5, 0),
      entrances = Map(Location(2, 0) -> Some(ItemKindId("opened-door"))),
      fixedContent = FixedContent(
        gatewayLocations = Set(Location(2, 2)),
        items = Map(
          Location(1, 1) -> List(ItemKindId("grey-altar")),
          Location(3, 1) -> List(ItemKindId("grey-altar")),
          Location(3, 2) -> List(ItemKindId("grey-altar")),
          Location(3, 3) -> List(ItemKindId("grey-altar")),
          Location(2, 3) -> List(ItemKindId("grey-altar")),
          Location(1, 3) -> List(ItemKindId("grey-altar")),
          Location(1, 2) -> List(ItemKindId("grey-altar"))
        ),
        walls = Map(
          Location(0, 0) -> WallKindId("stone-wall"),
          Location(1, 0) -> WallKindId("stone-wall"),
          Location(3, 0) -> WallKindId("stone-wall"),
          Location(4, 0) -> WallKindId("stone-wall"),
          Location(0, 4) -> WallKindId("stone-wall"),
          Location(1, 4) -> WallKindId("stone-wall"),
          Location(2, 4) -> WallKindId("stone-wall"),
          Location(3, 4) -> WallKindId("stone-wall"),
          Location(4, 4) -> WallKindId("stone-wall"),
          Location(0, 1) -> WallKindId("stone-wall"),
          Location(0, 2) -> WallKindId("stone-wall"),
          Location(0, 3) -> WallKindId("stone-wall"),
          Location(4, 1) -> WallKindId("stone-wall"),
          Location(4, 2) -> WallKindId("stone-wall"),
          Location(4, 3) -> WallKindId("stone-wall")
        )
      )
    )

    Map(
      main.id -> main,
      bigRoom.id -> bigRoom,
      room.id -> room,
      gatewayRoom.id -> gatewayRoom
    )
  }

  private def creatures = {
    val hero = CreatureKind(
      id = CreatureKindId("hero"),
      name = "Hero",
      display = '@',
      color = "yellow",
      faction = FactionId("justice"),
      progression = Some(ProgressionId("some")),
      stats = Stats(
        health = 30,
        attack = 20,
        defence = 20,
        damage = 10,
        armor = 5,
        sight = Sight(10)
      )
    )

    val avatarOfJustice = CreatureKind(
      id = CreatureKindId("avatar-of-justice"),
      name = "Avatar of Justice",
      display = 'A',
      color = "white",
      faction = FactionId("justice"),
      progression = Some(ProgressionId("some")),
      stats = Stats(
        health = 1000,
        attack = 20,
        defence = 20,
        damage = 10,
        armor = 5,
        sight = Sight(5)
      ),
      greetings = Map(
        FactionId("justice") -> List(
          CommunicationId("greet-justice-1"),
          CommunicationId("greet-justice-2")
        )
      ),
      responses = Map(
        FactionId("justice") -> List(
          CommunicationId("response-justice-1"),
          CommunicationId("response-justice-2")
        )
      )
    )

    val houndOfChaos = CreatureKind(
      id = CreatureKindId("hound-of-chaos"),
      name = "Hound of Chaos",
      display = 'c',
      color = "purple",
      faction = FactionId("chaos"),
      progression = None,
      stats = Stats(
        health = 10,
        attack = 10,
        defence = 10,
        damage = 10,
        armor = 5,
        sight = Sight(5)
      )
    )

    val heraldOfChaos = CreatureKind(
      id = CreatureKindId("herald-of-chaos"),
      name = "Herald of Chaos",
      display = 'C',
      color = "blue",
      faction = FactionId("chaos"),
      progression = None,
      stats = Stats(
        health = 10,
        attack = 20,
        defence = 20,
        damage = 10,
        armor = 5,
        sight = Sight(5)
      )
    )

    val avatarOfChaos = CreatureKind(
      id = CreatureKindId("avatar-of-chaos"),
      name = "Avatar of Chaos",
      display = 'A',
      color = "red",
      faction = FactionId("chaos"),
      progression = None,
      stats = Stats(
        health = 50,
        attack = 30,
        defence = 30,
        damage = 10,
        armor = 5,
        sight = Sight(5)
      )
    )

    Map(
      hero.id -> hero,
      avatarOfJustice.id -> avatarOfJustice,
      houndOfChaos.id -> houndOfChaos,
      heraldOfChaos.id -> heraldOfChaos,
      avatarOfChaos.id -> avatarOfChaos
    )
  }

  private def items = {
    val openedDoor = ItemKind(ItemKindId("opened-door"), "Opened door", '/', "brown")
    val portal = ItemKind(ItemKindId("chaos-portal"), "Chaos portal", 'O', "red")
    val greyAltar = ItemKind(ItemKindId("grey-altar"), "Grey altar", 'T', "grey")
    val whiteAltar = ItemKind(ItemKindId("white-altar"), "White altar", 'T', "white")
    val shiningAltar = ItemKind(ItemKindId("shining-altar"), "Shining altar", 'T', "yellow")
    val stairsDown = ItemKind(ItemKindId("stairs-down"), "Stairs down", '>', "light-gray")
    val stairsUp = ItemKind(ItemKindId("stairs-up"), "Stairs up", '<', "light-gray")

    val longSword = ItemKind(
      id = ItemKindId("long-sword"),
      name = "Long sword",
      display = '(',
      color = "light-gray",
      pickable = true,
      weapon = Some(Weapon(
        stats = Stats(
          attack = 5,
          defence = 5,
          damage = 5
        ),
        twoHanded = false
      ))
    )
    val claymore = ItemKind(
      id = ItemKindId("claymore"),
      name = "Claymore",
      display = '(',
      color = "white",
      pickable = true,
      weapon = Some(Weapon(
        stats = Stats(
          attack = 10,
          damage = 10
        ),
        twoHanded = true
      ))
    )

    Map(
      openedDoor.id -> openedDoor,
      portal.id -> portal,
      greyAltar.id -> greyAltar,
      whiteAltar.id -> whiteAltar,
      shiningAltar.id -> shiningAltar,
      stairsDown.id -> stairsDown,
      stairsUp.id -> stairsUp,
      longSword.id -> longSword,
      claymore.id -> claymore
    )
  }

  private def terrains = {
    val stone = TerrainKind(TerrainKindId("stone-floor"), "Stone floor", '.', "lightgray")
    val rubble = TerrainKind(TerrainKindId("rubble"), "Rubble", '.', "brown")

    Map(
      stone.id -> stone,
      rubble.id -> rubble
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
      summon = WeightedChoices(List(
        (CreatureKindId("hound-of-chaos"), 100),
        (CreatureKindId("herald-of-chaos"), 10),
        (CreatureKindId("avatar-of-chaos"), 2)
      )),
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
