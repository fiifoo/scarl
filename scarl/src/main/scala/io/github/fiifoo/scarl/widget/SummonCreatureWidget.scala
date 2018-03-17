package io.github.fiifoo.scarl.widget

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.WidgetKind.Category
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.math.Rng.WeightedChoices
import io.github.fiifoo.scarl.status.SummonCreatureStatus

case class SummonCreatureWidget(id: WidgetKindId,
                                item: ItemKindId,
                                category: Option[Category] = None,
                                power: Option[Int] = None,
                                summon: WeightedChoices[CreatureKindId],
                                summonDescription: Option[String] = None,
                                interval: Tick
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
