package physics

import mathematics.Vector
import mathematics.times

var path: List<Vector> = listOf()

fun generatePath(
    startVector: Vector,
    timestep: Double,
    steps: Int,
    chargeToMass: Double,
    magneticFieldLaw: (Vector) -> Vector
) {
    val res = mutableListOf(startVector)

    var coord = startVector
    var velocity = Vector(0, 0, 0)

    repeat(steps) {
        val acceleration = chargeToMass * velocity x magneticFieldLaw(coord)
        coord += velocity * timestep
        velocity += acceleration * timestep
        res.add(coord)
    }

    path = res
}