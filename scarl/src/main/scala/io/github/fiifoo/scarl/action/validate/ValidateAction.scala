package io.github.fiifoo.scarl.action.validate

import io.github.fiifoo.scarl.action._
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.action.Action
import io.github.fiifoo.scarl.core.entity.CreatureId

object ValidateAction {

  implicit val attack = AttackValidator
  implicit val communicate = CommunicateValidator
  implicit val enterConduit = EnterConduitValidator
  implicit val equipItem = EquipItemValidator
  implicit val move = MoveValidator
  implicit val pass = PassValidator
  implicit val pickItem = PickItemValidator
  implicit val shoot = new NullValidator[ShootAction]

  def apply(s: State, actor: CreatureId, action: Action): Boolean = {
    action match {
      case action: AttackAction => validate(s, actor, action)
      case action: CommunicateAction => validate(s, actor, action)
      case action: EnterConduitAction => validate(s, actor, action)
      case action: EquipItemAction => validate(s, actor, action)
      case action: MoveAction => validate(s, actor, action)
      case action: PassAction => validate(s, actor, action)
      case action: PickItemAction => validate(s, actor, action)
      case action: ShootAction => validate(s, actor, action)
      case _ => false
    }
  }

  private def validate[A <: Action](s: State,
                                    actor: CreatureId,
                                    action: A
                                   )(implicit validator: ActionValidator[A]): Boolean = {
    validator(s, actor, action)
  }
}
