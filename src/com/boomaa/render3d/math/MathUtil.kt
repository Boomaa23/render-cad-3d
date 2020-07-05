package com.boomaa.render3d.math

import kotlin.math.abs

object MathUtil {
    fun maxAbsCompare(v1: Double, v2: Double, v3: Double): Double {
        return when (abs(v1).coerceAtLeast(abs(v2).coerceAtLeast(abs(v3)))) {
            abs(v1) -> v1
            abs(v2) -> v2
            abs(v3) -> v3
            else -> 0.0
        }
    }
}