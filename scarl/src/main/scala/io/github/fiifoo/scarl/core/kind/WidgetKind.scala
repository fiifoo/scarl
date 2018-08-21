package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.Kind.{Options, Result}
import io.github.fiifoo.scarl.core.kind.WidgetKind.Category
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}


object WidgetKind {

  trait Category

  case object HealCategory extends Category

  case object PortalCategory extends Category

  case object TrapCategory extends Category

  val categories: Set[Category] = Set(
    HealCategory,
    PortalCategory,
    TrapCategory,
  )
}

trait WidgetKind extends Kind {
  val id: WidgetKindId
  val item: ItemKindId
  val category: Option[Category]
  val power: Option[Int]

  def apply(s: State, idSeq: IdSeq, location: Location, options: Options = Options()): Result[Container] = {
    val itemResult = item(s).apply(s, idSeq, location, options)
    val (nextId, nextIdSeq) = itemResult.idSeq()
    val status = createStatus(s, nextId, itemResult.entity.id)

    Result(
      mutations = itemResult.mutations ::: List(IdSeqMutation(nextIdSeq), NewEntityMutation(status)),
      idSeq = nextIdSeq,
      entity = itemResult.entity
    )
  }

  def createStatus(s: State, id: Int, target: ContainerId): Status
}
