package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.{Color, State}
import io.github.fiifoo.scarl.core.entity.{IdSeq, Wall, WallId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.Kind.Result
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}

case class WallKind(id: WallKindId,
                    name: String,
                    display: Char,
                    color: Color,
                    hardness: Option[Int] = None,
                   ) extends Kind {

  def toLocation(s: State, idSeq: IdSeq, location: Location): Result[Wall] = {
    val (nextId, nextIdSeq) = idSeq()

    val wall = Wall(
      id = WallId(nextId),
      kind = id,
      location = location,
      hardness = hardness,
    )

    Result(
      mutations = List(IdSeqMutation(nextIdSeq), NewEntityMutation(wall)),
      nextIdSeq,
      wall,
    )
  }
}
