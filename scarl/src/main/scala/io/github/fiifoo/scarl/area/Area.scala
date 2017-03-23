package io.github.fiifoo.scarl.area

import io.github.fiifoo.scarl.area.template.TemplateId
import io.github.fiifoo.scarl.core.kind.ItemKindId

case class Area(id: AreaId,
                template: TemplateId,
                conduits: List[(AreaId, ItemKindId, ItemKindId)] = List()
               )
