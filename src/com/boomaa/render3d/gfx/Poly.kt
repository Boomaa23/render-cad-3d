package com.boomaa.render3d.gfx

import com.boomaa.render3d.math.MathBuilder
import com.boomaa.render3d.math.Matrix
import com.boomaa.render3d.math.Vec
import java.awt.Color

class Poly(var name: String, vararg var triangles: Triangle) {
    constructor(name: String, triangles: List<Triangle>) : this(name, *triangles.toTypedArray())

    class Builder(var name: String = "") : MathBuilder<Triangle, Poly>() {
        override fun build(): Poly {
            return Poly(name, values)
        }
    }
}