package com.boomaa.render3d.math

import java.lang.IndexOutOfBoundsException
import kotlin.math.sqrt

open class Vec(vararg val coords: Double) {
    fun magnitude(): Double {
        var result = 0.0
        for (coord in coords) {
            result += (coord * coord)
        }
        return sqrt(result)
    }

    fun dotProduct(other: Vec): Double {
        if (this.coords.size != other.coords.size) {
            throw IndexOutOfBoundsException("Vectors are not in the same vector space")
        }
        var result = 0.0
        for (i in coords.indices) {
            result += (this.coords[i] * other.coords[i])
        }
        return result
    }
}