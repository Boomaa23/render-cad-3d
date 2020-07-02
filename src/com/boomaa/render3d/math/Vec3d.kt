package com.boomaa.render3d.math

open class Vec3d(val x: Double, val y: Double, val z: Double) : Vec(x, y, z) {
    companion object Hats {
        fun iHat(): Vec3d {
            return Vec3d(1.0, 0.0, 0.0)
        }

        fun jHat(): Vec3d {
            return Vec3d(0.0, 1.0, 0.0)
        }

        fun kHat(): Vec3d {
            return Vec3d(0.0, 0.0, 1.0)
        }
    }
}