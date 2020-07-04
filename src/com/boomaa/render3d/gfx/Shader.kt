package com.boomaa.render3d.gfx

import java.awt.Color
import kotlin.math.pow

object Shader {
    private const val SRGB_NUM = 2.4

    // Converts shades SRGB to SRGB via linear RGB
    fun getShade(color: Color, shade: Double): Color {
        return Color(
            applyColorShade(color.red, shade),
            applyColorShade(color.green, shade),
            applyColorShade(color.blue, shade)
        )
    }

    private fun applyColorShade(value: Int, shade: Double): Int {
        return (value.toDouble().pow(SRGB_NUM) * shade).pow(1 / SRGB_NUM).toInt()
    }
}