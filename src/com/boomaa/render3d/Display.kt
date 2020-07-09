package com.boomaa.render3d

import com.boomaa.render3d.gfx.MousePanel
import com.boomaa.render3d.gfx.Shader
import com.boomaa.render3d.gfx.Triangle
import com.boomaa.render3d.math.MathUtil
import com.boomaa.render3d.math.Matrix
import com.boomaa.render3d.math.Vec3d
import com.boomaa.render3d.parser.OBJ
import com.boomaa.render3d.parser.STL
import java.awt.*
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
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
import javax.swing.JOptionPane
import kotlin.math.*
import kotlin.system.exitProcess

object Display: JFrame("3D Model Renderer") {
    private val initSize = Pair(800, 800)
    private var dist = DistExtrema()
    var scale: Double = 1.0
    private var autoScaleFactor: Double = 2.0 / 5.0
    var scaleManual = false
    private val triangles = ArrayList<Triangle>()
    private lateinit var renderPanel: MousePanel
    private var framesDisplayed = 0L
    private var accumDrawTime = 0L
    private var benchmark = false

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isEmpty()) {
            throwErrPopup("Must pass a model file and (optionally) a scale factor")
        }
        if (args.size >= 3) {
            benchmark = args[2].contentEquals("--benchmark")
        }
        this.renderPanel = object : MousePanel(benchmark) {
            override fun paintComponent(g: Graphics) {
                val g2 = g as Graphics2D
                g2.color = Color.BLACK
                g2.fillRect(0, 0, width, height)
                render(g2)
            }
        }

        var inputFn = args[0]
        if (args.size >= 2 && args[1].isNotEmpty() && !args[1].toLowerCase().contentEquals("auto")) {
            scale = args[1].toDouble()
            scaleManual = true
        }
        if (inputFn.toLowerCase().contains("https://")) {
            val output = inputFn.substring(inputFn.lastIndexOf('/') + 1)
            if (!File(output).exists()) {
                downloadFile(inputFn, output)
            }
            inputFn = output
        }

        if (!File(inputFn).exists()) {
            throwErrPopup("File $inputFn does not exist")
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
        this.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                if (!scaleManual) {
                    scale = 1.0
                    dist.isset = false
                }
                Display.repaint()
            }
        })

        super.getContentPane().layout = BorderLayout()
        super.getContentPane().add(renderPanel)
        super.setDefaultCloseOperation(EXIT_ON_CLOSE)
        super.setPreferredSize(Dimension(initSize.first, initSize.second))
        super.pack()
        super.setLocationRelativeTo(null)
        super.setVisible(true)

        if (benchmark) {
            benchmark()
        }
    }

    fun render(g2: Graphics2D) {
        val startDrawTime = System.currentTimeMillis()
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val zBuffer = DoubleArray(img.width * img.height)
        Arrays.fill(zBuffer, Double.NEGATIVE_INFINITY)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        val thisHeading = renderPanel.heading
        val thisPitch = renderPanel.pitch

        for (t in triangles) {
            val midPt = getMidpoint()
            val rotMatrix = Matrix.Rotation.xz(Math.toRadians(thisHeading))
                .matrixMultiply(Matrix.Rotation.yz(Math.toRadians(thisPitch)))!!
                .matrixMultiply(t.matrix)!!.scalarMultiply(scale).add(midPt.toVec())!!
            val v1: Vec3d = rotMatrix.getCol(0)!!.asVec3d()
            val v2: Vec3d = rotMatrix.getCol(1)!!.asVec3d()
            val v3: Vec3d = rotMatrix.getCol(2)!!.asVec3d()

            if (!dist.isset) {
                val currMax: Double = MathUtil.maxAbsCompare(
                    MathUtil.maxAbsCompare(v1.x, v2.x, v3.x) - midPt.x,
                    MathUtil.maxAbsCompare(v1.y, v2.y, v3.y) - midPt.y,
                    MathUtil.maxAbsCompare(v1.z, v2.z, v3.z) - midPt.z
                )
                if (dist.max < currMax) {
                    dist.max = currMax
                }
                if (currMax < dist.min) {
                    dist.min = currMax
                }
            }

            val minX = max(0.0, ceil(minOf(v1.x, v2.x, v3.x))).toInt()
            val maxX = min(img.width - 1.0, floor(maxOf(v1.x, v2.x, v3.x))).toInt()
            val minY = max(0.0, ceil(minOf(v1.y, v2.y, v3.y))).toInt()
            val maxY = min(img.height - 1.0, floor(maxOf(v1.y, v2.y, v3.y))).toInt()

            val cross = v2.add(v1.negate())!!.crossProduct(v3.add(v1.negate())!!)!!.toUnitVec().asVec3d()
            val triArea: Double = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x)

            for (y in minY..maxY) {
                for (x in minX..maxX) {
                    val b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triArea
                    val b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triArea
                    val b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triArea
                    val depth = (b1 * v1.z) + (b2 * v2.z) + (b3 * v3.z)
                    val zIndex = (y * img.width) + x
                    val zORange = 0.0..1.0
                    if (b1 in zORange && b2 in zORange && b3 in zORange) {
                        if (zBuffer[zIndex] < depth) {
                            img.setRGB(x, y, Shader.getShade(t.color, abs(cross.z)).rgb)
                            zBuffer[zIndex] = depth
                        }
                    }
                }
            }
        }

        if (!scaleManual && !dist.isset) {
            dist.isset = true
            scale = ((min(width.toDouble(), height.toDouble()) / (abs(dist.max) + abs(dist.min))) * (autoScaleFactor))
            super.repaint()
        } else {
            g2.drawImage(img, 0, 0, null)
        }
        if (benchmark) {
            framesDisplayed++
            g2.color = Color.WHITE
            g2.font = Font("Arial", Font.PLAIN, 20)

            accumDrawTime += System.currentTimeMillis() - startDrawTime
            g2.drawString("Avg FPS: ${(framesDisplayed / (accumDrawTime / 1000.0)).round(4)}", 10, 20)
        }
    }

    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }

    private fun getMidpoint(): Vec3d {
        return Vec3d(width / 2.0, height / 2.0, 0.0)
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

    fun benchmark(durationSec: Int = 20) {
        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() < (startTime + (durationSec * 1000L))) {
            renderPanel.heading = (renderPanel.heading + 0.000001) % 360
            renderPanel.pitch = (renderPanel.pitch + 0.000001) % 360
            super.repaint()
        }
        println("${framesDisplayed / durationSec.toDouble()} FPS (${framesDisplayed} frames in $durationSec seconds)")
        exitProcess(0)
    }

    private fun throwErrPopup(message: String) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE)
        throw IllegalArgumentException(message)
    }

    class DistExtrema {
        var min: Double = 0.0
        var max: Double = 0.0
        var isset: Boolean = false
    }
}