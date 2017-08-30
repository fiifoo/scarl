package game

import io.github.fiifoo.scarl.ai.tactic.{FollowerTactic, RoamTactic}
import io.github.fiifoo.scarl.area.shape.Rectangle
import io.github.fiifoo.scarl.area.template.Template.FixedContent
import io.github.fiifoo.scarl.area.template.{FixedTemplate, RandomizedTemplate, Template, TemplateId}
import io.github.fiifoo.scarl.area.{Area, AreaId}
import io.github.fiifoo.scarl.core.Location
import io.github.fiifoo.scarl.core.Rng.WeightedChoices
import io.github.fiifoo.scarl.core.action.PassTactic
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.communication.{Communication, CommunicationId, Message}
import io.github.fiifoo.scarl.core.creature.Missile.Guided
import io.github.fiifoo.scarl.core.creature.Stats._
import io.github.fiifoo.scarl.core.creature._
import io.github.fiifoo.scarl.core.item.Equipment._
import io.github.fiifoo.scarl.core.item._
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.power.{CreaturePowerId, ItemPowerId, Powers}
import io.github.fiifoo.scarl.power.{TransformCreaturePower, TransformItemPower}
import io.github.fiifoo.scarl.widget._
import io.github.fiifoo.scarl.world.WorldAssets

object Data {

  def apply(): WorldAssets = {
    WorldAssets(
      areas,
      combatPower,
      communications,
      factions,
      kinds,
      powers,
      progressions,
      templates
    )
  }

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

