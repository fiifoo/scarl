import io.github.fiifoo.scarl.area.AreaId
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.kind.CreatureKindId
import io.github.fiifoo.scarl.world.{WorldManager, WorldState}
import models.json.FormatWorldState
import models.{Data, GameUtils}
import org.scalatestplus.play._

class FormatWorldStateSpec extends PlaySpec {

  val worldManager = new WorldManager(
    Data.areas,
    Data.factions,
    Data.kinds,
    Data.progressions,
    Data.templates
  )

  val world = createWorld()

  "FormatWorldState" must {
    "read and write WorldState" in {
      val json = FormatWorldState.formatWorldState.writes(world)
      val loaded = FormatWorldState.formatWorldState.reads(json).get
      val result = GameUtils.finalizeLoadedWorld(loaded, worldManager)

      world.hashCode mustBe result.hashCode
    }
  }

  private def createWorld(): WorldState = {
    val area = AreaId("first")

    val (world, _) = worldManager.create(area, CreatureKindId("hero"))
    val state = world.states(area)

    world.copy(
      states = world.states + (area -> state.copy(
        stored = State.Stored(actors = state.tmp.addedActors),
        tmp = State.Temporary()
      ))
    )
  }
}
