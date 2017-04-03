package models

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.Rng
import io.github.fiifoo.scarl.core.entity.CreatureId
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.game.{Game, OutConnection, OutMessage, Player}
import io.github.fiifoo.scarl.world.{WorldManager, WorldState}
import models.json.FormatId._
import models.json.FormatWorldState._
import models.save.SaveStorage
import play.api.libs.json._

import scala.util.Random

class GameManager(saveStorage: SaveStorage) {

  case class SaveData(areaId: AreaId,
                      playerCreature: CreatureId,
                      world: WorldState,
                      worldHashCode: Int
                     )

  val formatSaveData = Json.format[SaveData]

  private val worldManager = new WorldManager(
    Data.areas,
    Data.factions,
    Data.kinds,
    Data.templates
  )

  def loadOrCreate(send: OutMessage => Unit): Game = {
    val (world, area, creature) = saveStorage.load() map loadWorld getOrElse generateWorld

    val player = new Player(creature)
    val connection = new OutConnection(player, send)

    new Game(connection, player, worldManager, world, area)
  }

  def save(game: Game): Unit = {
    val (world, area, playerCreature) = game.save()

    val data = SaveData(area, playerCreature, world, world.hashCode)
    val json = formatSaveData.writes(data)

    saveStorage.save(json)
  }

  def end(game: Game): Unit = {
    saveStorage.clear()
  }

  private def loadWorld(json: JsValue): (WorldState, AreaId, CreatureId) = {
    val data = formatSaveData.reads(json).get
    val world = GameUtils.finalizeLoadedWorld(data.world, worldManager)

    if (data.worldHashCode != world.hashCode) {
      throw new Exception("Corrupt save data.")
    }

    (world, data.areaId, data.playerCreature)
  }

  private def generateWorld(): (WorldState, AreaId, CreatureId) = {
    val rng = Rng(Random.nextInt())
    val area = AreaId("first")
    val (world, player) = worldManager.create(area, CreatureKindId("hero"), rng)

    (world, area, player)
  }
}
