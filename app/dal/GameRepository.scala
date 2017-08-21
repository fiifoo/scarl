package dal

import com.google.inject.ImplementedBy
import models.Game

import scala.concurrent.Future

@ImplementedBy(classOf[GameFilesystemRepository])
trait GameRepository {
  def list(): Future[Seq[Game]]

  def create(name: String): Future[Game]

  def start(id: Long): Future[Option[Game]]

  def save(id: Long, save: String): Unit

  def delete(id: Long): Unit
}
