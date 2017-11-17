package io.github.fiifoo.scarl.widget

import io.github.fiifoo.scarl.core.Rng.WeightedChoices
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.status.SummonCreatureStatus

case class SummonCreatureWidget(id: WidgetKindId,
                                item: ItemKindId,
                                summon: WeightedChoices[CreatureKindId],
                                summonDescription: Option[String] = None,
                                interval: Int
                               ) extends WidgetKind {

  def createStatus(s: State, id: Int, target: ContainerId): Status = {
    SummonCreatureStatus(
      id = ActiveStatusId(id),
      tick = s.tick,
      target = target,
      summon = summon,
      summonDescription = summonDescription,
      interval = interval
    )
  }
}
