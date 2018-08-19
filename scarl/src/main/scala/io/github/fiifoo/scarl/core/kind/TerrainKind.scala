package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.entity.{IdSeq, Terrain, TerrainId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.Kind.{Options, Result}
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}
import io.github.fiifoo.scarl.core.{Color, State}

case class TerrainKind(id: TerrainKindId,
                       name: String,
                       display: Char,
                       color: Color,
                      ) extends Kind {

  def apply(s: State, idSeq: IdSeq, location: Location, options: Options = Options()): Result[Terrain] = {
    val (nextId, nextIdSeq) = idSeq()

    val terrain = Terrain(
      id = TerrainId(nextId),
      kind = id,
      location = location,
      tags = options.tags
    )

    Result(
      mutations = List(IdSeqMutation(nextIdSeq), NewEntityMutation(terrain)),
      nextIdSeq,
      terrain,
    )
  }
}
