package dal

import java.sql.Timestamp
import javax.inject.{Inject, Singleton}

import models.Game
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class GameDatabaseRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)
                                      (implicit ec: ExecutionContext) extends GameRepository {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class GameTable(tag: Tag) extends Table[Game](tag, "game") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def player = column[String]("player")

    def save = column[Option[String]]("save")

    def running = column[Boolean]("running")

    def createdAt = column[Timestamp]("createdAt")

    def lastPlayedAt = column[Timestamp]("lastPlayedAt")

    def * = (id, player, save, running, createdAt, lastPlayedAt) <> ((Game.apply _).tupled, Game.unapply)
  }

  private val games = TableQuery[GameTable]

  def list(): Future[Seq[Game]] = db.run {
    games
      .sortBy(_.id.asc)
      .result
  }

  def create(name: String): Future[Game] = {
    val now = timestamp

    db.run {
      (games.map(game => (game.player, game.save, game.running, game.createdAt, game.lastPlayedAt))
        returning games.map(_.id)
        into ((game, id) => Game(id, game._1, game._2, game._3, game._4, game._5))
        ) += (name, None, true, now, now)
    }
  }

  def start(id: Long): Future[Option[Game]] = {
    db.run {
      games
        .filter(game => game.id === id && game.running === false)
        .map(game => (game.running, game.lastPlayedAt))
        .update((true, timestamp))
        .map {
          case 0 => false
          case _ => true
        }
    } flatMap (success => {
      if (success) {
        db.run {
          games
            .filter(game => game.id === id)
            .result
            .headOption
        }
      } else {
        Future(None)
      }
    })
  }

  def save(id: Long, save: String): Unit = db.run {
    games
      .filter(_.id === id)
      .map(game => (game.save, game.running, game.lastPlayedAt))
      .update((Some(save), false, timestamp))
  }

  def delete(id: Long): Unit = db.run {
    games
      .filter(_.id === id)
      .delete
  }

  private def timestamp = new Timestamp(System.currentTimeMillis())
}
