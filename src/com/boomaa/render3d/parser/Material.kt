package com.boomaa.render3d.parser

import java.awt.Color

class Material(val name: String = "") {
    var red: Double = 1.0
    var blue: Double = 1.0
    var green: Double = 1.0

    fun color(): Color {
        return Color((red * 255).toInt(), (blue * 255).toInt(), (green * 255).toInt())
    }
}