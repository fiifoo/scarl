package models

import io.github.fiifoo.scarl.area.template.{Template, TemplateId}
import io.github.fiifoo.scarl.area.{Area, AreaId}
import io.github.fiifoo.scarl.core.communication.{Communication, CommunicationId}
import io.github.fiifoo.scarl.core.creature.{Faction, FactionId, Progression, ProgressionId}
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.power.Powers
import models.json._
import play.api.libs.json._

case class Data(areas: Map[AreaId, Area],
                communications: Map[CommunicationId, Communication],
                factions: Map[FactionId, Faction],
                kinds: Kinds,
                powers: Powers,
                progressions: Map[ProgressionId, Progression],
                templates: Map[TemplateId, Template]
               )

object Data {
  lazy private implicit val areaMapReads = JsonArea.areaMapReads
  lazy private implicit val communicationMapReads = JsonCommunication.communicationMapReads
  lazy private implicit val factionMapReads = JsonFaction.factionMapReads
  lazy private implicit val kindsReads = JsonKind.kindsReads
  lazy private implicit val powersReads = JsonPowers.powersReads
  lazy private implicit val progressionMapReads = JsonProgression.progressionMapReads
  lazy private implicit val templateMapReads = JsonTemplate.templateMapReads

  lazy val dataReads: Reads[Data] = Json.reads[Data]
}
