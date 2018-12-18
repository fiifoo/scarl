package io.github.fiifoo.scarl.simulation

import io.github.fiifoo.scarl.area.shape.Rectangle
import io.github.fiifoo.scarl.area.template.{ApplyTemplate, CalculateTemplate, FixedTemplate, TemplateId}
import io.github.fiifoo.scarl.area.theme.{Theme, ThemeId}
import io.github.fiifoo.scarl.area.{Area, AreaId}
import io.github.fiifoo.scarl.core.State
import io.github.fiifoo.scarl.core.assets._
import io.github.fiifoo.scarl.core.creature.{Faction, FactionId}
import io.github.fiifoo.scarl.core.entity.{ActorQueue, Creature, SafeCreatureId}
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind._
import io.github.fiifoo.scarl.core.math.Rng
import io.github.fiifoo.scarl.core.math.Rng.WeightedChoice
import io.github.fiifoo.scarl.core.mutation.ActorTickMutation
import io.github.fiifoo.scarl.world._

case class CombatPowerSimulation(matches: Int = 25,
                                 turns: Int = 50,
                                 teamSize: Int = 1,
                                 teamDistance: Int = 5
                                ) {

  private val homeFaction = FactionId("home")
  private val visitorFaction = FactionId("visitor")
  private val dummyFaction = FactionId("dummy")
  private val terrain = TerrainKind(TerrainKindId(""), "", '.', "")
  private val terrainCatalogue = TerrainCatalogue(TerrainCatalogueId(""), content = Map(
    TerrainKind.DefaultCategory -> List(WeightedChoice(terrain.id, 1))
  ))

  private val theme = Theme(
    ThemeId(""),
    CreatureCatalogueId(""),
    ItemCatalogueId(""),
    TemplateCatalogueId(""),
    TerrainCatalogueId(""),
    WallCatalogueId(""),
    WidgetCatalogueId(""),
  )

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
    val initial: (CombatPower.Opposed, Iterable[Combatant]) = (Map(), combatants)

    val (result, _) = (combatants foldLeft initial) ((carry, combatant) => {
      val (result, combatants) = carry
      val opponents = combatants.tail

      val nextResult = (opponents foldLeft result) ((result, opponent) => {
        val scores = run(instance, combatant, opponent)

        addResult(result, combatant.id, opponent.id, scores)
      })

      (nextResult, opponents)
    })

    result
  }

  private def run(emptyInstance: State, combatant: Combatant, opponent: Combatant): (Int, Int) = {
    val (instance, home, visitor) = addTeams(emptyInstance, combatant(homeFaction), opponent(visitorFaction))
    val initialState = createState(instance)

    val (homeScore, visitorScore, _) = ((0 until matches) foldLeft(0.0, 0.0, initialState)) ((carry, _) => {
      val (homeScore, visitorScore, state) = carry
      val resultState = simulation(state)
      val nextState = initialState.copy(
        instance = randomizeInstanceActorOrder(initialState.instance.copy(rng = resultState.instance.rng))
      )

      (
        homeScore + calculateScore(resultState.instance, visitor),
        visitorScore + calculateScore(resultState.instance, home),
        nextState
      )
    })

    (normalizeScore(homeScore), normalizeScore(visitorScore))
  }

  private def randomizeInstanceActorOrder(state: State): State = {
    val (random, rng) = state.rng()

    val creatures = state.entities.values collect {
      case creature: Creature => creature
    }
    val winners = random.shuffle(creatures).take(creatures.size / 2)
    val next = (winners foldLeft state) ((state, creature) => {
      ActorTickMutation(creature.id, creature.tick + 1)(state)
    })

    next.copy(
      rng = rng,
      cache = next.cache.copy(
        actorQueue = ActorQueue(next)
      )
    )
  }

  private def calculateScore(instance: State, opponents: Set[SafeCreatureId]): Double = {
    (opponents foldLeft 0.0) ((score, opponent) => {
      score + (opponent(instance) map (opponent => {
        opponent.damage / opponent.stats.health.max
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
      })).toMap,
      items = (combatants flatMap (_.equipments.values) map (item => item.id -> item)).toMap,
      terrains = Map(terrain.id -> terrain)
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

    val area = Area(
      AreaId(""),
      RegionId(""),
      template.id,
      theme.id
    )

    val assets = WorldAssets(
      areas = Map(area.id -> area),
      factions = factions.map(f => (f.id, f)).toMap,
      catalogues = WorldCatalogues(terrains = Map(terrainCatalogue.id -> terrainCatalogue)),
      kinds = kinds,
      templates = Map(template.id -> template),
      themes = Map(theme.id -> theme)
    )

    val templateResult = CalculateTemplate(assets, area, random)(template)

    val instance = State(
      assets = assets.instance(),
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

    ((0 until teamSize) foldLeft(instance, Set[SafeCreatureId]())) ((carry, y) => {
      val (instance, creatures) = carry
      val result = kind.apply(instance, instance.idSeq, Location(x, y))

      (result.write(instance), creatures + SafeCreatureId(result.entity.id))
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
