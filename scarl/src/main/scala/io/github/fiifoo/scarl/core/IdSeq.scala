package io.github.fiifoo.scarl.core

case class IdSeq(value: Int) {
  def apply(): (Int, IdSeq) = {
    (value, copy(value + 1))
  }
}
