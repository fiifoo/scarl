package io.github.fiifoo.scarl.generate

import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.{CreatureKind, WallKind, WidgetKind}
import io.github.fiifoo.scarl.core.mutation.{NewEntityMutation, NewFactionMutation, RngMutation}
import io.github.fiifoo.scarl.core.{Location, State}

import scala.util.Random

class Generator(locationConstraint: (Int, Int) = (80, 25)) {

  def factions(s: State, factions: Map[FactionId, Faction]): State = {
    factions.values.foldLeft(s)((s, faction) => {
      NewFactionMutation(faction)(s)
    })
  }

  def locatables(s: State, count: Int, mutate: (State, Location) => State): State = {
    val (random, rng) = s.rng()

    val result = (0 until count).foldLeft(s)((s, _) => {
      mutate(s, generateLocation(random))
    })

    RngMutation(rng)(result)
  }

  def walls(s: State, count: Int, kind: WallKind): State = {
    locatables(s, count, (s: State, location: Location) => {
      val wall = kind(s, location)

      NewEntityMutation(wall)(s)
    })
  }

  def creatures(s: State, count: Int, kind: CreatureKind): State = {
    locatables(s, count, (s: State, location: Location) => {
      val creature = kind(s, location)

      NewEntityMutation(creature)(s)
    })
  }

  def widgets(s: State, count: Int, kind: WidgetKind): State = {
    locatables(s, count, (s: State, location: Location) => {
      val (container, item, status) = kind(s, location)

      NewEntityMutation(status)(NewEntityMutation(item)(NewEntityMutation(container)(s)))
    })
  }

  def player(s: State, kind: CreatureKind): (State, CreatureId) = {
    val id = CreatureId(s.nextEntityId)

    val (random, rng) = s.rng()
    val location = generateLocation(random)
    val creature = kind(s, location)

    (
      RngMutation(rng)(NewEntityMutation(creature)(s)),
      id
    )
  }

  private def generateLocation(random: Random): Location = {
    val x = random.nextInt(locationConstraint._1)
    val y = random.nextInt(locationConstraint._2)

    Location(x, y)
  }
}
