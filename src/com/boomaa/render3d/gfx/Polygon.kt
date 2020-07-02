package com.boomaa.render3d.gfx

import com.boomaa.render3d.math.Matrix
import com.boomaa.render3d.math.Vec
import java.awt.Color

class Polygon(var color: Color, var matrix: Matrix) {
    constructor(color: Color, vararg vectors: Vec) : this(color, Matrix(*vectors))
}