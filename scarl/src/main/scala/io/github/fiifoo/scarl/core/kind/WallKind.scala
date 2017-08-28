package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.entity.{Wall, WallId}
import io.github.fiifoo.scarl.core.kind.Kind.Result
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}
import io.github.fiifoo.scarl.core.{IdSeq, Location, State}

case class WallKind(id: WallKindId,
                    name: String,
                    display: Char,
                    color: String
                   ) extends Kind[Wall] {

  def toLocation(s: State, idSeq: IdSeq, location: Location): Result[Wall] = {
    val (nextId, nextIdSeq) = idSeq()

    val wall = Wall(
      id = WallId(nextId),
      kind = id,
      location = location
    )

    Result(
      mutations = List(IdSeqMutation(nextIdSeq), NewEntityMutation(wall)),
      nextIdSeq,
      wall,
    )
  }
}
