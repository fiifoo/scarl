package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.action._
import io.github.fiifoo.scarl.action.validate.ValidatorUtils._
import io.github.fiifoo.scarl.core.Selectors.getItemLocation
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.CreatureId

object ActionValidator {

  def apply(s: State, actor: CreatureId, action: Action): Boolean = {
    action match {
      case action: AttackAction => validate(s, actor, action)
      case action: CommunicateAction => validate(s, actor, action)
      case action: EnterConduitAction => validate(s, actor, action)
      case action: EquipItemAction => EquipItemValidator(s, actor, action)
      case action: MoveAction => validate(s, actor, action)
      case action: PassAction => validate(s, actor, action)
      case action: PickItemAction => validate(s, actor, action)
      case action: ShootAction => validate(s, actor, action)
      case action: UseDoorAction => validate(s, actor, action)
      case _ => false
    }
  }

  private def validate(s: State, actor: CreatureId, action: AttackAction): Boolean = {
    entityExists(s, action.target) &&
      adjacentLocation(s, actor, action.target(s).location)
  }

  private def validate(s: State, actor: CreatureId, action: CommunicateAction): Boolean = {
    entityExists(s, action.target) &&
      adjacentLocation(s, actor, action.target(s).location)
  }

  private def validate(s: State, actor: CreatureId, action: EnterConduitAction): Boolean = {
    actor(s).location == s.conduits(action.conduit)
  }

  private def validate(s: State, actor: CreatureId, action: MoveAction): Boolean = {
    adjacentLocation(s, actor, action.location)
  }

  private def validate(s: State, actor: CreatureId, action: PassAction): Boolean = true

  private def validate(s: State, actor: CreatureId, action: PickItemAction): Boolean = {
    if (!entityExists(s, action.item)) {
      return false
    }

    action.item(s).pickable && (getItemLocation(s)(action.item) contains actor(s).location)
  }

  private def validate(s: State, actor: CreatureId, action: ShootAction): Boolean = true

  private def validate(s: State, actor: CreatureId, action: UseDoorAction): Boolean = {
    if (!entityExists(s, action.target) || action.target(s).door.isEmpty) {
      return false
    }

    getItemLocation(s)(action.target) exists (adjacentLocation(s, actor, _))
  }
}
