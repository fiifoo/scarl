package models

import io.github.fiifoo.scarl.area.template.{ApplyTemplate, CalculateTemplate, Template, TemplateId}
import io.github.fiifoo.scarl.core.entity._
import io.github.fiifoo.scarl.core.kind.{CreatureKind, CreatureKindId, Kinds}
import io.github.fiifoo.scarl.core.mutation.{NewEntityMutation, NewFactionMutation}
import io.github.fiifoo.scarl.core.{Rng, State}

import scala.util.Random

object GenerateBubble {

  def apply(factions: Map[FactionId, Faction],
            kinds: Kinds,
            templates: Map[TemplateId, Template]
           ): (State, CreatureId) = {

    val initial = factions.values.foldLeft(State(kinds = kinds))((s, faction) => {
      NewFactionMutation(faction)(s)
    })

    val (random, _) = initial.rng()
    val template = templates(TemplateId("main"))
    val templateResult = CalculateTemplate(template, templates, random)
    val state = ApplyTemplate(initial, templateResult)

    player(state, kinds.creatures(CreatureKindId("hero")), templateResult, random)
  }

  private def player(s: State,
                     kind: CreatureKind,
                     templateResult: Template.Result,
                     random: Random
                    ): (State, CreatureId) = {
    val id = CreatureId(s.nextEntityId)
    // temp until areas have conduits
    val location = Rng.nextChoice(random, templateResult.shape.contained)
    val creature = kind(s, location)

    (
      NewEntityMutation(creature)(s),
      id
    )
  }
}
