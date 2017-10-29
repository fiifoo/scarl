import dal.AssetsRepository
import game.LoadGame
import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.world.{CreateWorld, WorldState}
import models.json.JsonWorldState
import org.scalatestplus.play._
import play.api.inject.guice.GuiceApplicationBuilder

class JsonWorldStateSpec extends PlaySpec {
  val environment = new GuiceApplicationBuilder().build().environment
  val assets = new AssetsRepository(environment).build()
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
    val area = AreaId("start-level")

    val (world, _) = CreateWorld(assets, area, CreatureKindId("player"))
    val state = world.states(area)

    world.copy(
      states = world.states + (area -> state.copy(
        tmp = State.Temporary()
      ))
    )
  }
}
