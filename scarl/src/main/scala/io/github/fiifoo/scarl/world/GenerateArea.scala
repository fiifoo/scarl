package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.template.{ApplyTemplate, CalculateTemplate}
import io.github.fiifoo.scarl.area.{Area, AreaId}
import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.ai.Brain
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity.IdSeq
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.core.world.ConduitId

object GenerateArea {

  def apply(world: WorldState,
            area: AreaId,
            rng: Rng,
            idSeq: IdSeq = IdSeq(1),
           ): WorldState = {

    val assets = world.assets

    var state = State(
      assets = assets.instance(),
      brains = createBrains(assets),
      idSeq = idSeq,
      rng = rng
    )

    val (random, _) = state.rng()
    val template = assets.templates(assets.areas(area).template)
    val theme = assets.themes(assets.areas(area).theme)
    val templateResult = CalculateTemplate(assets, theme, random)(template)

    val in = (world.conduits.values filter (_.target == area)).toList
    val (out, nextConduitId) = createConduits(assets.areas(area), world.nextConduitId)

    state = ApplyTemplate(state, templateResult, in, out, random)

    world.copy(
      states = world.states + (area -> state),
      conduits = world.conduits ++ (out map (c => (c.id, c))).toMap,
      nextConduitId = nextConduitId
    )
  }

  private def createBrains(assets: WorldAssets): Map[FactionId, Brain] = {
    (assets.factions.values flatMap (faction => {
      faction.strategy map (strategy => {
        (faction.id, Brain(faction.id, strategy))
      })
    })).toMap
  }

  private def createConduits(area: Area, nextId: Int): (List[Conduit], Int) = {
    val fold = area.conduits.foldLeft[(List[Conduit], Int)](List(), nextId) _

    fold((carry, x) => {
      val (result, nextId) = carry
      val (target, sourceItem, targetItem) = x

      val conduit = Conduit(ConduitId(nextId), area.id, target, sourceItem, targetItem)

      (conduit :: result, nextId + 1)
    })
  }
}
