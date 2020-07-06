package com.boomaa.render3d.math

import kotlin.collections.ArrayList

abstract class MathBuilder<K, V> {
    val values = ArrayList<K>()

    fun add(input: K): MathBuilder<K, V> {
        this.values.add(input)
        return this
    }

    fun add(input: Array<K>): MathBuilder<K, V> {
        input.forEach { this.values.add(it) }
        return this
    }

    fun clear(): MathBuilder<K, V> {
        values.clear()
        return this
    }

    abstract fun build(): V
}