package com.boomaa.render3d.util

import java.util.*

object ArrayUtils {
    fun dblToInt(dbl: DoubleArray): IntArray {
        val list = LinkedList<Int>()
        for (db in dbl) {
            list.add(db.toInt())
        }
        return list.toIntArray()
    }
}