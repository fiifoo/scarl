package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.area.template.{ApplyTemplate, CalculateTemplate}
import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.ai.Brain
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity.{GlobalActor, GlobalActorId, IdSeq}
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}

object GenerateArea {

  def apply(world: WorldState,
            area: AreaId,
            rng: Rng,
            idSeq: IdSeq = IdSeq(1),
           ): WorldState = {

    val assets = world.assets

    var state = State(
      area = State.Area(owner = assets.areas(area).owner),
      assets = assets.instance(),
      brains = createBrains(assets),
      idSeq = idSeq,
      rng = rng
    )

    val (random, _) = state.rng()
    val template = assets.templates(assets.areas(area).template)
    val templateResult = CalculateTemplate(assets, assets.areas(area), random)(template)

    val in = (world.conduits.values filter (_.target == area)).toList
    val out = (world.conduits.values filter (_.source == area)).toList

    state = ApplyTemplate(state, templateResult, in, out, random)
    state = createGlobalActor(state)

    world.copy(
      states = world.states + (area -> state)
    )
  }

  private def createBrains(assets: WorldAssets): Map[FactionId, Brain] = {
    (assets.factions.values flatMap (faction => {
      faction.strategy map (strategy => {
        (faction.id, Brain(faction.id, strategy))
      })
    })).toMap
  }

  private def createGlobalActor(state: State): State = {
    val (nextId, nextIdSeq) = state.idSeq.apply()
    val global = GlobalActor(GlobalActorId(nextId), state.tick)

    NewEntityMutation(global)(IdSeqMutation(nextIdSeq)(state))
  }
}
