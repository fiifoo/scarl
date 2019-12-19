package io.github.fiifoo.scarl.area.template

import io.github.fiifoo.scarl.area.shape.Rectangle
import io.github.fiifoo.scarl.area.theme.{Theme, ThemeId}
import io.github.fiifoo.scarl.area.{Area, AreaId}
import io.github.fiifoo.scarl.core.assets._
import io.github.fiifoo.scarl.core.geometry.Location
import io.github.fiifoo.scarl.core.kind.{TerrainKind, TerrainKindId}
import io.github.fiifoo.scarl.core.math.Distribution
import io.github.fiifoo.scarl.core.math.Rng.WeightedChoice
import io.github.fiifoo.scarl.world.{TemplateCatalogueId, WorldAssets, WorldCatalogues}
import org.scalatest._

import scala.util.Random

class CalculateTemplateSpec extends FlatSpec with Matchers {

  private val theme = Theme(
    ThemeId(""),
    CreatureCatalogueId(""),
    ItemCatalogueId(""),
    TemplateCatalogueId(""),
    TerrainCatalogueId(""),
    WallCatalogueId(""),
    WidgetCatalogueId(""),
  )

  private val terrainCatalogue = TerrainCatalogue(TerrainCatalogueId(""), content = Map(
    TerrainKind.DefaultCategory -> List(WeightedChoice(TerrainKindId(""), 1))
  ))

  "CalculateTemplate" should "calculate template tree" in {
    val t1 = RandomizedTemplate(
      id = TemplateId("main"),
      shape = Rectangle(80, 25, 0),
      templates = List(
        ContentSource.TemplateSource(
          ContentSelection.FixedTemplate(TemplateId("fixed-sub")),
          Distribution.Uniform(1, 1)
        ),
        ContentSource.TemplateSource(
          ContentSelection.FixedTemplate(TemplateId("random-sub-1")),
          Distribution.Uniform(1, 1)
        )
      )
    )
    val t2 = FixedTemplate(
      id = TemplateId("fixed-sub"),
      shape = Rectangle(20, 10, 0),
      templates = Map(
        Location(1, 1) -> TemplateId("random-sub-1"),
        Location(10, 1) -> TemplateId("random-sub-2")
      )
    )
    val t3 = RandomizedTemplate(
      id = TemplateId("random-sub-1"),
      shape = Rectangle(3, 3, 0)
    )
    val t4 = RandomizedTemplate(
      id = TemplateId("random-sub-2"),
      shape = Rectangle(3, 3, 0.5)
    )

    val assets = WorldAssets(
      catalogues = WorldCatalogues(terrains = Map(terrainCatalogue.id -> terrainCatalogue)),
      templates = Map(t1.id -> t1, t2.id -> t2, t3.id -> t3, t4.id -> t4),
      themes = Map(theme.id -> theme)
    )

    val area = Area(
      AreaId(""),
      t1.id,
      theme.id
    )

    t1(assets, Template.Context(area), new Random(1))
  }
}
