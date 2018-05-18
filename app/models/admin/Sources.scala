package models.admin

import io.github.fiifoo.scarl.ai.strategy._
import io.github.fiifoo.scarl.ai.tactic._
import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.feature.{BurrowFeature, Feature, HouseFeature, RandomizedContentFeature}
import io.github.fiifoo.scarl.area.shape.{Rectangle, Shape}
import io.github.fiifoo.scarl.area.template.{ContentSelection, FixedTemplate, RandomizedTemplate, Template}
import io.github.fiifoo.scarl.area.theme.Theme
import io.github.fiifoo.scarl.core.ai.{Behavior, Strategy}
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.communication.{Communication, Message}
import io.github.fiifoo.scarl.core.creature.{CreaturePower, Faction, Missile, Progression}
import io.github.fiifoo.scarl.core.item._
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.math.Distribution
import io.github.fiifoo.scarl.mechanism.{CreateEntityMechanism, RemoveWallMechanism, UseDoorMechanism}
import io.github.fiifoo.scarl.power._
import io.github.fiifoo.scarl.widget._

import scala.reflect.runtime.universe.typeOf

object Sources {

  lazy val main: Map[Model.RelationId, ModelSource] = List(
    ModelSource(typeOf[Area], List("areas")),

    ModelSource(typeOf[Communication], List("communications"), List(
      typeOf[Message],
    )),

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
      typeOf[TriggeredMachineryWidget],
      typeOf[TriggeredTransformingWidget],
      typeOf[TriggeredTrapWidget],
    )),

    ModelSource(typeOf[Progression], List("progressions")),

    ModelSource(typeOf[Template], List("templates"), List(
      typeOf[FixedTemplate],
      typeOf[RandomizedTemplate],
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
    )),

    SubModelSource(typeOf[CombatPower.Category], List(
      typeOf[CombatPower.Top.type],
      typeOf[CombatPower.High.type],
      typeOf[CombatPower.Medium.type],
      typeOf[CombatPower.Low.type],
    ), objectPolymorphism = true),

    SubModelSource(typeOf[ContentSelection.CreatureSelection], List(
      typeOf[ContentSelection.ThemeCreature],
      typeOf[ContentSelection.FixedCreature],
    )),

    SubModelSource(typeOf[ContentSelection.ItemSelection], List(
      typeOf[ContentSelection.ThemeItem],
      typeOf[ContentSelection.ThemeEquipment],
      typeOf[ContentSelection.FixedItem],
    )),

    SubModelSource(typeOf[ContentSelection.TemplateSelection], List(
      typeOf[ContentSelection.ThemeTemplate],
      typeOf[ContentSelection.FixedTemplate],
    )),

    SubModelSource(typeOf[ContentSelection.WidgetSelection], List(
      typeOf[ContentSelection.ThemeWidget],
      typeOf[ContentSelection.FixedWidget],
    )),

    SubModelSource(typeOf[CreaturePower], List(
      typeOf[TransformCreaturePower],
    )),

    SubModelSource(typeOf[Discover], List(
      typeOf[DiscoverTriggerer.type],
      typeOf[DiscoverEveryone.type],
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
    )),

    SubModelSource(typeOf[ItemKind.Category], List(
      typeOf[ItemKind.UtilityCategory.type],
    ), objectPolymorphism = true),

    SubModelSource(typeOf[ItemPower], List(
      typeOf[ExplodeItemPower.type],
      typeOf[RemoveItemPower],
      typeOf[TransformItemPower],
      typeOf[TrapAttackPower],
    )),

    SubModelSource(typeOf[Missile.Guidance], List(
      typeOf[Missile.Guided.type],
      typeOf[Missile.Smart.type],
    )),

    SubModelSource(typeOf[Mechanism], List(
      typeOf[CreateEntityMechanism],
      typeOf[RemoveWallMechanism],
      typeOf[UseDoorMechanism],
    )),

    SubModelSource(typeOf[Shape], List(
      typeOf[Rectangle],
    )),

    SubModelSource(typeOf[Strategy], List(
      typeOf[AttackStrategy.type],
      typeOf[RoamStrategy.type],
    )),

    SubModelSource(typeOf[Template.Category], List(
      typeOf[Template.ChallengeCategory.type],
      typeOf[Template.TrapCategory.type],
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
