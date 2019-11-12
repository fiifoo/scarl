package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.core.geometry.{Location, WaypointNetwork}
import io.github.fiifoo.scarl.core.kind.Kind.Options
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.core.mutation.{NewConduitEntranceMutation, NewConduitExitMutation, NewGatewayMutation}
import io.github.fiifoo.scarl.core.world.ConduitId
import io.github.fiifoo.scarl.core.{State, Tag}
import io.github.fiifoo.scarl.world.Conduit

import scala.util.Random

object ApplyTemplate {

  def apply(initial: State,
            template: Template.Result,
            in: List[Conduit],
            out: List[Conduit],
            random: Random
           ): State = {

    var state = initial.copy(area = initial.area.copy(
      height = template.shape.outerHeight,
      width = template.shape.outerWidth
    ))
    state = addContent(state, template)
    state = addWaypoints(state)
    state = addCreatures(state, template) // must be after waypoints
    state = addConduits(state, template, in, out, random)
    state = addGateways(state, template)

    state
  }

  private def addContent(initial: State,
                         template: Template.Result,
                         offset: Location = Location(0, 0)
                        ): State = {
    var state = initial

    state = processUniqueEntities(state, template.content.terrains, offset)
    state = processUniqueEntities(state, template.content.walls, offset)
    state = processEntities(state, template.content.items, offset, Options(faction = template.owner))
    state = processUniqueEntities(state, template.content.widgets, offset, Options(faction = template.owner))
    state = (template.content.machinery foldLeft state) ((s, machinery) => machinery(s, offset))

    template.templates.foldLeft(state)((s, data) => {
      val (location, sub) = data

      addContent(s, sub, offset.add(location))
    })
  }

  private def addCreatures(initial: State,
                           template: Template.Result,
                           offset: Location = Location(0, 0)
                          ): State = {
    val (leaders, rest) = template.content.creatures partition (x => {
      val kind = x._2._1

      kind(initial).traits.leader
    })

    val process =
      ((state: State) => processUniqueEntities(state, leaders, offset)) andThen
        ((state: State) => processUniqueEntities(state, rest, offset))

    val state = process(initial)

    template.templates.foldLeft(state)((s, data) => {
      val (location, sub) = data

      addCreatures(s, sub, offset.add(location))
    })
  }

  private def processEntities(s: State,
                              elements: Map[Location, List[(KindId, Set[Tag])]],
                              offset: Location,
                              options: Options = Options()
                             ): State = {

    elements.foldLeft(s)((s, x) => {
      val (location, elements) = x

      elements.foldLeft(s)((s, x) => {
        val (element, tags) = x

        element(s)
          .apply(s, s.idSeq, location.add(offset), options.copy(tags = tags))
          .write(s)
      })
    })
  }

  private def processUniqueEntities(s: State,
                                    elements: Map[Location, (KindId, Set[Tag])],
                                    offset: Location,
                                    options: Options = Options()
                                   ): State = {

    elements.foldLeft(s)((s, x) => {
      val (location, (element, tags)) = x

      element(s)
        .apply(s, s.idSeq, location.add(offset), options.copy(tags = tags))
        .write(s)
    })
  }

  private def addConduits(s: State,
                          template: Template.Result,
                          in: List[Conduit],
                          out: List[Conduit],
                          random: Random
                         ): State = {

    val locations = getConduitLocations(template)
    if (locations.size < in.size + out.size) {
      throw new CalculateFailedException
    }

    val data: List[(ConduitId, Option[ItemKindId], Option[Tag])] =
      (in map (conduit => (conduit.id, conduit.targetItem, conduit.tag))) :::
        (out map (conduit => (conduit.id, Some(conduit.sourceItem), conduit.tag)))

    val (result, _) = (data foldLeft ((s, locations))) ((carry, x) => {
      val (result, locations) = carry
      val (conduit, item, tag) = x

      val choices = (tag map (tag => {
        locations filter { case (_, b) => b.contains(tag) }
      }) getOrElse {
        locations filter { case (_, b) => b.isEmpty }
      }).keys

      if (choices.isEmpty) {
        throw new CalculateFailedException
      }

      val location = Rng.nextChoice(random, choices)

      (
        addConduit(result, conduit, item, location),
        locations - location
      )
    })

    result
  }

  private def addConduit(s: State,
                         conduit: ConduitId,
                         item: Option[ItemKindId],
                         location: Location
                        ): State = {
    val mutations = if (item.isDefined) {
      List(
        NewConduitEntranceMutation(conduit, location),
        NewConduitExitMutation(conduit, location)
      )
    } else {
      List(
        NewConduitExitMutation(conduit, location)
      )
    }

    val initial = item map (item => {
      item(s).apply(s, s.idSeq, location).write(s)
    }) getOrElse {
      s
    }

    (mutations foldLeft initial) ((s, mutation) => mutation(s))
  }

  private def getConduitLocations(template: Template.Result,
                                  offset: Location = Location(0, 0),
                                  result: Map[Location, Option[Tag]] = Map()
                                 ): Map[Location, Option[Tag]] = {
    val locations = template.content.conduitLocations map { case (k, v) => (offset.add(k), v) }

    (template.templates foldLeft (result ++ locations)) ((result, x) => {
      val (location, template) = x

      getConduitLocations(template, offset.add(location), result)
    })
  }

  private def addGateways(s: State, template: Template.Result): State = {
    val locations = getGatewayLocations(template)

    locations.foldLeft(s)((s, location) => {
      NewGatewayMutation(location)(s)
    })
  }

  private def getGatewayLocations(template: Template.Result): Set[Location] = {
    extractLocations(template, template => {
      template.content.gatewayLocations
    })
  }

  private def extractLocations(template: Template.Result,
                               extract: Template.Result => Set[Location],
                               offset: Location = Location(0, 0),
                               result: Set[Location] = Set()
                              ): Set[Location] = {
    val locations = extract(template) map offset.add

    template.templates.foldLeft(result ++ locations)((result, x) => {
      val (location, template) = x

      extractLocations(template, extract, offset.add(location), result)
    })
  }

  private def addWaypoints(s: State): State = {
    s.copy(
      cache = s.cache.copy(
        waypointNetwork = WaypointNetwork(s)
      ),
      tmp = s.tmp.copy(
        waypointNetworkChanged = Set()
      )
    )
  }
}
