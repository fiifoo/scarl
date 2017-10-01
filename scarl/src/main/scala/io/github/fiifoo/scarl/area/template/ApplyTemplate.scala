package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.mutation.{NewConduitMutation, NewGatewayMutation}
import io.github.fiifoo.scarl.core.world.ConduitId
import io.github.fiifoo.scarl.core.{Location, Rng, State}
import io.github.fiifoo.scarl.geometry.WaypointNetwork
import io.github.fiifoo.scarl.world.Conduit

import scala.util.Random

object ApplyTemplate {

  def apply(initial: State,
            template: Template.Result,
            in: List[Conduit],
            out: List[Conduit],
            random: Random
           ): State = {

    var state = initial.copy(area = State.Area(
      height = template.shape.outerHeight,
      width = template.shape.outerWidth
    ))
    state = addLayout(state, template)
    state = addWaypoints(state)
    state = addContent(state, template)
    state = addConduits(state, template, in, out, random)
    state = addGateways(state, template)

    state
  }

  private def addLayout(initial: State,
                        template: Template.Result,
                        offset: Location = Location(0, 0)
                       ): State = {
    var state = initial

    state = processUniqueEntities(state, template.content.terrains, offset)
    state = processUniqueEntities(state, template.content.walls, offset)

    template.templates.foldLeft(state)((s, data) => {
      val (location, sub) = data

      addLayout(s, sub, offset.add(location))
    })
  }

  private def addContent(initial: State,
                         template: Template.Result,
                         offset: Location = Location(0, 0)
                        ): State = {
    var state = initial

    state = processUniqueEntities(state, template.content.creatures, offset)
    state = processEntities(state, template.content.items, offset)
    state = processUniqueEntities(state, template.content.widgets, offset)
    state = (template.content.machinery foldLeft state) ((s, machinery) => machinery(s, offset))

    template.templates.foldLeft(state)((s, data) => {
      val (location, sub) = data

      addContent(s, sub, offset.add(location))
    })
  }

  private def processEntities(s: State,
                              elements: Map[Location, List[KindId]],
                              offset: Location,
                             ): State = {

    elements.foldLeft(s)((s, data) => {
      val (location, locationElements) = data

      locationElements.foldLeft(s)((s, element) => {
        element(s).toLocation(s, s.idSeq, location.add(offset)).write(s)
      })
    })
  }

  private def processUniqueEntities(s: State,
                                    elements: Map[Location, KindId],
                                    offset: Location,
                                   ): State = {

    elements.foldLeft(s)((s, data) => {
      val (location, element) = data

      element(s).toLocation(s, s.idSeq, location.add(offset)).write(s)
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

    val data: List[(ConduitId, ItemKindId)] =
      (in map (conduit => (conduit.id, conduit.targetItem))) :::
        (out map (conduit => (conduit.id, conduit.sourceItem)))

    val fold = data.foldLeft((s, locations)) _

    val (result, _) = fold((carry, x) => {
      val (result, locations) = carry
      val (conduit, item) = x
      val location = Rng.nextChoice(random, locations)

      (
        addConduit(result, conduit, item, location),
        locations - location
      )
    })

    result
  }

  private def addConduit(s: State,
                         conduit: ConduitId,
                         item: ItemKindId,
                         location: Location
                        ): State = {
    NewConduitMutation(conduit, location)(
      item(s).toLocation(s, s.idSeq, location).write(s)
    )
  }

  private def getConduitLocations(template: Template.Result): Set[Location] = {
    extractLocations(template, template => {
      template.content.conduitLocations
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
    s.copy(cache = s.cache.copy(
      waypointNetwork = WaypointNetwork(s)
    ))
  }
}
