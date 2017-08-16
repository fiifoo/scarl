package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.core.entity.Entity
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.mutation.{NewConduitMutation, NewEntityMutation, NewGatewayMutation}
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

    state = processUniqueEntities(
      s = state,
      elements = template.content.terrains,
      getEntities = (s: State, location: Location, kind: TerrainKindId) => {
        val terrain = kind(s)(s, offset.add(location))

        List(terrain)
      }
    )

    state = processUniqueEntities(
      s = state,
      elements = template.content.walls,
      getEntities = (s: State, location: Location, kind: WallKindId) => {
        val wall = kind(s)(s, offset.add(location))

        List(wall)
      }
    )

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

    state = processUniqueEntities(
      s = state,
      elements = template.content.creatures,
      getEntities = (s: State, location: Location, kind: CreatureKindId) => {
        val creature = kind(s)(s, offset.add(location))

        List(creature)
      }
    )

    state = processEntities(
      s = state,
      elements = template.content.items,
      getEntities = (s: State, location: Location, kind: ItemKindId) => {
        val (container, item) = kind(s)(s, offset.add(location))

        List(container, item)
      }
    )

    state = processUniqueEntities(
      s = state,
      elements = template.content.widgets,
      getEntities = (s: State, location: Location, kind: WidgetKindId) => {
        val (container, item, status) = kind(s)(s, offset.add(location))

        List(container, item, status)
      }
    )

    template.templates.foldLeft(state)((s, data) => {
      val (location, sub) = data

      addContent(s, sub, offset.add(location))
    })
  }

  private def processEntities[T](s: State,
                                 elements: Map[Location, List[T]],
                                 getEntities: (State, Location, T) => List[Entity]
                                ): State = {

    elements.foldLeft(s)((s, data) => {
      val (location, locationElements) = data

      locationElements.foldLeft(s)((s, element) => {
        val entities = getEntities(s, location, element)

        entities.foldLeft(s)((s, entity) => NewEntityMutation(entity)(s))
      })
    })
  }

  private def processUniqueEntities[T](s: State,
                                       elements: Map[Location, T],
                                       getEntities: (State, Location, T) => List[Entity]
                                      ): State = {

    elements.foldLeft(s)((s, data) => {
      val (location, element) = data
      val entities = getEntities(s, location, element)

      entities.foldLeft(s)((s, entity) => NewEntityMutation(entity)(s))
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

    val (container, _item) = item(s)(s, location)

    NewConduitMutation(conduit, location)(
      NewEntityMutation(_item)(
        NewEntityMutation(container)(s))
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
