package com.boomaa.render3d.parser

import com.boomaa.render3d.gfx.Poly
import com.boomaa.render3d.gfx.Triangle
import com.boomaa.render3d.math.Vec
import java.awt.Color
import java.io.File
import java.util.*
import kotlin.collections.HashMap

class OBJ(fileLoc: String) : InputFormat() {
    private val vertices = LinkedList<Vec>()
    private val materials = HashMap<String, Color>()

    init {
        var matTemp = ""
        val matFile = File(fileLoc.substring(0, fileLoc.lastIndexOf('.')) + ".mtl")
        if (matFile.exists()) {
            matFile.forEachLine {
                val line = it.split(" ").toTypedArray()
                if (line.isNotEmpty()) {
                    when (line[0]) {
                        "newmtl" -> matTemp = line[1]
                        "Kd" -> materials[matTemp] = matToColor(line)
                    }
                }
            }
        }
        var materialColor: Color? = Color.WHITE
        File(fileLoc).forEachLine {
            val line = it.split(" ")
            if (line.isNotEmpty()) {
                when (line[0]) {
                    "v" -> vertices.add(vecFromLine(line))
                    "f" -> polygons.add(polyFromLine(line, vertices, materialColor!!))
                    "usemtl" -> materialColor = if (matFile.exists()) materials[line[1]] else Color.WHITE
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

    private fun polyFromLine(line: List<String>, vertices: List<Vec>, color: Color): Poly {
        val polyBldr = Poly.Builder()
        var ctr = 1
        for (i in 0 until (line.size - 3)) {
            val last = if ((2 + ctr) >= line.size) 1 else (2 + ctr)
            polyBldr.add(triFromFaceString(vertices, line, color, ctr, 1 + ctr, last))
            ctr += 2
        }
        return polyBldr.build()
    }

    private fun triFromFaceString(vertices: List<Vec>, line: List<String>, color: Color, p1: Int, p2: Int, p3: Int): Triangle {
        return Triangle(
            color,
            vertices[sepFaceIndex(line[p1])],
            vertices[sepFaceIndex(line[p2])],
            vertices[sepFaceIndex(line[p3])]
        )
    }

    private fun sepFaceIndex(poly: String): Int {
        return poly.split('/')[0].toInt() - 1
    }

    private fun matToColor(line: Array<String>): Color {
        return Color((line[1].toDouble() * 255).toInt(), (line[2].toDouble() * 255).toInt(), (line[3].toDouble() * 255).toInt())
    }
}