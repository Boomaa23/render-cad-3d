package com.boomaa.render3d.gfx

import com.boomaa.render3d.math.MathBuilder
import com.boomaa.render3d.math.fixed.FixedMatrix3d
import com.boomaa.render3d.math.fixed.FixedVec3d
import java.awt.Color

class Triangle(var color: Color = Color.WHITE, var matrix: FixedMatrix3d) {
    constructor(color: Color = Color.WHITE, vararg vectors: FixedVec3d) : this(color, FixedMatrix3d(*vectors))

    class Builder(private val color: Color = Color.WHITE) : MathBuilder<FixedVec3d, Triangle>() {
        override fun build(): Triangle {
            return Triangle(color, *values.toTypedArray())
        }
    }
}