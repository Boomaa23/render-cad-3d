package com.boomaa.render3d.gfx

import java.awt.Point
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import javax.swing.JPanel

open class MousePanel : JPanel(), MouseMotionListener, MouseListener {
    private var firstPoint: Point? = null
    var heading = 0.0
    var pitch = 0.0

    init {
        super.addMouseMotionListener(this)
        super.addMouseListener(this)
    }

    override fun mouseDragged(e: MouseEvent?) {
        //TODO fix this calculation, doesn't work properly (maybe?)
        heading = (e?.locationOnScreen!!.x - firstPoint!!.x).toDouble()
        pitch = (e.locationOnScreen!!.y - firstPoint!!.y).toDouble()
        super.repaint()
    }

    override fun mouseMoved(e: MouseEvent?) {
    }

    override fun mouseReleased(e: MouseEvent?) {
        firstPoint = null
    }

    override fun mousePressed(e: MouseEvent?) {
        firstPoint = e!!.locationOnScreen
    }

    override fun mouseEntered(e: MouseEvent?) {
    }

    override fun mouseClicked(e: MouseEvent?) {
    }

    override fun mouseExited(e: MouseEvent?) {
    }
}