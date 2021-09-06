@file:Suppress("NOTHING_TO_INLINE")

package mathematics

import kotlin.math.*

operator fun Number.plus(v: Vector) = Quaternion(this.toDouble(), v)
operator fun Vector.plus(a: Number) = Quaternion(a.toDouble(), this)
operator fun Number.minus(v: Vector) = Quaternion(this.toDouble(), -v)
operator fun Vector.minus(a: Number) = Quaternion(-a.toDouble(), this)

class Quaternion(val a: Double, val v: Vector) {
    constructor(a: Number, v: Vector) : this(a.toDouble(), v)
    constructor(a: Number, x: Number, y: Number, z: Number) : this(a.toDouble(), Vector(x, y, z))

    companion object {
        fun ofAngle(angle: Double, axis: Vector) = cos(angle / 2) + axis.normalized * sin(angle / 2)
        private val HORIZONTAL_ROTATION_AXIS = Vector(1, 0, 0)
        private val VERTICAL_ROTATION_AXIS = Vector(0, 0, 1)
        fun ofSpherical(horiz: Double, vert: Double) =
            ofAngle(horiz, HORIZONTAL_ROTATION_AXIS) * ofAngle(vert, VERTICAL_ROTATION_AXIS)
    }

    operator fun unaryPlus() = this

    val negated by lazy { Quaternion(-a, -v) }
    inline operator fun unaryMinus() = negated

    inline operator fun plus(other: Quaternion) = Quaternion(a + other.a, v + other.v)
    inline operator fun minus(other: Quaternion) = Quaternion(a - other.a, v - other.v)


    val norm = a * a + v.norm
    val magnitude = sqrt(norm)
    val normalized by lazy { this / magnitude }
    val conjugated by lazy { Quaternion(a, -v) }
    val inverted by lazy { conjugated / norm }

    inline operator fun times(other: Quaternion) =
        Quaternion(a * other.a - (v dot other.v), a * other.v + v * other.a + (v x other.v))

    inline operator fun div(other: Quaternion) = this * other.inverted

    inline operator fun plus(other: Number) = Quaternion(a + other.toDouble(), v)
    inline operator fun minus(other: Number) = Quaternion(a - other.toDouble(), v)
    inline operator fun plus(other: Vector) = Quaternion(a, v + other)
    inline operator fun minus(other: Vector) = Quaternion(a, v - other)

    inline operator fun times(mul: Number) = Quaternion(a * mul.toDouble(), v * mul)
    inline operator fun div(mul: Number) = Quaternion(a / mul.toDouble(), v / mul)
    inline operator fun times(mul: Vector) = times(0 + v)
    inline operator fun div(mul: Vector) = div(0 + v)
}

inline operator fun Number.plus(other: Quaternion) = other + this
inline operator fun Number.minus(other: Quaternion) = -other + this
inline operator fun Vector.plus(other: Quaternion) = other + this
inline operator fun Vector.minus(other: Quaternion) = -other + this

inline operator fun Number.times(mul: Quaternion) = mul * this
inline operator fun Number.div(mul: Quaternion) = mul.inverted * this
inline operator fun Vector.times(mul: Quaternion) = (0 + this) * mul
inline operator fun Vector.div(mul: Quaternion) = (0 + this) / mul