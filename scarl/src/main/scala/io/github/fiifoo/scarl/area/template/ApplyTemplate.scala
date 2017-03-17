package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.core.entity.Entity
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation
import io.github.fiifoo.scarl.core.{Location, State}

object ApplyTemplate {

  def apply(initial: State, template: Template.Result, offset: Location = Location(0, 0)): State = {

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

      apply(s, sub, offset.add(location))
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
}
