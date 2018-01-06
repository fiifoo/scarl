package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.Kind.Result
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}

trait WidgetKind extends Kind {
  val id: WidgetKindId
  val item: ItemKindId

  def toLocation(s: State, idSeq: IdSeq, location: Location, owner: Option[SafeCreatureId]): Result[Container] = {
    val itemResult = item(s).toWidget(s, idSeq, location, owner)
    val (nextId, nextIdSeq) = itemResult.idSeq()
    val status = createStatus(s, nextId, itemResult.entity.id)

    Result(
      mutations = itemResult.mutations ::: List(IdSeqMutation(nextIdSeq), NewEntityMutation(status)),
      idSeq = nextIdSeq,
      entity = itemResult.entity,
    )
  }

  def toLocation(s: State, idSeq: IdSeq, location: Location): Result[Container] = {
    toLocation(s, idSeq, location, None)
  }

  def createStatus(s: State, id: Int, target: ContainerId): Status
}
