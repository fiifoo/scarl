package io.github.fiifoo.scarl.core

import io.github.fiifoo.scarl.core.entity._

object Selectors {

  def getLocationEntities(location: Location)(s: State): List[Locatable] = {
    s.index.entities.location.getOrElse(location, List()) map (id => s.entities(id).asInstanceOf[Locatable])
  }

  def getTargetStatuses(target: EntityId)(s: State): List[Status] = {
    s.index.statuses.target.getOrElse(target, List()) map (id => s.entities(id).asInstanceOf[Status])
  }

  def getContainerItems(container: EntityId)(s: State): List[Item] = {
    s.index.items.container.getOrElse(container, List()) map (id => s.entities(id).asInstanceOf[Item])
  }
}
