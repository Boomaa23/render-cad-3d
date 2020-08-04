package com.boomaa.render3d.math.variable

import com.boomaa.render3d.math.MathUtil

@Deprecated(MathUtil.FIXED_MATH_REPLACE_MESSAGE)
open class Vec3d(var x: Double, var y: Double, var z: Double) : Vec(x, y, z) {
    fun toVec(): Vec {
        return this
    }
}