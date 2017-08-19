package game

import game.json.FormatGameState._
import game.save.SaveStorage
import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.Rng
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.game._
import io.github.fiifoo.scarl.world.WorldManager
import play.api.libs.json._

import scala.util.Random

class GameManager(saveStorage: SaveStorage) {

  case class SaveData(state: GameState,
                      checkHashCode: Int
                     )

  val formatSaveData = Json.format[SaveData]

  private val worldManager = new WorldManager(
    Data.areas,
    Simulations.combatPower(),
    Data.communications,
    Data.factions,
    Data.kinds,
    Data.powers,
    Data.progressions,
    Data.templates
  )

  def loadOrCreate(): (Game, RunState) = {
    val state = saveStorage.load() map loadGame getOrElse generateGame

    Game(state, worldManager)
  }

  def save(game: Game, state: RunState): Unit = {
    val gameState = game.save(state)

    val data = SaveData(gameState, gameState.hashCode)
    val json = formatSaveData.writes(data)

    saveStorage.save(json)
  }

  def end(): Unit = {
    saveStorage.clear()
  }

  private def loadGame(json: JsValue): GameState = {
    val data = formatSaveData.reads(json).get
    val world = GameUtils.finalizeLoadedWorld(data.state.world, worldManager)

    val gameState = GameState(
      area = data.state.area,
      player = data.state.player,
      world = world,
      maps = data.state.maps,
      statistics = data.state.statistics
    )

    if (data.checkHashCode != gameState.hashCode) {
      throw new Exception("Corrupt save data.")
    }

    gameState
  }

  private def generateGame(): GameState = {
    val rng = Rng(Random.nextInt())
    val area = AreaId("first")
    val (world, player) = worldManager.create(area, CreatureKindId("hero"), rng)

    GameState(area, player, world)
  }
}
