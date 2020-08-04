package com.boomaa.render3d.math.fixed

import kotlin.math.sqrt

open class FixedVec3d(var x: Double, var y: Double, var z: Double) {
    fun dimension(): Int {
        return 3
    }

    fun magnitude(): Double {
        return sqrt((x * x) + (y * y) + (z * z))
    }

    fun dotProduct(other: FixedVec3d): Double {
        return (this.x * other.x) + (this.y * other.y) + (this.z * other.z)
    }

    fun crossProduct(other: FixedVec3d): FixedVec3d {
        return FixedVec3d(
            (this.y * other.z) - (this.z * other.y),
            (this.z * other.x) - (this.x * other.z),
            (this.x * other.y) - (this.y * other.x)
        )
    }

    fun scalarMultiply(scalar: Double): FixedVec3d {
        return FixedVec3d(x * scalar, y * scalar, z * scalar)
    }

    fun toUnitVec(): FixedVec3d {
        val mag = magnitude()
        return FixedVec3d(x / mag, y / mag, z / mag)
    }

    fun negate(): FixedVec3d {
        return FixedVec3d(-x, -y, -z)
    }

    fun add(other: FixedVec3d): FixedVec3d {
        return FixedVec3d(this.x + other.x, this.y + other.y, this.z + other.z)
    }

    fun copy(): FixedVec3d {
        return FixedVec3d(x, y, z)
    }
}