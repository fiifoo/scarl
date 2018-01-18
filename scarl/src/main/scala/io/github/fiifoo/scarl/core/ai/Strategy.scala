package io.github.fiifoo.scarl.core.ai

import io.github.fiifoo.scarl.core.State

import scala.util.Random

trait Strategy {
  def apply(s: State, brain: Brain, random: Random): Brain
}
