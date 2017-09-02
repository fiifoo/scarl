import game.LoadGame
import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.world.{CreateWorld, WorldState}
import models.Data
import models.json.JsonWorldState
import org.scalatestplus.play._

class JsonWorldStateSpec extends PlaySpec {
  val assets = Data.build()
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
    val area = AreaId("first")

    val (world, _) = CreateWorld(assets, area, CreatureKindId("hero"))
    val state = world.states(area)

    world.copy(
      states = world.states + (area -> state.copy(
        tmp = State.Temporary()
      ))
    )
  }
}
