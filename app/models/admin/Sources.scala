package models.admin

import io.github.fiifoo.scarl.ai.strategy._
import io.github.fiifoo.scarl.ai.tactic._
import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.feature._
import io.github.fiifoo.scarl.area.shape.{Rectangle, Shape}
import io.github.fiifoo.scarl.area.template._
import io.github.fiifoo.scarl.area.theme.Theme
import io.github.fiifoo.scarl.core.ai.{Behavior, Strategy}
import io.github.fiifoo.scarl.core.assets._
import io.github.fiifoo.scarl.core.communication.Communication
import io.github.fiifoo.scarl.core.creature._
import io.github.fiifoo.scarl.core.entity.{CreaturePower, ItemPower}
import io.github.fiifoo.scarl.core.item._
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.math.Distribution
import io.github.fiifoo.scarl.mechanism._
import io.github.fiifoo.scarl.power._
import io.github.fiifoo.scarl.status.{Conditions, Stances}
import io.github.fiifoo.scarl.widget._
import io.github.fiifoo.scarl.world._
import io.github.fiifoo.scarl.world.system.source.{SpaceshipSource, StellarBodySource}

import scala.reflect.runtime.universe.typeOf

object Sources {

  lazy val main: Map[Model.RelationId, ModelSource] = List(
    ModelSource(typeOf[Area], List("areas")),
    ModelSource(typeOf[Goal], List("goals")),
    ModelSource(typeOf[Region], List("regions")),
    ModelSource(typeOf[Site], List("sites")),
    ModelSource(typeOf[SpaceshipSource], List("spaceships")),
    ModelSource(typeOf[StellarBodySource], List("stellarBodies")),
    ModelSource(typeOf[Transport], List("transports")),
    ModelSource(typeOf[World], List("worlds")),

    ModelSource(typeOf[ContentSourceCatalogue], List("catalogues", "contentSources")),
    ModelSource(typeOf[CreatureCatalogue], List("catalogues", "creatures")),
    ModelSource(typeOf[ItemCatalogue], List("catalogues", "items")),
    ModelSource(typeOf[TemplateCatalogue], List("catalogues", "templates")),
    ModelSource(typeOf[TemplateSourceCatalogue], List("catalogues", "templateSources")),
    ModelSource(typeOf[TerrainCatalogue], List("catalogues", "terrains")),
    ModelSource(typeOf[WallCatalogue], List("catalogues", "walls")),
    ModelSource(typeOf[WidgetCatalogue], List("catalogues", "widgets")),

    ModelSource(typeOf[Communication], List("communications")),

    ModelSource(typeOf[Faction], List("factions")),
    ModelSource(typeOf[KeyKind], List("keys")),

    ModelSource(typeOf[CreatureKind], List("kinds", "creatures")),
    ModelSource(typeOf[ItemKind], List("kinds", "items")),
    ModelSource(typeOf[TerrainKind], List("kinds", "terrains")),
    ModelSource(typeOf[WallKind], List("kinds", "walls")),
    ModelSource(typeOf[WidgetKind], List("kinds", "widgets"), List(
      typeOf[DelayedTransformingWidget],
      typeOf[HealLocationWidget],
      typeOf[SummonCreatureWidget],
      typeOf[TimedExplosiveWidget],
      typeOf[TriggeredTransformingWidget],
      typeOf[TriggeredTrapWidget],
    )),

    ModelSource(typeOf[Progression], List("progressions")),
    ModelSource(typeOf[Recipe], List("recipes")),

    ModelSource(typeOf[Template], List("templates"), List(
      typeOf[FixedTemplate],
      typeOf[RandomizedTemplate],
      typeOf[SequenceTemplate],
    )),

    ModelSource(typeOf[Theme], List("themes")),

  ).map(s => s.relationId -> s).toMap

