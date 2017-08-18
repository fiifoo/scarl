package io.github.fiifoo.scarl.simulation

import io.github.fiifoo.scarl.area.shape.Rectangle
import io.github.fiifoo.scarl.area.template.{ApplyTemplate, CalculateTemplate, FixedTemplate, TemplateId}
import io.github.fiifoo.scarl.core.assets.CombatPower
import io.github.fiifoo.scarl.core.creature.{Faction, FactionId}
import io.github.fiifoo.scarl.core.entity.SafeCreatureId
import io.github.fiifoo.scarl.core.kind.{CreatureKind, CreatureKindId, Kinds}
import io.github.fiifoo.scarl.core.mutation.NewEntityMutation
import io.github.fiifoo.scarl.core.{Assets, Location, Rng, State}

object CombatPowerSimulation {
  private val matches = 20
  private val turns = 100

  private val teamSize = 1
  private val teamDistance = 5

  private val homeFaction = FactionId("home")
  private val visitorFaction = FactionId("visitor")
  private val dummyFaction = FactionId("dummy")

  private val simulation = new Simulation[Null](
    listener = NullListener,
    turnLimit = turns
  )

  def apply(combatants: Iterable[CreatureKind], rng: Rng = Rng(1)): CombatPower = {
    simulate(combatants map Combatant.apply, rng)
  }

  def simulate(combatants: Iterable[Combatant], rng: Rng = Rng(1)): CombatPower = {
    val instance = createInstance(combatants, rng)

    val opposed = runAll(instance, combatants)
    val average = opposed mapValues (creatureResults => {
      creatureResults.values.sum / creatureResults.values.size
    })

    CombatPower(average, opposed)
  }

  private def runAll(instance: State, combatants: Iterable[Combatant]): CombatPower.Opposed = {
    type Data = (CombatPower.Opposed, Iterable[Combatant])

    val (result, _) = combatants.foldLeft[Data](Map(), combatants)((carry, combatant) => {
      val (result, combatants) = carry
      val opponents = combatants.tail

      val nextResult = opponents.foldLeft(result)((result, opponent) => {
        val scores = run(instance, combatant, opponent)

        addResult(result, combatant.id, opponent.id, scores)
      })

      (nextResult, opponents)
    })

    result
  }

  private def run(emptyInstance: State, combatant: Combatant, opponent: Combatant): (Int, Int) = {
    val (instance, home, visitor) = addTeams(emptyInstance, combatant(homeFaction), opponent(visitorFaction))
    val state = createState(instance)

    val (homeScore, visitorScore, _) = (0 until matches).foldLeft((0.0, 0.0, state))((carry, _) => {
      val (homeScore, visitorScore, state) = carry
      val resultState = simulation(state)

      (
        homeScore + calculateScore(resultState.instance, visitor),
        visitorScore + calculateScore(resultState.instance, home),
        state.copy(instance = state.instance.copy(rng = resultState.instance.rng))
      )
    })

    (normalizeScore(homeScore), normalizeScore(visitorScore))
  }

  private def calculateScore(instance: State, opponents: Set[SafeCreatureId]): Double = {
    opponents.foldLeft(0.0)((score, opponent) => {
      score + (opponent(instance) map (opponent => {
        (opponent.damage: Double) / opponent.stats.health
      }) getOrElse 1.0)
    })
  }

  private def normalizeScore(score: Double): Int = {
    (score / teamSize / matches * 100).round.toInt
  }

  private def createInstance(combatants: Iterable[Combatant], rng: Rng): State = {
    val (random, _) = rng()

    val kinds = Kinds(
      creatures = (combatants map (c => {
        val kind = c(dummyFaction) // Kind faction hopefully doesn't matter...

        (kind.id, kind)
      })).toMap
    )

    val factions = List(
      Faction(
        id = homeFaction,
        enemies = Set(visitorFaction)
      ),
      Faction(
        id = visitorFaction,
        enemies = Set(homeFaction)
      )
    )

    val template = FixedTemplate(
      TemplateId("arena"),
      shape = Rectangle(teamDistance, teamSize, 0)
    )

    val templateResult = CalculateTemplate(template, Map(template.id -> template), random)

    val assets = Assets(
      factions = factions.map(f => (f.id, f)).toMap,
      kinds = kinds,
    )
    val instance = State(
      assets = assets,
      rng = rng
    )

    ApplyTemplate(instance, templateResult, List(), List(), random)
  }

  private def createState(instance: State): SimulationState[Null] = {
    SimulationState(instance, null)
  }

  private def addTeams(instance: State,
                       combatant: CreatureKind,
                       opponent: CreatureKind
                      ): (State, Set[SafeCreatureId], Set[SafeCreatureId]) = {
    val (nextInstance, home) = addTeam(instance, combatant, isHome = true)
    val (instanceResult, visitor) = addTeam(nextInstance, opponent, isHome = false)

    (instanceResult, home, visitor)
  }

  private def addTeam(instance: State, kind: CreatureKind, isHome: Boolean): (State, Set[SafeCreatureId]) = {
    val x = if (isHome) 0 else teamDistance

    (0 until teamSize).foldLeft((instance, Set[SafeCreatureId]()))((carry, y) => {
      val (instance, creatures) = carry
      val creature = kind(instance, Location(x, y))

      (NewEntityMutation(creature)(instance), creatures + SafeCreatureId(creature.id))
    })
  }

  private def addResult(result: CombatPower.Opposed,
                        combatant: CreatureKindId,
                        opponent: CreatureKindId,
                        scores: (Int, Int)
                       ) = {
    val combatantResults = result.getOrElse(combatant, Map())
    val opponentResults = result.getOrElse(opponent, Map())

    result +
      (combatant -> (combatantResults + (opponent -> scores._1))) +
      (opponent -> (opponentResults + (combatant -> scores._2)))
  }
}
