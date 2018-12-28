import dal.{AssetsRepository, SimulationsRepository}
import game.LoadGame
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.world.{CreateWorld, SiteId, WorldState}
import models.json.JsonWorldState
import org.scalatestplus.play._
import play.api.inject.guice.GuiceApplicationBuilder

class JsonWorldStateSpec extends PlaySpec {
  val environment = new GuiceApplicationBuilder().build().environment
  val simulationsRepository = new SimulationsRepository(environment)
  val assets = new AssetsRepository(environment, simulationsRepository).build()
  val world = createWorld()

  "JsonWorldState" must {
    "read and write WorldState" in {
      val json = JsonWorldState.worldStateFormat.writes(world)
      val loaded = JsonWorldState.worldStateFormat.reads(json).get
      val result = LoadGame.finalize(assets, loaded)

      world.hashCode mustBe result.hashCode
    }
  }

  private def createWorld(): WorldState = {
    val area = SiteId("start-level")

    val world = assets.worlds.values.head
    val character = world.characters.head

    val (worldState, _) = CreateWorld(assets, world, character)
    val instanceState = worldState.states(area)

    worldState.copy(
      states = worldState.states + (area -> instanceState.copy(
        tmp = State.Temporary()
      ))
    )
  }
}
