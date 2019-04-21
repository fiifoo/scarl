package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.core.geometry.{Distance, Location}

object CalculateRoutes {

  def apply(entrances: Set[Location], locations: Set[Location]): Set[Location] = {
    if (entrances.size <= 1) {
      return Set()
    }

    val fold = entrances.foldLeft[(Set[Location], Set[Location])](Set(), Set()) _

    val (results, _) = fold((carry, from) => {
      val (results, connected) = carry

      val to = if (connected.isEmpty) {
        this.select(entrances - from, from)
      } else {
        this.select(connected - from, from)
      }

      val route = CalculateRoute(from, to, locations)
      if (route.isEmpty) {
        throw new CalculateFailedException
      }

      (results ++ route.get, connected + from + to)
    })

    results
  }

  private def select(targets: Set[Location], from: Location): Location = {
    (targets.tail foldLeft targets.head) ((result, candidate) => {
      if (Distance(from, candidate) < Distance(result, candidate)) {
        candidate
      } else {
        result
      }
    })
  }
}
