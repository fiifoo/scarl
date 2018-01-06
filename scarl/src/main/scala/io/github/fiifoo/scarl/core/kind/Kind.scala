package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.{Entity, IdSeq}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.Kind.Result
import io.github.fiifoo.scarl.core.mutation.Mutation

trait Kind {
  val id: KindId

  def toLocation(s: State, idSeq: IdSeq, location: Location): Result[_]
}

object Kind {

  case class Result[T <: Entity](mutations: List[Mutation], idSeq: IdSeq, entity: T) {
    def write(s: State): State = {
      (mutations foldLeft s) ((s, mutation) => mutation(s))
    }
  }

}
