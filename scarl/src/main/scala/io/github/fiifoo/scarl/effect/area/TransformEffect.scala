package io.github.fiifoo.scarl.effect.area

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Selectors.getEntityLocation
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.{Location, Obstacle, Shape}
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.mutation.RemovableEntityMutation

case class TransformEffect(from: EntityId,
                           to: KindId,
                           owner: Option[CreatureId] = None,
                           description: Option[String] = None,
                           parent: Option[Effect] = None
                          ) extends Effect {

  def apply(s: State): EffectResult = {
    getTargetLocation(s) map transform(s) getOrElse getBlockedResult
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
    val result = to match {
      case kind: CreatureKindId => toCreature(s, kind, location)
      case kind: WidgetKindId => kind(s).toLocation(s, s.idSeq, location, owner map SafeCreatureId.apply)
      case kind: KindId => kind(s).toLocation(s, s.idSeq, location)
    }

    getSuccessResult(s, location, result)
  }

  private def toCreature(s: State, kind: CreatureKindId, location: Location): Kind.Result[Creature] = {
    owner map (owner => {
      kind(s).copy(faction = owner(s).faction).toLocation(s, s.idSeq, location, Some(SafeCreatureId(owner)))
    }) getOrElse {
      kind(s).toLocation(s, s.idSeq, location)
    }
  }

  private def getBlockedResult: EffectResult = {
    EffectResult(
      TransformBlockedEffect(from, to, owner, Some(this))
    )
  }

  private def getSuccessResult(s: State, location: Location, result: Kind.Result[_]): EffectResult = {
    val remove = from match {
      case item: ItemId => item(s).container match {
        case container: ContainerId => container
        case _ => from
      }
      case _ => from
    }

    val mutations = RemovableEntityMutation(remove) :: result.mutations

    EffectResult(
      mutations,
      TransformedEffect(from, to, location, owner, description, Some(this))
    )
  }
}
