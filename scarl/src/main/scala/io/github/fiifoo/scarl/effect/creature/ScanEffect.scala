package io.github.fiifoo.scarl.effect.creature

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.creature.Stats
import io.github.fiifoo.scarl.core.effect.{Effect, EffectResult}
import io.github.fiifoo.scarl.core.entity.Selectors.{getCreatureStats, getItemLocation, isVisibleItem}
import io.github.fiifoo.scarl.core.entity.{CreatureId, Item}
import io.github.fiifoo.scarl.core.geometry.{Distance, Location, Los, Sector}
import io.github.fiifoo.scarl.core.mutation.RngMutation
import io.github.fiifoo.scarl.rule.DetectRule

import scala.util.Random

case class ScanEffect(creature: CreatureId,
                      location: Location,
                      parent: Option[Effect] = None
                     ) extends Effect {
  val maxDistance = 10

  def apply(s: State): EffectResult = {
    val (random, rng) = s.rng()

    val detected = scan(s, random).toList
    val effects = detected flatMap (item => {
      getItemLocation(s)(item.id) map (DetectEffect(creature, item.id, _))
    })

    EffectResult(
      RngMutation(rng),
      effects
    )
  }

  private def scan(s: State, random: Random): Set[Item] = {
    val stats = getCreatureStats(s)(creature)
    val sector = Sector(s)(location)
    val sectors = sector.adjacent + sector

    sectors flatMap (sector => {
      s.index.sectorItems.get(sector) map (items => {
        items map (_ (s)) filter (item => {
          isDetectableItem(s, item) && detectItem(s, stats, item, random)
        })
      }) getOrElse Set()
    })
  }

  private def isDetectableItem(s: State, item: Item): Boolean = {
    !isVisibleItem(s, creature)(item.id) && item.concealment > 0
  }

  private def detectItem(s: State, stats: Stats, item: Item, random: Random): Boolean = {
    getItemLocation(s)(item.id) exists (location => {
      Los.locations(s)(this.location, location, maxDistance) &&
        DetectRule(random)(stats.sight.sensors, item.concealment, Distance(this.location, location))
    })
  }
}
