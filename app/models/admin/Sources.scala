package models.admin

import io.github.fiifoo.scarl.ai.strategy._
import io.github.fiifoo.scarl.ai.tactic._
import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.feature.{Feature, RandomizedContentFeature}
import io.github.fiifoo.scarl.area.shape.{Rectangle, Shape}
import io.github.fiifoo.scarl.area.template.{FixedTemplate, RandomizedTemplate, Template}
import io.github.fiifoo.scarl.area.theme.ContentSelection.{FixedCreature, FixedItem, ThemeCreature, ThemeItem}
import io.github.fiifoo.scarl.area.theme.{CreatureSelection, ItemSelection, Theme}
import io.github.fiifoo.scarl.core.ai.{Behavior, Strategy}
import io.github.fiifoo.scarl.core.communication.{Communication, Message}
import io.github.fiifoo.scarl.core.creature.Missile.{Guidance, Guided, Smart}
import io.github.fiifoo.scarl.core.creature.{CreaturePower, Faction, Progression}
import io.github.fiifoo.scarl.core.item.Equipment._
import io.github.fiifoo.scarl.core.item._
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.math.Distribution
import io.github.fiifoo.scarl.core.math.Distribution.{Binomial, Uniform}
import io.github.fiifoo.scarl.mechanism.{CreateEntityMechanism, RemoveWallMechanism, UseDoorMechanism}
import io.github.fiifoo.scarl.power.{TransformCreaturePower, TransformItemPower}
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
      typeOf[AttackingTrapWidget],
      typeOf[DelayedTransformingWidget],
      typeOf[HealLocationWidget],
      typeOf[SummonCreatureWidget],
      typeOf[TimedExplosiveWidget],
      typeOf[TriggeredExplosiveWidget],
      typeOf[TriggeredMachineryWidget],
      typeOf[TriggeredTransformingWidget],
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
    SubModelSource(typeOf[CreaturePower], List(
      typeOf[TransformCreaturePower],
    )),
    SubModelSource(typeOf[CreatureSelection], List(
      typeOf[ThemeCreature],
      typeOf[FixedCreature],
    )),
    SubModelSource(typeOf[Discover], List(
      typeOf[DiscoverTriggerer.type],
      typeOf[DiscoverEveryone.type],
    ), objectPolymorphism = true),
    SubModelSource(typeOf[Distribution], List(
      typeOf[Binomial],
      typeOf[Uniform],
    )),
    SubModelSource(typeOf[Feature], List(
      typeOf[RandomizedContentFeature],
    )),
    SubModelSource(typeOf[Guidance], List(
      typeOf[Guided.type],
      typeOf[Smart.type],
    )),
    SubModelSource(typeOf[ItemPower], List(
      typeOf[TransformItemPower],
    )),
    SubModelSource(typeOf[ItemSelection], List(
      typeOf[ThemeItem.type],
      typeOf[FixedItem],
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

    SubModelSource(typeOf[ArmorSlot], List(
      typeOf[HeadArmor.type],
      typeOf[ChestArmor.type],
      typeOf[HandArmor.type],
      typeOf[LegArmor.type],
      typeOf[FootArmor.type],
    ), objectPolymorphism = true),
    SubModelSource(typeOf[Slot], List(
      typeOf[MainHand.type],
      typeOf[OffHand.type],
      typeOf[RangedSlot.type],
      typeOf[HeadArmor.type],
      typeOf[ChestArmor.type],
      typeOf[HandArmor.type],
      typeOf[LegArmor.type],
      typeOf[FootArmor.type],
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
