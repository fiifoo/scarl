package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.action._
import io.github.fiifoo.scarl.action.validate.ValidatorUtils._
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.entity.Selectors.{getCreatureStats, getItemLocation}

object ActionValidator {

  def apply(s: State, actor: CreatureId, action: Action): Boolean = {
    if (s.creature.conversations.isDefinedAt(actor)) {
      action match {
        case action: ConverseAction => validate(s, actor, action)
        case EndConversationAction => true
        case _ => false
      }
    } else {
      action match {
        case action: AttackAction => validate(s, actor, action)
        case action: CancelRecycleItemAction => validate(s, actor, action)
        case action: ChangeStanceAction => validate(s, actor, action)
        case action: CommunicateAction => validate(s, actor, action)
        case action: CraftItemAction => validate(s, actor, action)
        case action: DisplaceAction => validate(s, actor, action)
        case action: DropItemAction => validate(s, actor, action)
        case action: EnterConduitAction => validate(s, actor, action)
        case action: EquipItemAction => EquipItemValidator(s, actor, action)
        case action: EquipWeaponsAction => EquipItemValidator(s, actor, action)
        case action: HackCreatureAction => validate(s, actor, action)
        case action: HackItemAction => validate(s, actor, action)
        case action: MoveAction => validate(s, actor, action)
        case PassAction => true
        case action: PickItemAction => validate(s, actor, action)
        case action: RecycleItemAction => validate(s, actor, action)
        case action: ShootAction => validate(s, actor, action)
        case action: ShootMissileAction => validate(s, actor, action)
        case action: UnequipItemAction => validate(s, actor, action)
        case action: UseCreatureAction => validate(s, actor, action)
        case action: UseDoorAction => validate(s, actor, action)
        case action: UseItemAction => validate(s, actor, action)
        case _ => false
      }
    }
  }

  private def validate(s: State, actor: CreatureId, action: AttackAction): Boolean = {
    entityExists(s)(action.target) &&
      isAdjacentLocation(s, actor)(action.target(s).location) &&
      (getCreatureStats(s)(actor).melee.stance forall isValidStanceAttack(s, actor))
  }

  private def validate(s: State, actor: CreatureId, action: CancelRecycleItemAction): Boolean = {
    s.creature.recycledItems.get(actor) exists (_.contains(action.item))
  }

  private def validate(s: State, actor: CreatureId, action: ChangeStanceAction): Boolean = {
    isValidStanceChange(s, actor)(action.stance)
  }

  private def validate(s: State, actor: CreatureId, action: CommunicateAction): Boolean = {
    entityExists(s)(action.target) &&
      isAdjacentLocation(s, actor)(action.target(s).location)
  }

  private def validate(s: State, actor: CreatureId, action: ConverseAction): Boolean = {
    entityExists(s)(action.target) &&
      s.creature.conversations.get(actor).exists(x => {
        val (source, communication) = x

        source == action.target && communication(s).validChoices(actor)(s).exists(_.communication == action.subject)
      })
  }

  private def validate(s: State, actor: CreatureId, action: CraftItemAction): Boolean = {
    s.creature.recipes.get(actor) exists (_.contains(action.recipe))
  }

  private def validate(s: State, actor: CreatureId, action: DisplaceAction): Boolean = {
    entityExists(s)(action.target) &&
      isAdjacentLocation(s, actor)(action.target(s).location) &&
      !isEnemy(s, actor, action.target) &&
      !actor(s).traits.immobile &&
      getCreatureStats(s)(actor).speed > 0
  }

  private def validate(s: State, actor: CreatureId, action: DropItemAction): Boolean = {
    entityExists(s)(action.item) &&
      action.item(s).pickable &&
      action.item(s).container == actor
  }

  private def validate(s: State, actor: CreatureId, action: EnterConduitAction): Boolean = {
    actor(s).location == s.conduits.entrances(action.conduit)
  }

  private def validate(s: State, actor: CreatureId, action: HackCreatureAction): Boolean = {
    entityExists(s)(action.target) &&
      action.target(s).locked.isDefined &&
      isAdjacentLocation(s, actor)(action.target(s).location)
  }

  private def validate(s: State, actor: CreatureId, action: HackItemAction): Boolean = {
    entityExists(s)(action.target) &&
      action.target(s).locked.isDefined &&
      (getItemLocation(s)(action.target) exists isAdjacentOrCurrentLocation(s, actor))
  }

  private def validate(s: State, actor: CreatureId, action: MoveAction): Boolean = {
    isAdjacentLocation(s, actor)(action.location) &&
      !actor(s).traits.immobile &&
      getCreatureStats(s)(actor).speed > 0
  }

  private def validate(s: State, actor: CreatureId, action: PickItemAction): Boolean = {
    entityExists(s)(action.item) &&
      action.item(s).pickable &&
      (getItemLocation(s)(action.item) contains actor(s).location)
  }

  private def validate(s: State, actor: CreatureId, action: RecycleItemAction): Boolean = {
    entityExists(s)(action.target) &&
      action.target(s).recyclable.isDefined &&
      (action.target(s).container == actor || (getItemLocation(s)(action.target) contains actor(s).location))
  }

  private def validate(s: State, actor: CreatureId, action: ShootAction): Boolean = {
    getCreatureStats(s)(actor).ranged.stance forall isValidStanceAttack(s, actor)
  }

  private def validate(s: State, actor: CreatureId, action: ShootMissileAction): Boolean = {
    val stats = getCreatureStats(s)(actor)

    (stats.launcher.missiles contains action.missile) &&
      (stats.launcher.stance forall isValidStanceAttack(s, actor))
  }

  private def validate(s: State, actor: CreatureId, action: UnequipItemAction): Boolean = {
    entityExists(s)(action.item) &&
      action.item(s).container == actor &&
      (s.creature.equipments.get(actor) exists (_.values exists (_ == action.item)))
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
