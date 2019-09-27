package io.github.fiifoo.scarl.core.mutation

import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.entity.Selectors.getCreatureStanceStatuses
import io.github.fiifoo.scarl.core.entity.{ActorId, CreatureId}

case class InitializeTickMutation(actor: ActorId) extends Mutation {

  def apply(s: State): State = {
    val next = actor match {
      case creature: CreatureId => applyCreature(s, creature)
      case _ => s
    }

    if (next.tmp.removableEntities.nonEmpty) {
      RemoveEntitiesMutation()(next)
    } else {
      next
    }
  }

  private def applyCreature(s: State, creature: CreatureId): State = {
    val mutations = endStances(s, creature)

    (mutations foldLeft s) ((s, mutation) => mutation(s))
  }

  private def endStances(s: State, creature: CreatureId): List[Mutation] = {
    val end = getCreatureStanceStatuses(s)(creature) filter (_.duration exists (_ < 1))

    if (end.isEmpty) {
      List()
    } else {
      val activate = getCreatureStanceStatuses(s, active = false)(creature)

      val activateMutations = activate.toList map (status => {
        StanceActiveMutation(status, active = true)
      })
      val endMutations = end.toList map (status => {
        RemovableEntityMutation(status.id)
      })

      activateMutations ::: endMutations
    }
  }
}
