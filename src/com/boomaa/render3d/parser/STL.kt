package com.boomaa.render3d.parser

import com.boomaa.render3d.gfx.Poly
import com.boomaa.render3d.gfx.Triangle
import com.boomaa.render3d.math.Vec
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.IndexOutOfBoundsException
import java.net.URL
import java.util.*

class STL(private var fileLoc: String) {
    val file = LinkedList<String>()
    val polygons = LinkedList<Poly>()

    init {
        if (fileLoc.toLowerCase().contains("https://")) {
            val output = fileLoc.substring(fileLoc.lastIndexOf('/') + 1)
            if (!File(output).exists()) {
                downloadFile(fileLoc, output)
            }
            fileLoc = output
        }
        if (fileLoc.toLowerCase().contains(".stl")) {
            File(fileLoc).forEachLine { file.add(it.trim()) }
            var polyBldr: Poly.Builder? = null
            var triBldr: Triangle.Builder? = null
            if (file.size == 0 || !file.toString().contains("solid")) {
                //TODO implement binary STL parsing
                throw IllegalArgumentException("STL file not in ASCII format (likely binary)")
            }
            for (line in file) {
                if (polyBldr == null) {
                    if (line.contains("solid") && !line.contains("endsolid")) {
                        val name = try {
                            line.substring(line.indexOf("solid") + 6)
                        } catch (ignored: IndexOutOfBoundsException) {
                            ""
                        }
                        polyBldr = Poly.Builder(name)
                    }
                    continue
                } else if (line.contains("vertex")) {
                    if (triBldr == null) {
                        triBldr = Triangle.Builder()
                    }
                    val ioSpcOne = line.indexOf("vertex") + 7
                    val ioSpcTwo = line.indexOf(" ", ioSpcOne + 1)
                    val ioSpcThree = line.indexOf(" ", ioSpcTwo + 1)
                    triBldr.add(Vec(
                        line.substring(ioSpcOne, ioSpcTwo).trim().toDouble(),
                        line.substring(ioSpcTwo, ioSpcThree).trim().toDouble(),
                        line.substring(ioSpcThree).trim().toDouble()
                    ))
                    if (triBldr.values.size == 3) {
                        polyBldr.add(triBldr.build())
                        triBldr = null
                    }
                } else if (line.contains("endsolid")) {
                    polygons.add(polyBldr.build())
                    polyBldr = null
                }
            }
        } else {
            throw IllegalArgumentException("File is not an STL file")
        }
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