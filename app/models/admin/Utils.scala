package models.admin

import scala.reflect.runtime.universe.{Symbol, Type}

object Utils {

  def getModelKey(t: Type): String = {
    val base = t.toString
      .replaceAll("""\[.*\]""", "")
      .split('.')
      .filterNot(_ == "type")
      .last

    val args = t.typeArgs map getModelKey

    if (args.isEmpty) {
      base
    } else {
      base + "[" + (args mkString ",") + "]"
    }
  }

  def getProperties(t: Type): List[Symbol] = {
    val accessors = t.members
      .filter(p => p.isMethod && p.asMethod.isCaseAccessor)
      .map(getPropertyName)
      .toSet

    t.members
      .filterNot(_.isMethod)
      .filterNot(getPropertyName(_) == "id")
      .filter(p => accessors.contains(getPropertyName(p)))
      .toList
      .reverse
  }

  def getPropertyName(s: Symbol): String = s.name.toString.trim

  def isSubType(t: Type): Boolean = {
    t.toString.startsWith("io.github.fiifoo.scarl.")
  }
}
