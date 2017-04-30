package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.core.entity.{Entity, TriggerStatusId}
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.mutation.{NewConduitMutation, NewEntityMutation, NewGatewayMutation}
import io.github.fiifoo.scarl.core.world.ConduitId
import io.github.fiifoo.scarl.core.{Location, Rng, State}
import io.github.fiifoo.scarl.status.TriggeredConduitStatus
import io.github.fiifoo.scarl.world.Conduit

import scala.util.Random

object ApplyTemplate {

  def apply(initial: State,
            template: Template.Result,
            in: List[Conduit],
            out: List[Conduit],
            random: Random
           ): State = {

    val processed = process(initial, template)
    val conduits = addConduits(processed, template, in, out, random)
    val gateways = addGateways(conduits, template)

    gateways
  }

  private def process(initial: State,
                      template: Template.Result,
                      offset: Location = Location(0, 0)
                     ): State = {

    val creatures = processUniqueEntities(
      s = initial,
      elements = template.content.creatures,
      getEntities = (s: State, location: Location, kind: CreatureKindId) => {
        val creature = kind(s)(s, offset.add(location))

        List(creature)
      }
    )

    val items = processEntities(
      s = creatures,
      elements = template.content.items,
      getEntities = (s: State, location: Location, kind: ItemKindId) => {
        val (container, item) = kind(s)(s, offset.add(location))

        List(container, item)
      }
    )

    val terrains = processUniqueEntities(
      s = items,
      elements = template.content.terrains,
      getEntities = (s: State, location: Location, kind: TerrainKindId) => {
        val terrain = kind(s)(s, offset.add(location))

        List(terrain)
      }
    )

    val walls = processUniqueEntities(
      s = terrains,
      elements = template.content.walls,
      getEntities = (s: State, location: Location, kind: WallKindId) => {
        val wall = kind(s)(s, offset.add(location))

        List(wall)
      }
    )

    val widgets = processUniqueEntities(
      s = walls,
      elements = template.content.widgets,
      getEntities = (s: State, location: Location, kind: WidgetKindId) => {
        val (container, item, status) = kind(s)(s, offset.add(location))

        List(container, item, status)
      }
    )

    val subs = template.templates.foldLeft(widgets)((s, data) => {
      val (location, sub) = data

      process(s, sub, offset.add(location))
    })

    subs
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

    val status = TriggeredConduitStatus(
      id = TriggerStatusId(s.nextEntityId + 2),
      target = container.id,
      conduit = conduit
    )

    NewConduitMutation(conduit, location)(
      NewEntityMutation(status)(
        NewEntityMutation(_item)(
          NewEntityMutation(container)(s))))
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
}
