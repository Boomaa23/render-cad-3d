package com.boomaa.render3d

import com.boomaa.render3d.gfx.MousePanel
import com.boomaa.render3d.gfx.Shader
import com.boomaa.render3d.gfx.Triangle
import com.boomaa.render3d.math.Matrix
import com.boomaa.render3d.math.Vec
import com.boomaa.render3d.parser.OBJ
import com.boomaa.render3d.parser.STL
import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.image.BufferedImage
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.util.*
import javax.swing.JFrame
import kotlin.math.*


object Display: JFrame("3D Model Renderer") {
    val triangles = ArrayList<Triangle>()
    var scale = 1.0
    val renderPanel: MousePanel = object : MousePanel() {
        override fun paintComponent(g: Graphics) {
            val g2 = g as Graphics2D
            g2.color = Color.BLACK
            g2.fillRect(0, 0, width, height)
            render(g2)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isEmpty()) {
            throw IllegalArgumentException("Must pass a model file and (optionally) a scale factor")
        }
        var inputFn = args[0]
        if (args.size >= 2 && args[1].isNotEmpty()) {
            scale = args[1].toDouble()
        }
        if (inputFn.toLowerCase().contains("https://")) {
            val output = inputFn.substring(inputFn.lastIndexOf('/') + 1)
            if (!File(output).exists()) {
                downloadFile(inputFn, output)
            }
            inputFn = output
        }

        when(val inputExt = inputFn.substring(inputFn.lastIndexOf('.') + 1).toLowerCase()) {
            "obj" -> OBJ(inputFn)
            "stl" -> STL(inputFn)
            else -> throw IllegalArgumentException("Invalid/Not Supported file extension type \"$inputExt\"")
        }.polygons.forEach { triangles.addAll(it.triangles) }

        this.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                if (Config.inDemo) {
                    File(Config.DEMO_MODEL.substring(Config.DEMO_MODEL.lastIndexOf('/') + 1)).delete()
                }
            }
        })

        super.getContentPane().layout = BorderLayout()
        super.getContentPane().add(renderPanel)
        super.setDefaultCloseOperation(EXIT_ON_CLOSE)
        super.setPreferredSize(Dimension(800, 800))
        super.pack()
        super.setLocationRelativeTo(null)
        super.setVisible(true)
    }

    fun render(g2: Graphics2D) {
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val zBuffer = DoubleArray(img.width * img.height)
        Arrays.fill(zBuffer, Double.NEGATIVE_INFINITY)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        for (t in triangles) {
            //TODO implement auto scaling such that the polygons fit inside the frame
            val v1 = applyRotation(t, 0, scale)
            val v2 = applyRotation(t, 1, scale)
            val v3 = applyRotation(t, 2, scale)

            val minX = max(0.0, ceil(coordExtrema(0, true, v1, v2, v3))).toInt()
            val maxX = min(img.width - 1.0, floor(coordExtrema(0, false, v1, v2, v3))).toInt()
            val minY = max(0.0, ceil(coordExtrema(1, true, v1, v2, v3))).toInt()
            val maxY = min(img.height - 1.0, floor(coordExtrema(1, false, v1, v2, v3))).toInt()

            val cross = v2.add(v1.negate())!!.crossProduct(v3.add(v1.negate())!!)!!.toUnitVec()
            val triangleArea: Double = (v1.coords[1] - v3.coords[1]) * (v2.coords[0] - v3.coords[0]) + (v2.coords[1] - v3.coords[1]) * (v3.coords[0] - v1.coords[0])

            for (y in minY..maxY) {
                for (x in minX..maxX) {
                    val b1 = ((y - v3.coords[1]) * (v2.coords[0] - v3.coords[0]) + (v2.coords[1] - v3.coords[1]) * (v3.coords[0] - x)) / triangleArea
                    val b2 = ((y - v1.coords[1]) * (v3.coords[0] - v1.coords[0]) + (v3.coords[1] - v1.coords[1]) * (v1.coords[0] - x)) / triangleArea
                    val b3 = ((y - v2.coords[1]) * (v1.coords[0] - v2.coords[0]) + (v1.coords[1] - v2.coords[1]) * (v2.coords[0] - x)) / triangleArea
                    val depth = (b1 * v1.coords[2]) + (b2 * v2.coords[2]) + (b3 * v3.coords[2])
                    val zIndex = (y * img.width) + x
                    if (b1 in 0.0..1.0 && b2 in 0.0..1.0 && b3 in 0.0..1.0) {
                        if (zBuffer[zIndex] < depth) {
                            img.setRGB(x, y, Shader.getShade(t.color, abs(cross.coords[2])).rgb)
                            zBuffer[zIndex] = depth
                        }
                    }
                }
            }
            g2.drawImage(img, 0, 0, null)
        }
    }

    private fun applyRotation(p: Triangle, n: Int, sc: Double): Vec {
        return Matrix.Rotation.xz(Math.toRadians(renderPanel.heading))
            .matrixMultiply(Matrix.Rotation.yz(Math.toRadians(renderPanel.pitch)))!!
            .matrixMultiply(Matrix.Identity.of(3).scalarMultiply(sc))!!
            .vectorMultiply(p.matrix.getCol(n)!!)!!
            .add(Vec(width / 2.0, height / 2.0, 0.0))!!
    }

    private fun coordExtrema(n: Int, min: Boolean, vararg vecs: Vec): Double {
        var out = 0.0
        for (vec in vecs) {
            if ((!min && vec.coords[n] > out) || (min && vec.coords[n] < out)) {
                out = vec.coords[n]
            }
        }
        return out
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