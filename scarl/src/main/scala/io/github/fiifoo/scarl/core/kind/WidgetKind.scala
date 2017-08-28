package io.github.fiifoo.scarl.core.kind

import io.github.fiifoo.scarl.core.entity.{Container, ContainerId, Status}
import io.github.fiifoo.scarl.core.kind.Kind.Result
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}
import io.github.fiifoo.scarl.core.{IdSeq, Location, State}

trait WidgetKind extends Kind[Container] {
  val id: WidgetKindId
  val item: ItemKindId

  def toLocation(s: State, idSeq: IdSeq, location: Location): Result[Container] = {
    val itemResult = item(s).toLocation(s, idSeq, location)
    val (nextId, nextIdSeq) = itemResult.idSeq()
    val status = createStatus(s, nextId, itemResult.entity.id)

    Result(
      mutations = itemResult.mutations ::: List(IdSeqMutation(nextIdSeq), NewEntityMutation(status)),
      idSeq = nextIdSeq,
      entity = itemResult.entity,
    )
  }

  def createStatus(s: State, id: Int, target: ContainerId): Status
}
