package com.boomaa.render3d.math

import kotlin.math.sqrt

open class Vec(vararg var coords: Double) {
    constructor(coords: List<Double>) : this(*coords.toDoubleArray())

    fun dimension(): Int {
        return coords.size
    }

    fun magnitude(): Double {
        var result = 0.0
        for (coord in coords) {
            result += (coord * coord)
        }
        return sqrt(result)
    }
    
    fun dotProduct(other: Vec): Double? {
        if (this.dimension() != other.dimension()) {
            return null
        }
        var result = 0.0
        for (i in this.coords.indices) {
            result += (this.coords[i] * other.coords[i])
        }
        return result
    }

    // Cross product only exists for 3D and 7D (only 3D implemented)
    fun crossProduct(other: Vec): Vec? {
        if (this.dimension() != other.dimension() || this.dimension() != 3 || other.dimension() != 3) {
            return null
        }
        return Vec(
            (this.coords[1] * other.coords[2]) - (this.coords[2] * other.coords[1]),
            (this.coords[2] * other.coords[0]) - (this.coords[0] * other.coords[2]),
            (this.coords[0] * other.coords[1]) - (this.coords[1] * other.coords[0])
        )
    }

    fun scalarMultiply(scalar: Double): Vec {
        val vecBldr = Builder()
        for (i in coords.indices) {
            vecBldr.add(coords[i] * scalar)
        }
        return vecBldr.build()
    }

    fun toUnitVec(): Vec {
        val mag = magnitude()
        val vecBldr = Builder()
        for (i in coords.indices) {
            vecBldr.add(coords[i] / mag)
        }
        return vecBldr.build()
    }

    fun negate(): Vec {
        val vecBldr = Builder()
        for (coord in coords) {
            vecBldr.add(-coord)
        }
        return vecBldr.build()
    }

    fun add(other: Vec): Vec? {
        if (this.dimension() == other.dimension()) {
            val vecBldr = Builder()
            for (i in this.coords.indices) {
                vecBldr.add(this.coords[i] + other.coords[i])
            }
            return vecBldr.build()
        }
        return null
    }
    
    fun asMatrix(): Matrix {
        return Matrix(this)
    }

    class Builder : MathBuilder<Double, Vec>() {
        override fun build(): Vec {
            return Vec(super.values)
        }
    }

    override fun toString(): String {
        return "Vec: ${coords.toList()}"
    }
}