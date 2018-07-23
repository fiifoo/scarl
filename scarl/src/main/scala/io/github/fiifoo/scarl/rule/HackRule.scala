package io.github.fiifoo.scarl.rule

import scala.util.Random

object HackRule {

  sealed trait Result

  sealed trait FailureResult extends Result

  case object Success extends Result

  case object Failure extends FailureResult

  case object CertainFailure extends FailureResult

  case object BadFailure extends FailureResult

  case object CriticalFailure extends FailureResult

  private val bad = 0.75
  private val critical = 0.25

  private val securityIncrease = 1.5

  def apply(random: Random)(skill: Int, security: Int): Result = {
    if (skill <= 0 || security <= 0) {
      CertainFailure
    } else {
      random.nextInt(skill) - random.nextInt(security) match {
        case 0 => if (random.nextBoolean()) Success else Failure
        case x if x > 0 => Success
        case x if x < 0 => if (skill + x < skill * critical) {
          CriticalFailure
        } else if (skill + x < skill * bad) {
          BadFailure
        } else {
          Failure
        }
      }
    }
  }

  def shouldTriggerTrap(failure: FailureResult): Boolean = {
    failure match {
      case CriticalFailure => true
      case _ => false
    }
  }

  def increaseSecurity(failure: FailureResult)(security: Int): Option[Int] = {
    failure match {
      case Failure | CertainFailure => None
      case _ => Some((security * securityIncrease).toInt)
    }
  }
}
