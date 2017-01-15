package io.github.fiifoo.scarl.core.effect

import io.github.fiifoo.scarl.core.mutation.Mutation

object EffectResult {

  def apply(mutation: Mutation): EffectResult = EffectResult(List(mutation))

  def apply(effect: Effect): EffectResult = EffectResult(List(), List(effect))

  def apply(mutation: Mutation, effect: Effect): EffectResult = EffectResult(List(mutation), List(effect))

  def apply(effects: List[Effect]): EffectResult = EffectResult(List(), effects)

  def apply(mutation: Mutation, effects: List[Effect]): EffectResult = EffectResult(List(mutation), effects)

  def apply(mutations: List[Mutation], effect: Effect): EffectResult = EffectResult(mutations, List(effect))
}

case class EffectResult(mutations: List[Mutation] = List(), effects: List[Effect] = List())
