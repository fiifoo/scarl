package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.entity.{Terrain, TerrainId}
import io.github.fiifoo.scarl.core.kind.Kind.Result
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}
import io.github.fiifoo.scarl.core.{IdSeq, Location, State}

case class TerrainKind(id: TerrainKindId,
                       name: String,
                       display: Char,
                       color: String
                      ) extends Kind[Terrain] {

  def toLocation(s: State, idSeq: IdSeq, location: Location): Result[Terrain] = {
    val (nextId, nextIdSeq) = idSeq()

    val terrain = Terrain(
      id = TerrainId(nextId),
      kind = id,
      location = location
    )

    Result(
      mutations = List(IdSeqMutation(nextIdSeq), NewEntityMutation(terrain)),
      nextIdSeq,
      terrain,
    )
  }
}
