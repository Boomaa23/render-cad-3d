package com.boomaa.render3d.parser

import com.boomaa.render3d.gfx.Poly
import com.boomaa.render3d.gfx.Triangle
import com.boomaa.render3d.math.Vec
import java.io.File
import java.lang.IndexOutOfBoundsException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class STL(fileLoc: String) : InputFormat() {
    private val fileStrs = LinkedList<String>()

    init {
        val stlFile = File(fileLoc)
        stlFile.forEachLine { fileStrs.add(it.trim()) }
        val allStrs = fileStrs.toString()
        if (allStrs.contains("facet")) {
            parseASCII()
        } else {
            parseBinary(stlFile.toPath())
        }
    }

    private fun parseASCII() {
        val polyBldr = Poly.Builder()
        val triBldr = Triangle.Builder()
        for (line in fileStrs) {
            if (line.contains("solid") && !line.contains("endsolid")) {
                polyBldr.name = try {
                    line.substring(line.indexOf("solid") + 6)
                } catch (ignored: IndexOutOfBoundsException) { "" }
            } else if (line.contains("vertex")) {
                val ioSpcOne = line.indexOf("vertex") + 7
                val ioSpcTwo = line.indexOf(" ", ioSpcOne + 1)
                val ioSpcThree = line.indexOf(" ", ioSpcTwo + 1)
                triBldr.add(
                    Vec(
                        line.substring(ioSpcOne, ioSpcTwo).trim().toDouble(),
                        line.substring(ioSpcTwo, ioSpcThree).trim().toDouble(),
                        line.substring(ioSpcThree).trim().toDouble()
                    )
                )
                if (triBldr.values.size == 3) {
                    polyBldr.add(triBldr.build())
                    triBldr.clear()
                }
            } else if (line.contains("endsolid")) {
                polygons.add(polyBldr.build())
                polyBldr.clear()
            }
        }
    }

    private fun parseBinary(path: Path) {
        val fileBytes = STLProcBA()
        val readBytes = Files.readAllBytes(path)
        fileBytes.addAll(readBytes.slice(80 until readBytes.size).toList()) // 80 byte header offset
        val polyBldr = Poly.Builder()
        val triBldr = Triangle.Builder()
        val vecBldr = Vec.Builder()

        for (tri in 0 until fileBytes.getNextUInt32()) {
            fileBytes.skipBytes(12)
            for (i in 0 until 3) {
                for (j in 0 until 3) {
                    vecBldr.add(fileBytes.getNextFloat32().toDouble())
                }
                triBldr.add(vecBldr.build())
                vecBldr.clear()
            }
            polyBldr.add(triBldr.build())
            triBldr.clear()
            fileBytes.skipBytes(2)
        }
        this.polygons.add(polyBldr.build())
    }
}