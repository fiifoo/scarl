package io.github.fiifoo.scarl.game

import io.github.fiifoo.scarl.game.api.AreaChange
import io.github.fiifoo.scarl.world._

sealed trait WorldAction {
  def apply(state: RunState): Option[RunState]
}

object WorldAction {

  case class Disembark(to: SiteId) extends WorldAction {

    def apply(state: RunState): Option[RunState] = {
      state.game.world.assets.sites.get(this.to) flatMap (to => {
        Utils.getActorTransport(state) filter (valid(state, to) _).tupled map (disembark(state, to) _).tupled
      })
    }

    private def valid(state: RunState, to: Site)(transport: Transport, region: Region): Boolean = {
      region.entrances.get(transport.category) exists (_.contains(to.id))
    }

    private def disembark(state: RunState, to: Site)(transport: Transport, region: Region): RunState = {
      Utils.changeArea(to.id)(state)
    }
  }

  case class Embark(transport: TransportId) extends WorldAction {

    def apply(state: RunState): Option[RunState] = {
      Utils.getTransport(state, this.transport) filter (valid(state) _).tupled map (embark(state) _).tupled
    }

    private def valid(state: RunState)(transport: Transport, region: Region): Boolean = {
      val from = state.game.area

      region.exits.get(transport.category) exists (_.contains(from))
    }

    private def embark(state: RunState)(transport: Transport, region: Region): RunState = {
      Utils.changeArea(transport.hub)(state)
    }
  }

  case class Travel(to: RegionId) extends WorldAction {

    def apply(state: RunState): Option[RunState] = {
      state.game.world.assets.regions.get(this.to) flatMap (to => {
        Utils.getActorTransport(state) map (travel(state, to) _).tupled
      })
    }

    private def travel(state: RunState, to: Region)(transport: Transport, from: Region): RunState = {
      state.copy(game = state.game.copy(world = state.game.world.copy(
        transports = state.game.world.transports + (transport.id -> to.id)
      )))
    }
  }

  private object Utils {

    def getTransport(state: RunState, id: TransportId): Option[(Transport, Region)] = {
      val worldState = state.game.world
      val assets = worldState.assets

      assets.transports.get(id) flatMap (transport => {
        worldState.transports.get(transport.id) map (transport -> assets.regions(_))
      })
    }

    def getActorTransport(state: RunState): Option[(Transport, Region)] = {
      val worldState = state.game.world
      val assets = worldState.assets

      assets.transports.values find (_.hub == state.game.area) flatMap (transport => {
        worldState.transports.get(transport.id) map (transport -> assets.regions(_))
      })
    }

    def changeArea(destination: SiteId)(state: RunState): RunState = {
      val change =
        ChangeArea(destination) _ andThen
          (state => state.copy(outMessages = AreaChange(state) :: state.outMessages)) andThen
          (state => RunGame(state))

      change(state)
    }
  }

}
