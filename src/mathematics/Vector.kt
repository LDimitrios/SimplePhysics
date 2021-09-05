@file:Suppress("NOTHING_TO_INLINE")

package mathematics

import kotlin.math.*

data class Vector(val x: Double, val y: Double, val z: Double) {
    constructor(x: Number, y: Number, z: Number) : this(x.toDouble(), y.toDouble(), z.toDouble())

    operator fun unaryPlus() = this

    val negated get() = Vector(-x, -y, -z)
    inline operator fun unaryMinus() = negated

    inline operator fun plus(other: Vector) = Vector(x + other.x, y + other.y, z + other.z)
    inline operator fun minus(other: Vector) = Vector(x - other.x, y - other.y, z - other.z)

    inline operator fun times(mul: Number) = Vector(x * mul.toDouble(), y * mul.toDouble(), z * mul.toDouble())
    inline operator fun div(mul: Number) = Vector(x / mul.toDouble(), y / mul.toDouble(), z / mul.toDouble())

    inline infix fun dot(other: Vector) = x * other.x + y * other.y + z * other.z
    inline infix fun cross(other: Vector) = Vector(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x)
    inline infix fun x(other: Vector) = Vector(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x)

    val norm = this dot this
    val magnitude = sqrt(norm)
    val normalized get() = this / magnitude
    inline fun unit() = normalized
}

inline operator fun Number.times(mul: Vector) = mul * this
