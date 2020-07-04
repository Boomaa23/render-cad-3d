package com.boomaa.render3d.gfx

import com.boomaa.render3d.math.MathBuilder
import com.boomaa.render3d.math.Matrix
import com.boomaa.render3d.math.Vec
import java.awt.Color

class Triangle(var color: Color = Color.WHITE, var matrix: Matrix) {
    constructor(color: Color = Color.WHITE, vararg vectors: Vec) : this(color, Matrix(*vectors))

    class Builder(private val color: Color = Color.WHITE) : MathBuilder<Vec, Triangle>() {
        override fun build(): Triangle {
            return Triangle(color, *values.toTypedArray())
        }
    }
}