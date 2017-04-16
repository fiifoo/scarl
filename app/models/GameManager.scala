package models

import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.Rng
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.game._
import io.github.fiifoo.scarl.world.WorldManager
import models.json.FormatGameState._
import models.save.SaveStorage
import play.api.libs.json._

import scala.util.Random

class GameManager(saveStorage: SaveStorage) {

  case class SaveData(state: GameState,
                      checkHashCode: Int
                     )

  val formatSaveData = Json.format[SaveData]

  private val worldManager = new WorldManager(
    Data.areas,
    Data.communications,
    Data.factions,
    Data.kinds,
    Data.progressions,
    Data.templates
  )

  def loadOrCreate(out: OutMessage => Unit): Game = {
    val state = saveStorage.load() map loadGame getOrElse generateGame

    new Game(state, worldManager, out)
  }

  def save(game: Game): Unit = {
    val state = game.save()

    val data = SaveData(state, state.hashCode)
    val json = formatSaveData.writes(data)

    saveStorage.save(json)
  }

  def end(game: Game): Unit = {
    saveStorage.clear()
  }

  private def loadGame(json: JsValue): GameState = {
    val data = formatSaveData.reads(json).get
    val world = GameUtils.finalizeLoadedWorld(data.state.world, worldManager)

    val state = GameState(
      data.state.area,
      data.state.player,
      world,
      data.state.maps,
      data.state.statistics
    )

    if (data.checkHashCode != state.hashCode) {
      throw new Exception("Corrupt save data.")
    }

    state
  }

  private def generateGame(): GameState = {
    val rng = Rng(Random.nextInt())
    val area = AreaId("first")
    val (world, player) = worldManager.create(area, CreatureKindId("hero"), rng)

    GameState(area, player, world)
  }
}
