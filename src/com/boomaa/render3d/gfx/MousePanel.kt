package com.boomaa.render3d.gfx

import java.awt.Point
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import javax.swing.JPanel

open class MousePanel : JPanel(), MouseMotionListener, MouseListener {
    private var firstPoint: Point? = null
    private var accumHeader = 0.0
    private var accumPitch = 0.0
    var heading = 0.0
    var pitch = 0.0

    init {
        super.addMouseMotionListener(this)
        super.addMouseListener(this)
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
}