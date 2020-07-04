package com.boomaa.render3d.parser

import com.boomaa.render3d.gfx.Poly
import com.boomaa.render3d.gfx.Triangle
import com.boomaa.render3d.math.Vec
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.IndexOutOfBoundsException
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class STL(private var fileLoc: String) : InputFormat() {
    private val fileStrs = LinkedList<String>()

    init {
        if (fileLoc.toLowerCase().contains("https://")) {
            val output = fileLoc.substring(fileLoc.lastIndexOf('/') + 1)
            if (!File(output).exists()) {
                downloadFile(fileLoc, output)
            }
            fileLoc = output
        }

        if (fileLoc.toLowerCase().contains(".stl")) {
            val stlFile = File(fileLoc)
            stlFile.forEachLine { fileStrs.add(it.trim()) }
            val allStrs = fileStrs.toString()
            if (allStrs.contains("facet")) {
                parseASCII()
            } else {
                parseBinary(stlFile.toPath())
            }
        } else {
            throw IllegalArgumentException("The file at \"$fileLoc\" is not a valid STL file")
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
        val headerOffsetBytes = 80
        val readBytes = Files.readAllBytes(path)
        fileBytes.addAll(readBytes.slice(headerOffsetBytes until readBytes.size).toList())
        val numTris = fileBytes.getNextUInt32()
        val polyBldr = Poly.Builder()
        val triBldr = Triangle.Builder()
        val vecBldr = Vec.Builder()

        for (tri in 0 until numTris) {
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

    private fun downloadFile(url: String, outPath: String) {
        try {
            BufferedInputStream(URL(url).openStream()).use { input ->
                FileOutputStream(outPath).use { fileOutputStream ->
                    val dataBuffer = ByteArray(1024)
                    var bytesRead: Int
                    while (input.read(dataBuffer, 0, 1024).also { bytesRead = it } != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}