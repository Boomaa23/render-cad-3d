package com.boomaa.render3d.gfx

import com.boomaa.render3d.Display
import com.boomaa.render3d.math.Vec
import java.awt.Color

class PGN {
    init {
        Display.poly.add(
            Poly(
                Color.WHITE,
                Vec(100.0, 100.0, 100.0),
                Vec(-100.0, -100.0, 100.0),
                Vec(-100.0, 100.0, -100.0)

            )
        )
        Display.poly.add(
            Poly(
                Color.RED,
                Vec(100.0, 100.0, 100.0),
                Vec(-100.0, -100.0, 100.0),
                Vec(100.0, -100.0, -100.0)

            )
        )
        Display.poly.add(
            Poly(
                Color.GREEN,
                Vec(-100.0, 100.0, -100.0),
                Vec(100.0, -100.0, -100.0),
                Vec(100.0, 100.0, 100.0)

            )
        )
        Display.poly.add(
            Poly(
                Color.BLUE,
                Vec(-100.0, 100.0, -100.0),
                Vec(100.0, -100.0, -100.0),
                Vec(-100.0, -100.0, 100.0)
            )
        )
    }
}