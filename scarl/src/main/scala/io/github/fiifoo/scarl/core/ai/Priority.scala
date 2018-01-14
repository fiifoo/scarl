package io.github.fiifoo.scarl.core.ai

object Priority {
  type Value = Int

  val infinite: Value = 9999
  val top: Value = 100
  val high: Value = 75
  val medium: Value = 50
  val low: Value = 25
  val none: Value = 0
}
