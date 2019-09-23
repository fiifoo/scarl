package dal

import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import java.sql.Timestamp
import javax.inject.Inject

import models.Game
import play.api.libs.json.Json

import scala.concurrent.{ExecutionContext, Future}

class GameFilesystemRepository @Inject()(implicit ec: ExecutionContext) extends GameRepository {
  private val directory = "user/saves"
  private val root = Paths.get(directory)

  def list(): Future[Seq[Game]] = {

    if (Files.notExists(root)) {
      Files.createDirectory(root)
    }

    var games: Seq[Game] = Seq()
    Files.list(root) forEach (readGame(_) foreach (game => games = games :+ game))

    Future(games)
  }

  def create(name: String): Future[Game] = {
    val now = timestamp
    val game = Game(now.getTime, name, None, running = true, now, now)
    writeGame(game)

    Future(game)
  }

  def start(id: Long): Future[Option[Game]] = {
    val game = readGame(id) filterNot (_.running) map (_.copy(running = true, lastPlayedAt = timestamp))
    game foreach writeGame

    Future(game)
  }

  def stop(id: Long): Unit = {
    readGame(id) map (_.copy(running = false)) foreach writeGame
  }

  def save(id: Long, save: String): Unit = {
    readGame(id) map (_.copy(save = Some(save), running = false, lastPlayedAt = timestamp)) foreach writeGame
  }

  def delete(id: Long): Unit = {
    Files.deleteIfExists(getFile(id))
  }

  private def getFile(id: Long): Path = {
    Paths.get(directory, id.toString)
  }

  private def readGame(id: Long): Option[Game] = {
    readGame(getFile(id))
  }

  private def readGame(file: Path): Option[Game] = {
    if (Files.exists(file)) {
      val data = new String(Files.readAllBytes(file), UTF_8)
      val json = Json.parse(data)

      Some(Game.format.reads(json).get)
    } else {
      None
    }
  }

  private def writeGame(game: Game): Unit = {
    val json = Game.format.writes(game)

    Files.write(
      getFile(game.id),
      json.toString.getBytes(UTF_8),
      StandardOpenOption.CREATE,
      StandardOpenOption.TRUNCATE_EXISTING
    )
  }

  private def timestamp = new Timestamp(System.currentTimeMillis())
}
