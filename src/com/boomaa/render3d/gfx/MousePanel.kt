package com.boomaa.render3d.gfx

import com.boomaa.render3d.Display
import java.awt.Point
import java.awt.event.*
import javax.swing.JPanel

open class MousePanel(benchmark: Boolean) : JPanel(), MouseMotionListener, MouseListener, MouseWheelListener {
    private var firstPoint: Point? = null
    private var accumHeader = 0.0
    private var accumPitch = 0.0
    var heading = 0.0
    var pitch = 0.0

    init {
        if (!benchmark) {
            super.addMouseMotionListener(this)
            super.addMouseListener(this)
            super.addMouseWheelListener(this)
        }
    }

    override fun mouseDragged(e: MouseEvent?) {
        //TODO implement rotation based on front facing axis
        heading = accumHeader + (((e?.locationOnScreen!!.x - firstPoint!!.x).toDouble() / 2.0) % 360)
        pitch = accumPitch + (-((e.locationOnScreen!!.y - firstPoint!!.y).toDouble() / 2.0) % 360)
        super.repaint()
    }

    override fun mouseMoved(e: MouseEvent?) {
    }

    override fun mouseReleased(e: MouseEvent?) {
        accumHeader = heading % 360
        accumPitch = pitch % 360
        firstPoint = null
    }

    override fun mousePressed(e: MouseEvent?) {
        firstPoint = e!!.locationOnScreen
    }

    override fun mouseClicked(e: MouseEvent?) {
        accumHeader = heading % 360
        accumPitch = pitch % 360
        firstPoint = null
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseExited(e: MouseEvent?) {
    }

    override fun mouseWheelMoved(e: MouseWheelEvent?) {
        if (e != null) {
            val tmpScl = Display.scale
            if (tmpScl - e.unitsToScroll >= 0) {
                Display.scale -= e.unitsToScroll
            }
            if (!Display.scaleManual) {
                Display.scaleManual = true
            }
            super.repaint()
        }
    }
}