package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.action._
import io.github.fiifoo.scarl.action.validate.ValidatorUtils._
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Selectors.getItemLocation

object ActionValidator {

  def apply(s: State, actor: CreatureId, action: Action): Boolean = {
    action match {
      case action: AttackAction => validate(s, actor, action)
      case action: CommunicateAction => validate(s, actor, action)
      case action: DisplaceAction => validate(s, actor, action)
      case action: DropItemAction => validate(s, actor, action)
      case action: EnterConduitAction => validate(s, actor, action)
      case action: EquipItemAction => EquipItemValidator(s, actor, action)
      case action: HackItemAction => validate(s, actor, action)
      case action: MoveAction => validate(s, actor, action)
      case PassAction => true
      case action: PickItemAction => validate(s, actor, action)
      case _: ShootAction => true
      case _: ShootMissileAction => true
      case action: UnequipItemAction => validate(s, actor, action)
      case action: UseCreatureAction => validate(s, actor, action)
      case action: UseDoorAction => validate(s, actor, action)
      case action: UseItemAction => validate(s, actor, action)
      case _ => false
    }
  }

  private def validate(s: State, actor: CreatureId, action: AttackAction): Boolean = {
    entityExists(s)(action.target) &&
      isAdjacentLocation(s, actor)(action.target(s).location)
  }

  private def validate(s: State, actor: CreatureId, action: CommunicateAction): Boolean = {
    entityExists(s)(action.target) &&
      isAdjacentLocation(s, actor)(action.target(s).location)
  }

  private def validate(s: State, actor: CreatureId, action: DisplaceAction): Boolean = {
    !isEnemy(s, actor, action.target) && isAdjacentLocation(s, actor)(action.target(s).location)
  }

  private def validate(s: State, actor: CreatureId, action: DropItemAction): Boolean = {
    entityExists(s)(action.item) &&
      action.item(s).pickable &&
      action.item(s).container == actor
  }

  private def validate(s: State, actor: CreatureId, action: EnterConduitAction): Boolean = {
    actor(s).location == s.conduits.entrances(action.conduit)
  }

  private def validate(s: State, actor: CreatureId, action: HackItemAction): Boolean = {
    entityExists(s)(action.target) &&
      action.target(s).locked.isDefined &&
      (getItemLocation(s)(action.target) exists isAdjacentOrCurrentLocation(s, actor))
  }

  private def validate(s: State, actor: CreatureId, action: MoveAction): Boolean = {
    isAdjacentLocation(s, actor)(action.location)
  }

  private def validate(s: State, actor: CreatureId, action: PickItemAction): Boolean = {
    entityExists(s)(action.item) &&
      action.item(s).pickable &&
      (getItemLocation(s)(action.item) contains actor(s).location)
  }

  private def validate(s: State, actor: CreatureId, action: UnequipItemAction): Boolean = {
    entityExists(s)(action.item) &&
      action.item(s).container == actor &&
      (s.equipments.get(actor) exists (_.values exists (_ == action.item)))
  }

  private def validate(s: State, actor: CreatureId, action: UseCreatureAction): Boolean = {
    entityExists(s)(action.target) &&
      action.target(s).usable.isDefined &&
      isAdjacentLocation(s, actor)(action.target(s).location)
  }

  private def validate(s: State, actor: CreatureId, action: UseDoorAction): Boolean = {
    entityExists(s)(action.target) &&
      action.target(s).door.isDefined &&
      (getItemLocation(s)(action.target) exists isAdjacentLocation(s, actor))
  }

  private def validate(s: State, actor: CreatureId, action: UseItemAction): Boolean = {
    entityExists(s)(action.target) &&
      action.target(s).usable.isDefined &&
      (action.target(s).container == actor || (getItemLocation(s)(action.target) exists isAdjacentOrCurrentLocation(s, actor)))
  }
}
