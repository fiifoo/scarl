package io.github.fiifoo.scarl.widget

import io.github.fiifoo.scarl.core.Rng.WeightedChoices
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.{Location, State}
import io.github.fiifoo.scarl.status.SummonCreatureStatus

case class SummonCreatureWidget(id: WidgetKindId,
                                item: ItemKindId,
                                summon: WeightedChoices[CreatureKindId],
                                interval: Int
                               ) extends WidgetKind {

  def apply(s: State, location: Location): (Container, Item, SummonCreatureStatus) = {
    val (container, _item) = item(s)(s, location)

    val status = SummonCreatureStatus(
      id = ActiveStatusId(s.nextEntityId + 2),
      tick = s.tick,
      target = container.id,
      summon = summon,
      interval = interval
    )

    (container, _item, status)
  }
}
