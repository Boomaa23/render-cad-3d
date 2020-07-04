package com.boomaa.render3d.gfx

import com.boomaa.render3d.math.MathBuilder

class Poly(var name: String, vararg var triangles: Triangle) {
    constructor(name: String, triangles: List<Triangle>) : this(name, *triangles.toTypedArray())

    class Builder(var name: String = "") : MathBuilder<Triangle, Poly>() {
        override fun build(): Poly {
            return Poly(name, values)
        }
    }
}