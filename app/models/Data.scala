package models

import io.github.fiifoo.scarl.area.template.{Template, TemplateId}
import io.github.fiifoo.scarl.area.theme.{Theme, ThemeId}
import io.github.fiifoo.scarl.area.{Area, AreaId}
import io.github.fiifoo.scarl.core.communication.{Communication, CommunicationId}
import io.github.fiifoo.scarl.core.creature.{Faction, FactionId, Progression, ProgressionId}
import io.github.fiifoo.scarl.core.item.Recipe.RecipeId
import io.github.fiifoo.scarl.core.item.{KeyKind, KeyKindId, Recipe}
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.world.GoalId
import io.github.fiifoo.scarl.world._
import io.github.fiifoo.scarl.world.system.source.{SpaceshipSource, SpaceshipSourceId, StellarBodySource, StellarBodySourceId}
import models.json._
import play.api.libs.json._

case class Data(areas: Map[AreaId, Area],
                catalogues: WorldCatalogues,
                communications: Map[CommunicationId, Communication],
                factions: Map[FactionId, Faction],
                goals: Map[GoalId, Goal],
                keys: Map[KeyKindId, KeyKind],
                kinds: Kinds,
                progressions: Map[ProgressionId, Progression],
                recipes: Map[RecipeId, Recipe] = Map(),
                regions: Map[RegionId, Region] = Map(),
                sites: Map[SiteId, Site],
                spaceships: Map[SpaceshipSourceId, SpaceshipSource] = Map(),
                stellarBodies: Map[StellarBodySourceId, StellarBodySource] = Map(),
                templates: Map[TemplateId, Template],
                themes: Map[ThemeId, Theme],
                transports: Map[TransportId, Transport],
                worlds: Map[WorldId, World],
               )

object Data {
  lazy private implicit val areaMapReads = JsonArea.areaMapReads
  lazy private implicit val worldCataloguesReads = JsonWorldCatalogues.worldCataloguesReads
  lazy private implicit val communicationMapReads = JsonCommunication.communicationMapReads
  lazy private implicit val factionMapReads = JsonFaction.factionMapReads
  lazy private implicit val goalMapReads = JsonGoal.goalMapReads
  lazy private implicit val keyKindMapReads = JsonItemKey.keyKindMapReads
  lazy private implicit val kindsReads = JsonKind.kindsReads
  lazy private implicit val progressionMapReads = JsonProgression.progressionMapReads
  lazy private implicit val recipeMapReads = JsonRecipe.recipeMapReads
  lazy private implicit val regionMapReads = JsonRegion.regionMapReads
  lazy private implicit val sitesMapReads = JsonSite.siteMapReads
  lazy private implicit val spaceshipMapReads = JsonSpaceshipSource.spaceshipMapReads
  lazy private implicit val stellarBodyMapReads = JsonStellarBodySource.stellarBodyMapReads
  lazy private implicit val templateMapReads = JsonTemplate.templateMapReads
  lazy private implicit val themeMapReads = JsonTheme.themeMapReads
  lazy private implicit val transportMapReads = JsonTransport.transportMapReads
  lazy private implicit val worldMapReads = JsonWorld.worldMapReads

  lazy val dataReads: Reads[Data] = Json.reads[Data]
}
