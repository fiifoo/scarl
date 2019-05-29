package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.entity.{IdSeq, Wall, WallId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.Kind.{Options, Result}
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}
import io.github.fiifoo.scarl.core.{Color, State}

object WallKind {

  sealed trait Category

  case object DefaultCategory extends Category

  case object AreaBorderCategory extends Category

  case object ConstructedCategory extends Category

  case object NaturalCategory extends Category

  case object SecureCategory extends Category

  case object TransparentCategory extends Category

}

case class WallKind(id: WallKindId,
                    name: String,
                    display: Char,
                    color: Color,
                    description: Option[String] = None,
                    hardness: Option[Int] = None,
                    transparent: Boolean = false,
                   ) extends Kind {

  def apply(s: State, idSeq: IdSeq, location: Location, options: Options = Options()): Result[Wall] = {
    val (nextId, nextIdSeq) = idSeq()

    val wall = Wall(
      id = WallId(nextId),
      kind = id,
      location = location,
      tags = options.tags,
      hardness = hardness,
      transparent = transparent
    )

    Result(
      mutations = List(IdSeqMutation(nextIdSeq), NewEntityMutation(wall)),
      nextIdSeq,
      wall
    )
  }
}
