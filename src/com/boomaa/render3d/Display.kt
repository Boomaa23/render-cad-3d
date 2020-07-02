package com.boomaa.render3d

import com.boomaa.render3d.gfx.Polygon
import com.boomaa.render3d.math.Matrix
import com.boomaa.render3d.math.Vec
import java.awt.*
import java.awt.geom.Path2D
import java.util.*
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel


object Display: JFrame("CAD Renderer 3D") {
    val poly = ArrayList<Polygon>()
    var heading = 0.0
    var pitch = 0.0

    @JvmStatic
    fun main(args: Array<String>) {
        poly.add(
            Polygon(
                Color.WHITE,
                Vec(100.0, 100.0, 100.0),
                Vec(-100.0, -100.0, 100.0),
                Vec(-100.0, 100.0, -100.0)

            )
        )
        poly.add(
            Polygon(
                Color.RED,
                Vec(100.0, 100.0, 100.0),
                Vec(-100.0, -100.0, 100.0),
                Vec(100.0, -100.0, -100.0)

            )
        )
        poly.add(
            Polygon(
                Color.GREEN,
                Vec(-100.0, 100.0, -100.0),
                Vec(100.0, -100.0, -100.0),
                Vec(100.0, 100.0, 100.0)

            )
        )
        poly.add(
            Polygon(
                Color.BLUE,
                Vec(-100.0, 100.0, -100.0),
                Vec(100.0, -100.0, -100.0),
                Vec(-100.0, -100.0, 100.0)
            )
        )

        val renderPanel: JPanel = object : JPanel() {
            override fun paintComponent(g: Graphics) {
                val g2 = g as Graphics2D
                g2.color = Color.BLACK
                g2.fillRect(0, 0, width, height)
                render(g2)
            }
        }

        val btn = JButton("ROTATE")
        btn.addActionListener { super.repaint() }

        super.getContentPane().layout = BorderLayout()
        super.getContentPane().add(btn)
        super.getContentPane().add(renderPanel)
        super.setSize(400, 400)
        super.setVisible(true)
    }

    //TODO make this work
    // ref http://blog.rogach.org/2015/08/how-to-create-your-own-simple-3d-render.html
    fun render(g2: Graphics2D) {
        heading++
        pitch++
        g2.translate(width / 2, height / 2)
        g2.color = Color.WHITE
        for (p in poly) {
            val path = Path2D.Double()
            val vx = getTransformedVec(p, 0)
            val vy = getTransformedVec(p, 1)
            val vz = getTransformedVec(p, 2)
            path.moveTo(vx.coords[0], vx.coords[1])
            path.lineTo(vy.coords[0], vy.coords[1])
            path.lineTo(vz.coords[0], vz.coords[1])
            path.closePath()
            g2.draw(path)
        }
    }

    private fun getTransformedVec(p: Polygon, n: Int): Vec {
        return Matrix.Rotation.xz(Math.toRadians(180.0)).vectorMultiply(p.matrix.getCol(n)!!)!!
//            .asMatrix().matrixMultiply(Matrix.Rotation.yz(pitch))!!.asVec()!!
    }
}