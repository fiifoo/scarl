package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.core.{Location, Rng}

import scala.util.Random

object CalculateRoutes {

  def apply(entrances: Set[Location], locations: Set[Location], random: Random): Set[Location] = {
    if (entrances.size <= 1) {
      return Set()
    }

    val fold = entrances.foldLeft[(Set[Location], Set[Location])](Set(), Set()) _

    val (results, _) = fold((carry, from) => {
      val (results, connected) = carry

      val to = if (connected.isEmpty) {
        Rng.nextChoice(random, entrances - from)
      } else {
        Rng.nextChoice(random, connected - from)
      }

      val route = CalculateRoute(from, to, locations)
      if (route.isEmpty) {
        throw new CalculateFailedException
      }

      (results ++ route.get, connected + from + to)
    })

    results
  }
}
