package io.github.fiifoo.scarl.world.system

object Physics {

  val g: Double = 9.80665

  val G: Double = 6.67408 * math.pow(10, -11)

  val AU: Double = 149597871000.0

  val EarthMass: Double = 5.97237 * math.pow(10, 24)
  val SolarMass: Double = 1.98847 * math.pow(10, 30)

  def hypotenuse(a: Double, b: Double): Double = math.sqrt(math.pow(a, 2) + math.pow(b, 2))

  def vector(slope: Double, length: Double): Vector = {
    val x = length / math.sqrt(1 + math.pow(slope, 2))
    val y = slope * x

    Vector(x, y)
  }

  def travelTime(distance: Double, acceleration: Double): Int = math.sqrt(2 * distance / acceleration).toInt

  def gravity(bodies: Iterable[StellarBody], body: StellarBody): Vector = {
    bodies filter (_.id != body.id) map calculateGravity(body) reduceLeft (_ add _)
  }

  private def calculateGravity(body1: StellarBody)(body2: StellarBody): Vector = {
    val quarter = Coordinates.Quarter(body1.position, body2.position)
    val p1 = quarter.normalize(body1.position)
    val p2 = quarter.normalize(body2.position)

    val m1 = body1.mass
    val m2 = body2.mass

    val dx = p2.x - p1.x
    val dy = p2.y - p1.y
    val r = hypotenuse(dx, dy)

    val F = G * m1 * m2 / math.pow(r, 2)
    val a = F / m1

    quarter.normalize(vector(dy / dx, a))
  }
}