  // Needed only for polymorphic sub models. Others will be scanned recursively from main models.
  lazy val sub: Map[SubModel.Id, SubModelSource] = List(

    SubModelSource(typeOf[Behavior], List(
      typeOf[FollowOwnerTactic.type],
      typeOf[GreetTactic.type],
      typeOf[PassTactic.type],
      typeOf[RoamTactic.type],
      typeOf[ScoutTactic.type],
      typeOf[TurretTactic.type],
    )),

    SubModelSource(typeOf[CombatPower.Category], List(
      typeOf[CombatPower.Top.type],
      typeOf[CombatPower.High.type],
      typeOf[CombatPower.Medium.type],
      typeOf[CombatPower.Low.type],
    ), objectPolymorphism = true),

    SubModelSource(typeOf[Condition], List(
      typeOf[Conditions.Burning],
      typeOf[Conditions.Disoriented],
      typeOf[Conditions.Immobilized],
    )),

    SubModelSource(typeOf[ContentSelection.CreatureSelection], List(
      typeOf[ContentSelection.CatalogueCreature],
      typeOf[ContentSelection.FixedCreature],
      typeOf[ContentSelection.ThemeCreature],
    )),

    SubModelSource(typeOf[ContentSelection.DoorSelection], List(
      typeOf[ContentSelection.CatalogueDoor],
      typeOf[ContentSelection.FixedDoor],
      typeOf[ContentSelection.ThemeDoor],
    )),

    SubModelSource(typeOf[ContentSelection.ItemSelection], List(
      typeOf[ContentSelection.CatalogueEquipment],
      typeOf[ContentSelection.CatalogueItem],
      typeOf[ContentSelection.FixedItem],
      typeOf[ContentSelection.ThemeEquipment],
      typeOf[ContentSelection.ThemeItem],
    )),

    SubModelSource(typeOf[ContentSelection.TemplateSelection], List(
      typeOf[ContentSelection.CatalogueTemplate],
      typeOf[ContentSelection.FixedTemplate],
      typeOf[ContentSelection.ThemeTemplate],
    )),

    SubModelSource(typeOf[ContentSelection.TerrainSelection], List(
      typeOf[ContentSelection.CatalogueTerrain],
      typeOf[ContentSelection.FixedTerrain],
      typeOf[ContentSelection.ThemeTerrain],
    )),

    SubModelSource(typeOf[ContentSelection.WallSelection], List(
      typeOf[ContentSelection.CatalogueWall],
      typeOf[ContentSelection.FixedWall],
      typeOf[ContentSelection.ThemeWall],
    )),

    SubModelSource(typeOf[ContentSelection.WidgetSelection], List(
      typeOf[ContentSelection.CatalogueWidget],
      typeOf[ContentSelection.FixedWidget],
      typeOf[ContentSelection.ThemeWidget],
    )),

    SubModelSource(typeOf[CreatureKind.Category], List(
      typeOf[CreatureKind.DefaultCategory.type],
      typeOf[CreatureKind.TurretCategory.type],
    ), objectPolymorphism = true),

    SubModelSource(typeOf[CreaturePower], List(
      typeOf[AchieveGoalPower],
      typeOf[ActivateMachineryPower],
      typeOf[CompositeCreaturePower],
      typeOf[CapturePower],
      typeOf[CreateEntityPower],
      typeOf[DeathPower],
      typeOf[FactionDispositionPower],
      typeOf[FactionStrategyPower],
      typeOf[MaybeCreaturePower],
      typeOf[PartyPower],
      typeOf[ExplodePower],
      typeOf[ReceiveKeyPower],
      typeOf[RollCreatureConditionPower],
      typeOf[RollUserConditionPower],
      typeOf[TransformPower],
      typeOf[VoidPower],
    )),

    SubModelSource(typeOf[Discover], List(
      typeOf[Discover.Everyone.type],
      typeOf[Discover.Nobody.type],
      typeOf[Discover.Triggerer.type],
    ), objectPolymorphism = true),

    SubModelSource(typeOf[Faction.Disposition], List(
      typeOf[Faction.Friendly.type],
      typeOf[Faction.Neutral.type],
      typeOf[Faction.Hostile.type],
    ), objectPolymorphism = true),

    SubModelSource(typeOf[Distribution], List(
      typeOf[Distribution.Binomial],
      typeOf[Distribution.Uniform],
    )),

    SubModelSource(typeOf[Equipment.ArmorSlot], List(
      typeOf[Equipment.HeadArmor.type],
      typeOf[Equipment.BodyArmor.type],
      typeOf[Equipment.HandArmor.type],
      typeOf[Equipment.FootArmor.type],
    ), objectPolymorphism = true),

    SubModelSource(typeOf[Equipment.Category], List(
      typeOf[Equipment.HeadArmorCategory.type],
      typeOf[Equipment.BodyArmorCategory.type],
      typeOf[Equipment.HandArmorCategory.type],
      typeOf[Equipment.FootArmorCategory.type],
      typeOf[Equipment.LauncherCategory.type],
      typeOf[Equipment.RangedWeaponCategory.type],
      typeOf[Equipment.ShieldCategory.type],
      typeOf[Equipment.WeaponCategory.type],
    ), objectPolymorphism = true),

    SubModelSource(typeOf[Equipment.Slot], List(
      typeOf[Equipment.MainHand.type],
      typeOf[Equipment.OffHand.type],
      typeOf[Equipment.RangedSlot.type],
      typeOf[Equipment.LauncherSlot.type],
      typeOf[Equipment.HeadArmor.type],
      typeOf[Equipment.BodyArmor.type],
      typeOf[Equipment.HandArmor.type],
      typeOf[Equipment.FootArmor.type],
    ), objectPolymorphism = true),

    SubModelSource(typeOf[Feature], List(
      typeOf[BurrowFeature],
      typeOf[HouseFeature],
      typeOf[RandomizedContentFeature],
      typeOf[TrapRoomFeature],
    )),

    SubModelSource(typeOf[ItemKind.Category], List(
      typeOf[ItemKind.UtilityCategory.type],

      typeOf[ItemKind.DefaultDoorCategory.type],
      typeOf[ItemKind.SecureDoorCategory.type],

      typeOf[Equipment.HeadArmorCategory.type],
      typeOf[Equipment.BodyArmorCategory.type],
      typeOf[Equipment.HandArmorCategory.type],
      typeOf[Equipment.FootArmorCategory.type],
      typeOf[Equipment.LauncherCategory.type],
      typeOf[Equipment.RangedWeaponCategory.type],
      typeOf[Equipment.ShieldCategory.type],
      typeOf[Equipment.WeaponCategory.type],
    ), objectPolymorphism = true),

    SubModelSource(typeOf[ItemKind.DoorCategory], List(
      typeOf[ItemKind.DefaultDoorCategory.type],
      typeOf[ItemKind.SecureDoorCategory.type],
    ), objectPolymorphism = true),

    SubModelSource(typeOf[ItemPower], List(
      typeOf[AchieveGoalPower],
      typeOf[ActivateMachineryPower],
      typeOf[CompositeItemPower],
      typeOf[CreateEntityPower],
      typeOf[ExplodePower],
      typeOf[MaybeItemPower],
      typeOf[ReceiveCommunicationPower],
      typeOf[ReceiveKeyPower],
      typeOf[RemoveItemPower],
      typeOf[RollUserConditionPower],
      typeOf[ScanPower],
      typeOf[TransformPower],
      typeOf[TrapAttackPower],
      typeOf[VoidPower],
    )),

    SubModelSource(typeOf[Missile.Guidance], List(
      typeOf[Missile.Guided.type],
      typeOf[Missile.Smart.type],
    )),

    SubModelSource(typeOf[Mechanism], List(
      typeOf[CreateEntityMechanism],
      typeOf[RemoveEntityMechanism],
      typeOf[RemoveWallMechanism],
      typeOf[TransformEntityMechanism],
      typeOf[UseDoorMechanism.type],
    )),

    SubModelSource(typeOf[Shape], List(
      typeOf[Rectangle],
    )),

    SubModelSource(typeOf[Stance], List(
      typeOf[Stances.Aim.type],
      typeOf[Stances.Evasive.type],
      typeOf[Stances.Vulnerable.type],
    ), objectPolymorphism = true),

    SubModelSource(typeOf[StellarBodySource.Category], List(
      typeOf[StellarBodySource.BlackHoleCategory.type],
      typeOf[StellarBodySource.PlanetCategory.type],
      typeOf[StellarBodySource.SunCategory.type],
    ), objectPolymorphism = true),

    SubModelSource(typeOf[Strategy], List(
      typeOf[AttackStrategy.type],
      typeOf[RoamStrategy.type],
    )),

    SubModelSource(typeOf[Template.Category], List(
      typeOf[Template.ChallengeCategory.type],
      typeOf[Template.RoomCategory.type],
      typeOf[Template.SpaceCategory.type],
      typeOf[Template.StorageCategory.type],
      typeOf[Template.TrapCategory.type],
    ), objectPolymorphism = true),

    SubModelSource(typeOf[TerrainKind.Category], List(
      typeOf[TerrainKind.DefaultCategory.type],
      typeOf[TerrainKind.ConstructedCategory.type],
      typeOf[TerrainKind.ImpassableCategory.type],
      typeOf[TerrainKind.NaturalCategory.type],
    ), objectPolymorphism = true),

    SubModelSource(typeOf[TransportCategory], List(
      typeOf[TransportCategory.Spaceship.type],
    ), objectPolymorphism = true),

    SubModelSource(typeOf[WallKind.Category], List(
      typeOf[WallKind.DefaultCategory.type],
      typeOf[WallKind.AreaBorderCategory.type],
      typeOf[WallKind.ConstructedCategory.type],
      typeOf[WallKind.NaturalCategory.type],
      typeOf[WallKind.SecureCategory.type],
      typeOf[WallKind.TransparentCategory.type],
    ), objectPolymorphism = true),

    SubModelSource(typeOf[WidgetKind.Category], List(
      typeOf[WidgetKind.HealCategory.type],
      typeOf[WidgetKind.PortalCategory.type],
      typeOf[WidgetKind.TrapCategory.type],
    ), objectPolymorphism = true),

  ).map(s => s.id -> s).toMap

  lazy val polymorphicRelation: Map[Model.RelationId, PolymorphicRelationSource] = List(
    PolymorphicRelationSource(typeOf[KindId], List(
      typeOf[CreatureKind],
      typeOf[ItemKind],
      typeOf[TerrainKind],
      typeOf[WallKind],
      typeOf[WidgetKind],
    )),
  ).map(s => s.id -> s).toMap
}
