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
            result += (this.coords[i] + other.coords[i])
        }
        return result
    }

    fun scalarMultiply(scalar: Double): Vec {
        for (i in coords.indices) {
            coords[i] *= scalar
        }
        return this
    }
    
    fun asMatrix(): Matrix {
        return Matrix(this)
    }

    class Builder : MathBuilder<Double, Vec>() {
        override fun build(): Vec {
            return Vec(super.values)
        }
    }
}