package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity.{CreatureId, Entity, IdSeq}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.Kind.{Options, Result}
import io.github.fiifoo.scarl.core.mutation.Mutation
import io.github.fiifoo.scarl.core.{State, Tag}

trait Kind {
  val id: KindId

  def apply(s: State, idSeq: IdSeq, location: Location, options: Options = Options()): Result[_]
}

object Kind {

  case class Options(faction: Option[FactionId] = None, owner: Option[CreatureId] = None, tags: Set[Tag] = Set())

  case class Result[T <: Entity](mutations: List[Mutation], idSeq: IdSeq, entity: T) {
    def write(s: State): State = {
      (mutations foldLeft s) ((s, mutation) => mutation(s))
    }
  }

}
