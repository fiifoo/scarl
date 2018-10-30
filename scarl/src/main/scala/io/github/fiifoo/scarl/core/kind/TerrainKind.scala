package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.entity.{IdSeq, Terrain, TerrainId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.Kind.{Options, Result}
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}
import io.github.fiifoo.scarl.core.{Color, State}

object TerrainKind {

  sealed trait Category

  case object DefaultCategory extends Category

  case object ConstructedCategory extends Category

  case object NaturalCategory extends Category

}

case class TerrainKind(id: TerrainKindId,
                       name: String,
                       display: Char,
                       color: Color,
                       description: Option[String] = None,
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
