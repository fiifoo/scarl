package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.template.{ApplyTemplate, CalculateTemplate}
import io.github.fiifoo.scarl.area.{Area, AreaId}
import io.github.fiifoo.scarl.core._
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
      idSeq = idSeq,
      rng = rng,
    )

    val (random, _) = state.rng()
    val template = assets.templates(assets.areas(area).template)
    val templateResult = CalculateTemplate(template, assets.templates, random)

    val in = (world.conduits.values filter (_.target == area)).toList
    val (out, nextConduitId) = createConduits(assets.areas(area), world.nextConduitId)

    state = ApplyTemplate(state, templateResult, in, out, random)

    world.copy(
      states = world.states + (area -> state),
      conduits = world.conduits ++ (out map (c => (c.id, c))).toMap,
      nextConduitId = nextConduitId
    )
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
