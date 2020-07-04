package com.boomaa.render3d.parser

import com.boomaa.render3d.gfx.Poly
import com.boomaa.render3d.gfx.Triangle
import com.boomaa.render3d.math.Vec
import java.awt.Color
import java.io.File
import java.lang.IllegalArgumentException
import java.util.*

class OBJ(fileLoc: String) : InputFormat() {
    private val vertices = LinkedList<Vec>()

    // ref https://en.wikipedia.org/wiki/Wavefront_.obj_file#File_format
    init {
        //TODO implement texture coordinates and lines
        File(fileLoc).forEachLine {
            val line = it.split(" ")
            if (line.isNotEmpty()) {
                when (line[0]) {
                    "v" -> vertices.add(vecFromLine(line))
                    "f" -> polygons.add(polyFromLine(line, vertices))
                }
            }
        }
    }

    private fun vecFromLine(line: List<String>): Vec {
        val vecBldr = Vec.Builder()
        for (i in 1 until line.size) {
            vecBldr.add(line[i].toDouble())
        }
        return vecBldr.build()
    }

    private fun polyFromLine(line: List<String>, vertices: List<Vec>): Poly {
        val polyBldr = Poly.Builder()
        //TODO make this work with more than just 4-gons and 3-gons, cleanup
        if (line.size in 4..5) {
            polyBldr.add(triFromFaceString(vertices, line, 1, 2, 3))
            if (line.size == 5) {
                polyBldr.add(triFromFaceString(vertices, line, 3, 4, 1))
            }
        } else {
            throw IllegalArgumentException("n-gons > 4 || < 2 sides not allowed")
        }
        return polyBldr.build()
    }

    private fun triFromFaceString(vertices: List<Vec>, line: List<String>, p1: Int, p2: Int, p3: Int): Triangle {
        return Triangle(
            Color.WHITE,
            vertices[sepFaceIndex(line[p1])],
            vertices[sepFaceIndex(line[p2])],
            vertices[sepFaceIndex(line[p3])]
        )
    }

    private fun sepFaceIndex(poly: String): Int {
        return poly.split('/')[0].toInt() - 1
    }
}