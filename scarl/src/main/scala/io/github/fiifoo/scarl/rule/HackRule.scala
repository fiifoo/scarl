package io.github.fiifoo.scarl.rule

import io.github.fiifoo.scarl.core.item.Lock

import scala.util.Random

object HackRule {

  sealed trait Result

  sealed trait FailureResult extends Result

  case object Success extends Result

  case object Failure extends FailureResult

  case object BadFailure extends FailureResult

  case object CriticalFailure extends FailureResult

  private val bad = 0.75
  private val critical = 0.25

  private val lockFailure = 1.5

  def apply(random: Random)(skill: Int, security: Int): Result = {
    if (skill <= 0 || security <= 0) {
      Failure
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

  def failureTrap(failure: FailureResult): Boolean = {
    failure match {
      case CriticalFailure => true
      case _ => false
    }
  }

  def failureLock(failure: FailureResult)(lock: Lock): Option[Lock] = {
    failure match {
      case Failure => None
      case _ => Some(lock.copy(
        security = (lock.security * lockFailure).toInt
      ))
    }
  }
}
