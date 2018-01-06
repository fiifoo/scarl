package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.IdSeq

case class IdSeqMutation(idSeq: IdSeq) extends Mutation {
  def apply(s: State): State = {
    if (idSeq.value > s.idSeq.value) {
      s.copy(idSeq = idSeq)
    } else {
      s
    }
  }
}
