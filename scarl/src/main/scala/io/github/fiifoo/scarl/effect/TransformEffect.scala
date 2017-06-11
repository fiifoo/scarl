package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.Selectors.getEntityLocation
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.mutation.{NewEntityMutation, RemovableEntityMutation}
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.geometry.{Obstacle, Shape}

case class TransformEffect(from: EntityId,
                           to: KindId,
                           owner: Option[CreatureId] = None,
                           description: Option[String] = None,
                           parent: Option[Effect] = None
                          ) extends Effect {

  def apply(s: State): EffectResult = {
    getTargetLocation(s) map transform(s) getOrElse getFailedResult(Some("Not enough space."))
  }

  private def getTargetLocation(s: State): Option[Location] = {
    getEntityLocation(s)(from, deep = true) flatMap (location => {
      to match {
        case _: CreatureKindId => findFreeLocation(s)(location)
        case _: WallKindId => findFreeLocation(s)(location)
        case _ => Some(location)
      }
    })
  }

  private def findFreeLocation(s: State)(location: Location): Option[Location] = {
    val free = isFreeLocation(s) _

    if (free(location)) {
      Some(location)
    } else {
      (Shape.circle(location, 1) - location) find free
    }
  }

  private def isFreeLocation(s: State)(location: Location): Boolean = {
    Obstacle.movement(s)(location).forall(_ == from)
  }

  private def transform(s: State)(location: Location): EffectResult = {
    (to match {
      case kind: CreatureKindId => Some(toCreature(s, kind, location))
      case kind: ItemKindId => Some(toItem(s, kind, location))
      case kind: TerrainKindId => Some(toTerrain(s, kind, location))
      case kind: WallKindId => Some(toWall(s, kind, location))
      case kind: WidgetKindId => Some(toWidget(s, kind, location))
      case _ => None
    }) map getSuccessResult(location) getOrElse getFailedResult()
  }

  private def getSuccessResult(location: Location)(entities: List[Entity]): EffectResult = {
    val mutations = RemovableEntityMutation(from) :: (entities map (NewEntityMutation(_)))

    EffectResult(
      mutations,
      TransformedEffect(from, to, location, owner, description, Some(this))
    )
  }

  private def getFailedResult(description: Option[String] = None): EffectResult = {
    EffectResult(
      TransformFailedEffect(from, to, owner, description, Some(this))
    )
  }

  private def toCreature(s: State, kind: CreatureKindId, location: Location) = {
    val initial = kind(s)(s, location)
    val creature = owner.map(owner => {
      if (owner == from) {
        initial.copy(
          faction = owner(s).faction
        )
      } else {
        initial.copy(
          faction = owner(s).faction,
          owner = Some(SafeCreatureId(owner))
        )
      }
    }) getOrElse initial

    List(creature)
  }

  private def toItem(s: State, kind: ItemKindId, location: Location) = {
    val (container, item) = kind(s)(s, location)

    List(container, item)
  }

  private def toTerrain(s: State, kind: TerrainKindId, location: Location) = {
    List(kind(s)(s, location))
  }

  private def toWall(s: State, kind: WallKindId, location: Location) = {
    List(kind(s)(s, location))
  }

  private def toWidget(s: State, kind: WidgetKindId, location: Location) = {
    val (container, item, status) = kind(s)(s, location)

    List(container, item, status)
  }
}
