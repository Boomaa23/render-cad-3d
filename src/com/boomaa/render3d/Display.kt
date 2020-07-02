package com.boomaa.render3d

import com.boomaa.render3d.gfx.MousePanel
import com.boomaa.render3d.gfx.PGN
import com.boomaa.render3d.gfx.Polygon
import com.boomaa.render3d.math.Matrix
import com.boomaa.render3d.math.Vec
import com.boomaa.render3d.util.ArrayUtils
import java.awt.*
import java.awt.geom.Path2D
import java.util.*
import javax.swing.JFrame


object Display: JFrame("CAD Renderer 3D") {
    val poly = ArrayList<Polygon>()
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
        PGN()
        super.getContentPane().layout = BorderLayout()
        super.getContentPane().add(renderPanel)
        super.setSize(400, 400)
        super.setVisible(true)
    }

    // ref http://blog.rogach.org/2015/08/how-to-create-your-own-simple-3d-render.html
    fun render(g2: Graphics2D) {
        g2.translate(width / 2, height / 2)
        g2.color = Color.WHITE
        for (p in poly) {
            val vx = applyRotation(p, 0)
            val vy = applyRotation(p, 1)
            val vz = applyRotation(p, 2)
            val path = Path2D.Double()
            path.moveTo(vx.coords[0], vx.coords[1])
            path.lineTo(vy.coords[0], vy.coords[1])
            path.lineTo(vz.coords[0], vz.coords[1])
            path.closePath()
            g2.draw(path)
        }
    }

    private fun applyRotation(p: Polygon, n: Int): Vec {
        return Matrix.Rotation.xz(Math.toRadians(renderPanel.heading))
            .matrixMultiply(Matrix.Rotation.yz(Math.toRadians(renderPanel.pitch)))!!
            .vectorMultiply(p.matrix.getCol(n)!!)!!
    }
}