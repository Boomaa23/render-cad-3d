package com.boomaa.render3d.math

import java.util.*
import kotlin.math.min

open class Matrix(vararg val vectors: Vec) {
    constructor(vectors: List<Vec>) : this(*vectors.toTypedArray())

   fun cols() : Int {
       return vectors.size
   }

    fun scalarMultiply(scalar: Double) {

    }

    fun vectorMultiply(other: Matrix): Matrix {
        return Matrix()
    }

    fun transform(input: Vec): Vec {
        return Vec()
    }

    companion object Identity {
        fun getIdentity(n: Int): Matrix {
            val matBld = Builder()
            for (i in 0 until min(n, 3)) {

            }
            return matBld.build()
        }
    }

    class Builder {
        private val vectors = LinkedList<Vec>()

        fun add(input: Vec): Builder {
            this.vectors.add(input)
            return this
        }

        fun build(): Matrix {
            return Matrix(vectors)
        }
    }
}