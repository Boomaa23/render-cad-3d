package com.boomaa.render3d.math.fixed

import kotlin.math.cos
import kotlin.math.sin

open class FixedMatrix3d(private vararg var points: Double) {
    constructor() : this(*DoubleArray(9))
    constructor(vararg vectors: FixedVec3d) : this(
        vectors[0].x, vectors[1].x, vectors[2].x,
        vectors[0].y, vectors[1].y, vectors[2].y,
        vectors[0].z, vectors[1].z, vectors[2].z
    )

    fun numCols(): Int {
        return 3
    }

    fun numRows(): Int {
        return 3
    }

    fun getCol(col: Int): FixedVec3d {
        return FixedVec3d(points[col], points[col + 3], points[col + 6])
    }

    fun getRow(row: Int): FixedVec3d {
        val rt = row * 3
        return FixedVec3d(points[rt], points[rt + 1], points[rt + 2])
    }

    fun scalarMultiply(scalar: Double): FixedMatrix3d {
        var tempPoints = points.clone()
        for (i in tempPoints.indices) {
            tempPoints[i] *= scalar
        }
        return FixedMatrix3d(*tempPoints)
    }

    fun vectorMultiply(vector: FixedVec3d): FixedVec3d {
        return FixedVec3d(
            getRow(0).dotProduct(vector),
            getRow(1).dotProduct(vector),
            getRow(2).dotProduct(vector)
        )
    }

    fun matrixMultiply(other: FixedMatrix3d): FixedMatrix3d {
        return FixedMatrix3d(
            getRow(0).dotProduct(other.getCol(0)),
            getRow(0).dotProduct(other.getCol(1)),
            getRow(0).dotProduct(other.getCol(2)),
            getRow(1).dotProduct(other.getCol(0)),
            getRow(1).dotProduct(other.getCol(1)),
            getRow(1).dotProduct(other.getCol(2)),
            getRow(2).dotProduct(other.getCol(0)),
            getRow(2).dotProduct(other.getCol(1)),
            getRow(2).dotProduct(other.getCol(2))
        )
    }

    fun add(vec: FixedVec3d): FixedMatrix3d {
        return FixedMatrix3d(
            points[0] + vec.x,
            points[1] + vec.x,
            points[2] + vec.x,
            points[3] + vec.y,
            points[4] + vec.y,
            points[5] + vec.y,
            points[6] + vec.z,
            points[7] + vec.z,
            points[8] + vec.z
        )
    }

    override fun toString(): String {
        return "Matrix: ${points.asList()}"
    }

    object Identity {
        fun get(): FixedMatrix3d {
            return FixedMatrix3d(
                1.0, 0.0, 0.0,
                0.0, 1.0, 0.0,
                0.0, 0.0, 1.0
            )
        }
    }

    object Rotation {
        fun xy(theta: Double): FixedMatrix3d {
            return FixedMatrix3d(
                cos(theta), -sin(theta), 0.0,
                sin(theta), cos(theta), 0.0,
                0.0, 0.0, 1.0
            )
        }

        fun yz(theta: Double): FixedMatrix3d {
            return FixedMatrix3d(
                1.0, 0.0, 0.0,
                0.0, cos(theta), sin(theta),
                0.0, -sin(theta), cos(theta)
            )
        }

        fun xz(theta: Double): FixedMatrix3d {
            return FixedMatrix3d(
                cos(theta), 0.0, -sin(theta),
                0.0, 1.0, 0.0,
                sin(theta), 0.0, cos(theta)
            )
        }
    }
}
