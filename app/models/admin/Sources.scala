package models.admin

import io.github.fiifoo.scarl.ai.tactic.{FollowerTactic, RoamTactic}
import io.github.fiifoo.scarl.area.Area
import io.github.fiifoo.scarl.area.shape.{Rectangle, Shape}
import io.github.fiifoo.scarl.area.template.{FixedTemplate, RandomizedTemplate, Template}
import io.github.fiifoo.scarl.core.action.{Behavior, PassTactic}
import io.github.fiifoo.scarl.core.communication.{Communication, Message}
import io.github.fiifoo.scarl.core.creature.Missile.{Guidance, Guided, Smart}
import io.github.fiifoo.scarl.core.creature.{Faction, Progression}
import io.github.fiifoo.scarl.core.entity.Locatable
import io.github.fiifoo.scarl.core.item.Equipment._
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.power.{CreaturePower, ItemPower}
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
    ModelSource(typeOf[CreatureKind], List("kinds", "creatures")),
    ModelSource(typeOf[ItemKind], List("kinds", "items")),
    ModelSource(typeOf[TerrainKind], List("kinds", "terrains")),
    ModelSource(typeOf[WallKind], List("kinds", "walls")),
    ModelSource(typeOf[WidgetKind], List("kinds", "widgets"), List(
      typeOf[DamagingTrapWidget],
      typeOf[DelayedTransformingWidget],
      typeOf[HealLocationWidget],
      typeOf[SummonCreatureWidget],
      typeOf[TriggeredTransformingWidget],
    )),

    ModelSource(typeOf[CreaturePower], List("powers", "creatures"), List(
      typeOf[TransformCreaturePower[Locatable]],
    )),
    ModelSource(typeOf[ItemPower], List("powers", "items"), List(
      typeOf[TransformItemPower[Locatable]],
    )),

    ModelSource(typeOf[Progression], List("progressions")),

    ModelSource(typeOf[Template], List("templates"), List(
      typeOf[FixedTemplate],
      typeOf[RandomizedTemplate],
    )),

  ).map(s => s.relationId -> s).toMap

  // Needed only for polymorphic sub models. Others will be scanned recursively from main models.
  lazy val sub: Map[SubModel.Id, SubModelSource] = List(
    SubModelSource(typeOf[Shape], List(
      typeOf[Rectangle],
    )),

    SubModelSource(typeOf[Behavior], List(
      typeOf[PassTactic.type],
      typeOf[FollowerTactic.type],
      typeOf[RoamTactic.type],
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
    SubModelSource(typeOf[Guidance], List(
      typeOf[Guided.type],
      typeOf[Smart.type],
    )),
  ).map(s => s.id -> s).toMap

  lazy val polymorphicRelation: Map[Model.RelationId, PolymorphicRelationSource] = List(
    PolymorphicRelationSource(typeOf[KindId[Locatable]], List(
      typeOf[CreatureKind],
      typeOf[ItemKind],
      typeOf[TerrainKind],
      typeOf[WallKind],
      typeOf[WidgetKind],
    )),
  ).map(s => s.id -> s).toMap
}
