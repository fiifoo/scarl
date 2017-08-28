package io.github.fiifoo.scarl.effect

import io.github.fiifoo.scarl.core.Selectors.getEntityLocation
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.mutation.RemovableEntityMutation
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.geometry.{Obstacle, Shape}

case class TransformEffect[T <: Locatable](from: EntityId,
                                           to: KindId[T],
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
    val result = to match {
      case kind: CreatureKindId => toCreature(s, kind, location)
      case kind: KindId[T] => kind(s).toLocation(s, s.idSeq, location)
    }

    getSuccessResult(location, result)
  }

  private def getSuccessResult(location: Location, result: Kind.Result[_]): EffectResult = {
    val mutations = RemovableEntityMutation(from) :: result.mutations

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

  private def toCreature(s: State, kind: CreatureKindId, location: Location): Kind.Result[Creature] = {
    owner map (owner => {
      kind(s).copy(faction = owner(s).faction).toLocation(s, s.idSeq, location, Some(SafeCreatureId(owner)))
    }) getOrElse {
      kind(s).toLocation(s, s.idSeq, location)
    }
  }
}