  def combatPower: CombatPower = {
    Simulations.combatPower(creatures.values)
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
    val none = Faction(
      id = FactionId("none"),
      enemies = Set()
    )
    val justice = Faction(
      id = FactionId("justice"),
      enemies = Set(FactionId("chaos"), FactionId("marines"))
    )
    val chaos = Faction(
      id = FactionId("chaos"),
      enemies = Set(FactionId("justice"), FactionId("marines"))
    )
    val marines = Faction(
      id = FactionId("marines"),
      enemies = Set(FactionId("justice"), FactionId("chaos"))
    )

    Map(
      none.id -> none,
      justice.id -> justice,
      chaos.id -> chaos,
      marines.id -> marines
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

  def powers: Powers = {
    val transformDroneCreature = TransformCreaturePower(
      id = CreaturePowerId("transform-drone"),
      to = ItemKindId("drone"),
      description = Some("Drone shuts down.")
    )
    val transformDroneItem = TransformItemPower(
      id = ItemPowerId("transform-drone"),
      to = CreatureKindId("drone"),
      description = Some("Drone starts humming.")
    )

    Powers(
      creatures = Map(
        transformDroneCreature.id -> transformDroneCreature
      ),
      items = Map(
        transformDroneItem.id -> transformDroneItem
      )
    )
  }

  def progressions: Map[ProgressionId, Progression] = {
    val add = Stats(
      health = 2,
      melee = Melee(attack = 1, damage = 1),
      defence = 1
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
      shape = Rectangle(70, 25, 0),
      templates = List(
        (TemplateId("big-room"), 1, 1),
        (TemplateId("room"), 2, 4),
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
      entrances = List((Some(ItemKindId("wooden-door-closed")), 4, 4)),
      border = Some(WallKindId("stone-wall")),
      terrain = Some(TerrainKindId("rubble")),
      content = Template.RandomizedContent(
        creatures = List((CreatureKindId("avatar-of-justice"), 5, 5)),
        items = List(
          (ItemKindId("grey-altar"), 5, 5),
          (ItemKindId("musket"), 1, 1),
          (ItemKindId("long-sword"), 0, 1),
          (ItemKindId("claymore"), 0, 1),
          (ItemKindId("steel-shield"), 0, 1),
          (ItemKindId("chain-shirt"), 0, 1),
          (ItemKindId("chain-leggings"), 0, 1),
          (ItemKindId("leather-helmet"), 0, 1),
          (ItemKindId("leather-boots"), 0, 1),
          (ItemKindId("leather-gloves"), 0, 1)
        ),
        widgets = List(
          (WidgetKindId("chaos-portal-widget"), 4, 4),
          (WidgetKindId("activate-healing-altar-widget"), 3, 3),
          (WidgetKindId("arrow-trap-widget"), 3, 3)
        )
      )
    )
    val room = RandomizedTemplate(
      id = TemplateId("room"),
      shape = Rectangle(12, 10, 0.4),
      entrances = List((Some(ItemKindId("wooden-door-closed")), 1, 2)),
      border = Some(WallKindId("stone-wall")),
      content = Template.RandomizedContent(
        conduitLocations = (1, 1),
        creatures = List(
          (CreatureKindId("hound-of-chaos"), 0, 2),
          (CreatureKindId("colonial-marine"), 0, 5)
        ),
        items = List(
          (ItemKindId("grey-altar"), 0, 1),
          (ItemKindId("musket"), 1, 1),
          (ItemKindId("long-sword"), 0, 1),
          (ItemKindId("claymore"), 0, 1),
          (ItemKindId("steel-shield"), 0, 1),
          (ItemKindId("chain-shirt"), 0, 1),
          (ItemKindId("chain-leggings"), 0, 1),
          (ItemKindId("leather-helmet"), 0, 1),
          (ItemKindId("leather-boots"), 0, 1),
          (ItemKindId("leather-gloves"), 0, 1)
        ),
        widgets = List(
          (WidgetKindId("chaos-portal-widget"), 0, 1),
          (WidgetKindId("activate-healing-altar-widget"), 0, 1),
          (WidgetKindId("arrow-trap-widget"), 0, 1)
        )
      )
    )
    val gatewayRoom = FixedTemplate(
      id = TemplateId("gateway-room"),
      shape = Rectangle(5, 5, 0),
      entrances = Map(Location(2, 0) -> Some(ItemKindId("wooden-door-closed"))),
      fixedContent = FixedContent(
        gatewayLocations = Set(Location(2, 2)),
        items = Map(
          Location(2, 1) -> List(ItemKindId("guided-missile-launcher")),
          Location(1, 1) -> List(ItemKindId("grey-altar")),
          Location(3, 1) -> List(ItemKindId("grey-altar")),
          Location(3, 2) -> List(ItemKindId("grey-altar")),
          Location(3, 3) -> List(ItemKindId("grey-altar")),
          Location(2, 3) -> List(ItemKindId("drone")),
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
      color = "Yellow",
      faction = FactionId("justice"),
      solitary = true,
      behavior = RoamTactic,
      stats = Stats(
        health = 30,
        melee = Melee(attack = 20, damage = 10),
        defence = 20,
        armor = 5,
        sight = Sight(10)
      ),
      character = Some(Character(
        progression = ProgressionId("some")
      )),
      equipments = Map(
        MainHand -> ItemKindId("long-sword"),
        RangedSlot -> ItemKindId("musket"),
      ),
      inventory = List(
        ItemKindId("drone"),
      ),
    )

    val avatarOfJustice = CreatureKind(
      id = CreatureKindId("avatar-of-justice"),
      name = "Avatar of Justice",
      display = 'A',
      color = "White",
      faction = FactionId("justice"),
      behavior = RoamTactic,
      stats = Stats(
        health = 1000,
        melee = Melee(attack = 20, damage = 10),
        defence = 20,
        armor = 5,
        sight = Sight(5)
      ),
      character = Some(Character(
        progression = ProgressionId("some")
      )),
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
      color = "Purple",
      faction = FactionId("chaos"),
      behavior = RoamTactic,
      stats = Stats(
        health = 10,
        melee = Melee(attack = 10, damage = 10),
        defence = 10,
        armor = 5,
        sight = Sight(5)
      )
    )

    val heraldOfChaos = CreatureKind(
      id = CreatureKindId("herald-of-chaos"),
      name = "Herald of Chaos",
      display = 'C',
      color = "Blue",
      faction = FactionId("chaos"),
      behavior = RoamTactic,
      stats = Stats(
        health = 10,
        melee = Melee(attack = 20, damage = 10),
        defence = 20,
        armor = 5,
        sight = Sight(5)
      )
    )

    val avatarOfChaos = CreatureKind(
      id = CreatureKindId("avatar-of-chaos"),
      name = "Avatar of Chaos",
      display = 'A',
      color = "Red",
      faction = FactionId("chaos"),
      behavior = RoamTactic,
      stats = Stats(
        health = 50,
        melee = Melee(attack = 30, damage = 15),
        defence = 30,
        armor = 5,
        sight = Sight(5)
      )
    )

    val colonialMarine = CreatureKind(
      id = CreatureKindId("colonial-marine"),
      name = "Colonial Marine",
      display = 'm',
      color = "Green",
      faction = FactionId("marines"),
      behavior = RoamTactic,
      stats = Stats(
        health = 10,
        melee = Melee(attack = 10, damage = 10),
        ranged = Ranged(attack = 10, damage = 10, range = 5),
        defence = 10,
        armor = 5,
        sight = Sight(5)
      )
    )

    val guidedMissile = CreatureKind(
      id = CreatureKindId("guided-missile"),
      name = "Missile",
      display = 'x',
      color = "White",
      faction = FactionId("none"),
      behavior = PassTactic,
      stats = Stats(
        speed = 3,
        health = 10,
        defence = 100,
        armor = 10,
        explosive = Explosive(attack = 50, damage = 20, radius = 2)
      ),
      missile = Some(Missile(
        guidance = Some(Guided))
      ),
      flying = true
    )

    val drone = CreatureKind(
      id = CreatureKindId("drone"),
      name = "Drone",
      display = 'd',
      color = "Yellow",
      faction = FactionId("none"),
      behavior = FollowerTactic,
      stats = Stats(
        health = 50,
        melee = Melee(attack = 10, damage = 10),
        missileLauncher = MissileLauncher(ammo = Some(CreatureKindId("guided-missile")), range = 10),
        ranged = Ranged(attack = 20, damage = 10, range = 5),
        defence = 30,
        armor = 10,
        sight = Sight(10)
      ),
      usable = Some(CreaturePowerId("transform-drone"))
    )

    Map(
      hero.id -> hero,
      avatarOfJustice.id -> avatarOfJustice,
      houndOfChaos.id -> houndOfChaos,
      heraldOfChaos.id -> heraldOfChaos,
      avatarOfChaos.id -> avatarOfChaos,
      colonialMarine.id -> colonialMarine,
      guidedMissile.id -> guidedMissile,
      drone.id -> drone
    )
  }

  private def items = {
    val portal = ItemKind(
      id = ItemKindId("chaos-portal"),
      name = "Chaos portal",
      display = 'O',
      color = "Red"
    )

    val greyAltar = ItemKind(
      id = ItemKindId("grey-altar"),
      name = "Grey altar",
      display = 'T',
      color = "Grey"
    )

    val whiteAltar = ItemKind(
      id = ItemKindId("white-altar"),
      name = "White altar",
      display = 'T',
      color = "White"
    )

    val shiningAltar = ItemKind(
      id = ItemKindId("shining-altar"),
      name = "Shining altar",
      display = 'T',
      color = "Yellow"
    )

    val stairsDown = ItemKind(
      id = ItemKindId("stairs-down"),
      name = "Stairs down",
      display = '>',
      color = "LightGrey"
    )

    val stairsUp = ItemKind(
      id = ItemKindId("stairs-up"),
      name = "Stairs up",
      display = '<',
      color = "LightGrey"
    )

    val arrowTrap = ItemKind(
      id = ItemKindId("arrow-trap"),
      name = "Arrow trap",
      display = '^',
      color = "Red",
      hidden = true
    )

    val woodenDoorOpen = ItemKind(
      id = ItemKindId("wooden-door-open"),
      name = "Wooden door",
      display = '/',
      color = "Brown",
      door = Some(Door(
        open = true,
        transformTo = ItemKindId("wooden-door-closed")
      ))
    )

    val woodenDoorClosed = ItemKind(
      id = ItemKindId("wooden-door-closed"),
      name = "Wooden door",
      display = '+',
      color = "Brown",
      door = Some(Door(
        open = false,
        transformTo = ItemKindId("wooden-door-open")
      ))
    )

    val musket = ItemKind(
      id = ItemKindId("musket"),
      name = "Musket",
      display = '!',
      color = "Brown",
      pickable = true,
      rangedWeapon = Some(RangedWeapon(
        stats = Stats(
          ranged = Ranged(attack = 20, damage = 10, range = 5)
        )
      ))
    )

    val longSword = ItemKind(
      id = ItemKindId("long-sword"),
      name = "Long sword",
      display = '(',
      color = "LightGrey",
      pickable = true,
      weapon = Some(Weapon(
        stats = Stats(
          melee = Melee(attack = 5, damage = 5),
          defence = 5
        ),
        twoHanded = false
      ))
    )
    val claymore = ItemKind(
      id = ItemKindId("claymore"),
      name = "Claymore",
      display = '(',
      color = "White",
      pickable = true,
      weapon = Some(Weapon(
        stats = Stats(
          melee = Melee(attack = 10, damage = 10)
        ),
        twoHanded = true
      ))
    )

    val steelShield = ItemKind(
      id = ItemKindId("steel-shield"),
      name = "Steel shield",
      display = '[',
      color = "DarkGrey",
      pickable = true,
      shield = Some(Shield(
        stats = Stats(
          defence = 10,
          armor = 1
        )
      ))
    )

    val chainShirt = ItemKind(
      id = ItemKindId("chain-shirt"),
      name = "Chain shirt",
      display = ']',
      color = "LightGrey",
      pickable = true,
      armor = Some(Armor(
        stats = Stats(
          armor = 2
        ),
        slot = ChestArmor
      ))
    )

    val chainLeggings = ItemKind(
      id = ItemKindId("chain-leggings"),
      name = "Chain leggings",
      display = ']',
      color = "LightGrey",
      pickable = true,
      armor = Some(Armor(
        stats = Stats(
          armor = 2
        ),
        slot = LegArmor
      ))
    )

    val leatherHelmet = ItemKind(
      id = ItemKindId("leather-helmet"),
      name = "Leather helmet",
      display = ']',
      color = "Brown",
      pickable = true,
      armor = Some(Armor(
        stats = Stats(
          armor = 1
        ),
        slot = HeadArmor
      ))
    )

    val leatherBoots = ItemKind(
      id = ItemKindId("leather-boots"),
      name = "Leather boots",
      display = ']',
      color = "Brown",
      pickable = true,
      armor = Some(Armor(
        stats = Stats(
          armor = 1
        ),
        slot = FootArmor
      ))
    )

    val leatherGloves = ItemKind(
      id = ItemKindId("leather-gloves"),
      name = "Leather gloves",
      display = ']',
      color = "Brown",
      pickable = true,
      armor = Some(Armor(
        stats = Stats(
          armor = 1
        ),
        slot = HandArmor
      ))
    )

    val guidedMissileLauncher = ItemKind(
      id = ItemKindId("guided-missile-launcher"),
      name = "Guided missile launcher",
      display = '!',
      color = "White",
      pickable = true,
      rangedWeapon = Some(RangedWeapon(
        stats = Stats(
          missileLauncher = MissileLauncher(
            ammo = Some(CreatureKindId("guided-missile")),
            range = 10
          )
        ),
      ))
    )

    val drone = ItemKind(
      id = ItemKindId("drone"),
      name = "Drone",
      display = 'd',
      color = "LightGrey",
      pickable = true,
      usable = Some(ItemPowerId("transform-drone"))
    )

    Map(
      portal.id -> portal,
      greyAltar.id -> greyAltar,
      whiteAltar.id -> whiteAltar,
      shiningAltar.id -> shiningAltar,
      stairsDown.id -> stairsDown,
      stairsUp.id -> stairsUp,
      arrowTrap.id -> arrowTrap,
      woodenDoorOpen.id -> woodenDoorOpen,
      woodenDoorClosed.id -> woodenDoorClosed,
      musket.id -> musket,
      longSword.id -> longSword,
      claymore.id -> claymore,
      steelShield.id -> steelShield,
      chainShirt.id -> chainShirt,
      chainLeggings.id -> chainLeggings,
      leatherHelmet.id -> leatherHelmet,
      leatherBoots.id -> leatherBoots,
      leatherGloves.id -> leatherGloves,
      guidedMissileLauncher.id -> guidedMissileLauncher,
      drone.id -> drone
    )
  }

  private def terrains = {
    val stone = TerrainKind(
      id = TerrainKindId("stone-floor"),
      name = "Stone floor",
      display = '.',
      color = "LightGrey"
    )
    val rubble = TerrainKind(
      id = TerrainKindId("rubble"),
      name = "Rubble",
      display = '.',
      color = "Brown"
    )

    Map(
      stone.id -> stone,
      rubble.id -> rubble
    )
  }

  private def walls = {
    val stone = WallKind(
      id = WallKindId("stone-wall"),
      name = "Stone wall",
      display = '#',
      color = "DarkGrey"
    )

    Map(
      stone.id -> stone
    )
  }

  private def widgets = {
    val arrowTrap = DamagingTrapWidget(
      id = WidgetKindId("arrow-trap-widget"),
      item = ItemKindId("arrow-trap"),
      damage = 10,
      triggerDescription = "You step on pressure plate. Arrow shoots from the wall and hits you."
    )

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
      arrowTrap.id -> arrowTrap,
      portal.id -> portal,
      activateAltar.id -> activateAltar,
      activeAltar.id -> activeAltar,
      inactiveAltar.id -> inactiveAltar
    )
  }
}
