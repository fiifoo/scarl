package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.{IdSeq, State}

case class IdSeqMutation(idSeq: IdSeq) extends Mutation {
  def apply(s: State): State = {
    if (idSeq.value > s.idSeq.value) {
      s.copy(idSeq = idSeq)
    } else {
      s
    }
  }
}
