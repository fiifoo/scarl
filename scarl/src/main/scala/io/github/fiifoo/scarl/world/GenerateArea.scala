package io.github.fiifoo.scarl.world

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.area.template.{ApplyTemplate, CalculateFailedException, CalculateTemplate}
import io.github.fiifoo.scarl.core._
import io.github.fiifoo.scarl.core.ai.{Brain, Strategy}
import io.github.fiifoo.scarl.core.creature.FactionId
import io.github.fiifoo.scarl.core.entity.{GlobalActor, GlobalActorId, IdSeq}
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.core.math.Rng.WeightedChoices
import io.github.fiifoo.scarl.core.mutation.{IdSeqMutation, NewEntityMutation}
import io.github.fiifoo.scarl.world.Site.AreaSource

import scala.util.Random

object GenerateArea {

  def apply(site: SiteId, rng: Rng, idSeq: IdSeq = IdSeq(1))(world: WorldState): WorldState = {
    val (random, _) = rng()
    val assets = world.assets
    val (variant, areaId) = selectArea(world, assets.sites(site), random)
    val area = assets.areas(areaId)

    var state = State(
      assets = assets.instance(),
      factions = State.Factions(
        brains = createBrains(assets, area.strategies),
        dispositions = area.dispositions,
        strategies = area.strategies,
      ),
      idSeq = idSeq,
      rng = rng
    )

    val template = assets.templates(area.template)
    val templateResult = CalculateTemplate(assets, area, world.usedUniqueTemplates, random)(template)

    val in = (world.conduits.values filter (_.target == site)).toList
    val out = (world.conduits.values filter (_.source == site)).toList

    state = ApplyTemplate(state, templateResult, in, out, random)
    state = createGlobalActor(state)

    world.copy(
      states = world.states + (site -> state),
      usedAreas = world.usedAreas + areaId,
      usedUniqueTemplates = world.usedUniqueTemplates ++ templateResult.collectUsedUniqueTemplates(assets),
      variants = world.variants + (assets.sites(site).region -> variant)
    )
  }

  private def selectArea(world: WorldState, site: Site, random: Random): (Option[RegionVariantKey], AreaId) = {
    val (variant, source) = selectAreaSource(world, site)

    val area: AreaId = source.priority flatMap (area => {
      if (world.usedAreas.contains(area)) {
        None
      } else {
        Some(area)
      }
    }) getOrElse {
      val choices = source.choices filter (choice => {
        val area = choice.value

        !world.assets.areas(area).unique || !world.usedAreas.contains(area)
      })

      if (choices.isEmpty) {
        source.priority getOrElse {
          throw new CalculateFailedException // ...
        }
      } else {
        WeightedChoices(choices)(random)
      }
    }

    (variant, area)
  }

  private def selectAreaSource(world: WorldState, site: Site): (Option[RegionVariantKey], AreaSource) = {
    val variant = world.variants.getOrElse(
      site.region,
      selectVariant(world, world.assets.regions(site.region))
    )
    val source = variant flatMap site.variants.get getOrElse site.area

    (variant, source)
  }

  private def selectVariant(world: WorldState, region: Region): Option[RegionVariantKey] = {
    region.variants find (_.requirements.apply(world)) map (_.key)
  }

  private def createBrains(assets: WorldAssets, strategies: Map[FactionId, Strategy]): Map[FactionId, Brain] = {
    (assets.factions.values flatMap (faction => {
      val strategy = strategies.get(faction.id).orElse(faction.strategy)

      strategy map (strategy => {
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
