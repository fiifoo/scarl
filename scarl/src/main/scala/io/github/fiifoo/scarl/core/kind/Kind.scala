package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.entity.{Entity, Locatable}
import io.github.fiifoo.scarl.core.kind.Kind.Result
import io.github.fiifoo.scarl.core.mutation.Mutation
import io.github.fiifoo.scarl.core.{IdSeq, Location, State}

trait Kind[T <: Locatable] {
  val id: KindId[T]

  def toLocation(s: State, idSeq: IdSeq, location: Location): Result[T]
}

object Kind {

  case class Result[T <: Entity](mutations: List[Mutation], idSeq: IdSeq, entity: T) {
    def write(s: State): State = {
      (mutations foldLeft s) ((s, mutation) => mutation(s))
    }
  }

}
