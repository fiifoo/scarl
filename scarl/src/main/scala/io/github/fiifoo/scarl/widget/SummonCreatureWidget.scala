package io.github.fiifoo.scarl.widget

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.assets.CreatureCatalogueId
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.WidgetKind.Category
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.math.Rng.WeightedChoices
import io.github.fiifoo.scarl.status.SummonCreatureStatus

case class SummonCreatureWidget(id: WidgetKindId,
                                item: ItemKindId,
                                category: Option[Category] = None,
                                power: Option[Int] = None,
                                summon: CreatureCatalogueId,
                                summonDescription: Option[String] = None,
                                interval: Tick
                               ) extends WidgetKind {

  def createStatus(s: State, id: Int, target: ContainerId): Status = {
    val catalogues = s.assets.catalogues.creatures
    val choices = WeightedChoices(catalogues(summon).apply(catalogues))

    SummonCreatureStatus(
      id = ActiveStatusId(id),
      tick = s.tick,
      target = target,
      summon = choices,
      summonDescription = summonDescription,
      interval = interval
    )
  }
}
