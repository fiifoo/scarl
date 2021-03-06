package io.github.fiifoo.scarl.world.system.source

import io.github.fiifoo.scarl.core.Color
import io.github.fiifoo.scarl.world.system.Physics
import io.github.fiifoo.scarl.world.system.source.StellarBodySource.Category

object StellarBodySource {

  sealed trait Category {
    def mass(mass: Double): Double
  }

  case object BlackHoleCategory extends Category {
    def mass(mass: Double): Double = mass * Physics.SolarMass
  }

  case object PlanetCategory extends Category {
    def mass(mass: Double): Double = mass * Physics.EarthMass
  }

  case object SunCategory extends Category {
    def mass(mass: Double): Double = mass * Physics.SolarMass
  }

}

case class StellarBodySource(id: StellarBodySourceId,
                             category: Category,
                             name: String,
                             description: Option[String],
                             color: Color,
                             mass: Double,
                             orbiters: List[SolarSystemSource.Orbiter] = List(),
                            )
