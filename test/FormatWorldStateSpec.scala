import game.json.FormatWorldState
import game.{Data, LoadGame}
import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.world.{CreateWorld, WorldState}
import org.scalatestplus.play._

class FormatWorldStateSpec extends PlaySpec {
  val assets = Data()
  val world = createWorld()

  "FormatWorldState" must {
    "read and write WorldState" in {
      val json = FormatWorldState.formatWorldState.writes(world)
      val loaded = FormatWorldState.formatWorldState.reads(json).get
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
