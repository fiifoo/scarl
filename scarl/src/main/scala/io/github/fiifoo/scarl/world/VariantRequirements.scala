package io.github.fiifoo.scarl.world

case class VariantRequirements(foo: Int) {
  def apply(world: WorldState): Boolean = {
    foo > 0
  }
}
