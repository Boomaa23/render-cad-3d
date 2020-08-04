package com.boomaa.render3d.math.variable

import com.boomaa.render3d.math.MathBuilder
import com.boomaa.render3d.math.MathUtil
import kotlin.math.cos
import kotlin.math.sin

@Deprecated(MathUtil.FIXED_MATH_REPLACE_MESSAGE)
open class Matrix(private vararg var vectors: Vec) {
    constructor(vectors: List<Vec>) : this(*vectors.toTypedArray())

    fun numCols(): Int {
        return vectors.size
    }

    fun numRows(): Int? {
        return vectors[0].dimension()
    }

    fun getCol(n: Int): Vec? {
        if (n < numCols()) {
            return vectors[n]
        }
        return null
    }

    fun getCols(range: IntRange = vectors.indices): List<Vec> {
        return vectors.slice(range)
    }

    fun getRow(n: Int): Vec? {
        if (numRows() != null && n < numRows()!!) {
            val vecBldr = Vec.Builder()
            for (vec in vectors) {
                vecBldr.add(vec.coords[n])
            }
            return vecBldr.build()
        }
        return null
    }

    fun scalarMultiply(scalar: Double): Matrix {
        val matBldr = Builder()
        for (vector in vectors) {
            matBldr.add(vector.scalarMultiply(scalar))
        }
        return matBldr.build()
    }

    fun vectorMultiply(vector: Vec): Vec? {
        if (vector.dimension() == this.numCols() && this.numRows() != null) {
            val vecBldr = Vec.Builder()
            for (i in 0 until this.numRows()!!) {
                vecBldr.add(this.getRow(i)!!.dotProduct(vector)!!)
            }
            return vecBldr.build()
        }
        return null
    }

    fun matrixMultiply(matrix: Matrix): Matrix? {
        if (this.numCols() == matrix.numRows() && matrix.numCols() != 0 && this.numRows() != null) {
            val matBldr = Builder()
            for (colNum in 0 until matrix.numCols()) {
                val vecBldr = Vec.Builder()
                for (rowNum in 0 until this.numRows()!!) {
                    vecBldr.add(this.getRow(rowNum)!!.dotProduct(matrix.getCol(colNum)!!)!!)
                }
                matBldr.add(vecBldr.build())
            }
            return matBldr.build()
        }
        return null
    }

    fun add(vec: Vec): Matrix? {
        if (this.numRows() == vec.dimension()) {
            val matBldr = Builder()
            for (col in this.getCols()) {
                matBldr.add(col.add(vec)!!)
            }
            return matBldr.build()
        }
        return null
    }

    fun asVec(): Vec? {
        if (numCols() == 1) {
            return getCol(0)
        }
        return null
    }

    override fun toString(): String {
        return "Matrix: ${vectors.asList()}"
    }

    object Identity {
        fun of(n: Int): Matrix {
            val matBld = Builder()
            val holderArr = DoubleArray(n)
            for (i in 0 until n) {
                holderArr[i] = 1.0
                matBld.add(Vec(*holderArr))
                holderArr[i] = 0.0
            }
            return matBld.build()
        }
    }

    // Rotation only works for 3d
    object Rotation {
        fun xy(theta: Double): Matrix {
            return Matrix(
                Vec3d(cos(theta), -sin(theta), 0.0),
                Vec3d(sin(theta), cos(theta), 0.0),
                Vec3d(0.0, 0.0, 1.0)
            )
        }

        fun yz(theta: Double): Matrix {
            return Matrix(
                Vec3d(1.0, 0.0, 0.0),
                Vec3d(0.0, cos(theta), sin(theta)),
                Vec3d(0.0, -sin(theta), cos(theta))
            )
        }

        fun xz(theta: Double): Matrix {
            return Matrix(
                Vec3d(cos(theta), 0.0, -sin(theta)),
                Vec3d(0.0, 1.0, 0.0),
                Vec3d(sin(theta), 0.0, cos(theta))
            )
        }
    }

    class Builder : MathBuilder<Vec, Matrix>() {
        override fun build(): Matrix {
            return Matrix(super.values)
        }
    }
}
