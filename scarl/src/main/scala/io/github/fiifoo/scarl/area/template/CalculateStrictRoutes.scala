package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.core.geometry.{Distance, Location}

import scala.annotation.tailrec

object CalculateStrictRoutes {

  private case class State(unconnected: Set[Location],
                           connected: Set[Location] = Set(),
                           loose: Set[Location] = Set(),
                           result: Set[Location] = Set()
                          )

  def apply(entrances: Iterable[Set[Location]], locations: Set[Location]): Set[Location] = {
    if (entrances.size <= 1) {
      return Set()
    }

    val groups = (entrances flatMap (x => {
      x map (_ -> x)
    })).toMap

    @tailrec
    def step(state: State): State = {
      if (state.unconnected.isEmpty) {
        state
      } else {
        val (from, choices) = if (state.loose.isEmpty) {
          val from = state.unconnected.head
          val choices = if (state.connected.isEmpty) {
            state.unconnected
          } else {
            state.connected
          }

          (from, choices)
        } else {
          val from = state.loose.head
          val choices = state.unconnected

          (from, choices)
        }

        val group = groups(from)

        val to = if ((choices -- group).isEmpty) {
          this.select(state.connected -- group, from)
        } else {
          this.select(choices -- group, from)
        }

        val route = CalculateRoute(from, to, locations) match {
          case Some(x) => x
          case None => throw new CalculateFailedException
        }

        step(state.copy(
          unconnected = state.unconnected - from - to,
          connected = state.connected + from + to,
          loose = state.loose ++ group ++ groups(to) -- state.connected - from - to,
          result = state.result ++ route
        ))
      }
    }

    step(State(entrances.flatten.toSet)).result
  }

  private def select(targets: Set[Location], from: Location): Location = {
    (targets.tail foldLeft targets.head) ((result, candidate) => {
      if (Distance(from, candidate) < Distance(from, result)) {
        candidate
      } else {
        result
      }
    })
  }
}
