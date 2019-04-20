package io.github.fiifoo.scarl.effect.area

import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Selectors.getEntityLocation
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.{Location, Obstacle, Shape}
import io.github.fiifoo.scarl.core.kind.Kind.Options
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.mutation.RemovableEntityMutation
import io.github.fiifoo.scarl.core.{State, Tag}

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
    val tags = from(s) match {
      case taggable: Taggable => taggable.tags
      case _ => Set[Tag]()
    }

    val result = to(s).apply(s, s.idSeq, location, Options(owner, tags))

    getSuccessResult(s, location, result)
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
      case status: StatusId => status(s).target match {
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
