package io.github.fiifoo.scarl.widget

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.Time.Tick
import io.github.fiifoo.scarl.core.assets.CreatureCatalogueId
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.math.Rng.WeightedChoices
import io.github.fiifoo.scarl.status.SummonCreatureStatus

case class SummonCreatureWidget(id: WidgetKindId,
                                item: ItemKindId,
                                power: Option[Int] = None,
                                summon: CreatureCatalogueId,
                                summonCategory: CreatureKind.Category = CreatureKind.DefaultCategory,
                                summonDescription: Option[String] = None,
                                interval: Tick
                               ) extends WidgetKind {

  def createStatus(s: State, id: Int, target: ContainerId): Status = {
    val catalogues = s.assets.catalogues.creatures
    val choices = WeightedChoices(catalogues(summon).apply(catalogues).getOrElse(summonCategory, List()))

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
