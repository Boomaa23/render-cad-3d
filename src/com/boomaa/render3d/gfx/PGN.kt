package com.boomaa.render3d.gfx

import com.boomaa.render3d.Display
import com.boomaa.render3d.math.Vec
import java.awt.Color

class PGN {
    init {
        Display.triangles.add(
            Triangle(
                Color.WHITE,
                Vec(100.0, 100.0, 100.0),
                Vec(-100.0, -100.0, 100.0),
                Vec(-100.0, 100.0, -100.0)

            )
        )
        Display.triangles.add(
            Triangle(
                Color.RED,
                Vec(100.0, 100.0, 100.0),
                Vec(-100.0, -100.0, 100.0),
                Vec(100.0, -100.0, -100.0)

            )
        )
        Display.triangles.add(
            Triangle(
                Color.GREEN,
                Vec(-100.0, 100.0, -100.0),
                Vec(100.0, -100.0, -100.0),
                Vec(100.0, 100.0, 100.0)

            )
        )
        Display.triangles.add(
            Triangle(
                Color.BLUE,
                Vec(-100.0, 100.0, -100.0),
                Vec(100.0, -100.0, -100.0),
                Vec(-100.0, -100.0, 100.0)
            )
        )
    }
}