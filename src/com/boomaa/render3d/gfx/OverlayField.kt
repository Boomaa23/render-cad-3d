package com.boomaa.render3d.gfx

import java.awt.Color
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.JTextField

class OverlayField(hint: String, col: Int) : JTextField(hint, col), FocusListener {
    private val hint: String
    private var showingHint: Boolean

    init {
        super.setForeground(Color.GRAY)
        this.hint = hint
        showingHint = true
        super.addFocusListener(this)
    }

    fun reset() {
        super.setVisible(false)
        super.setText(hint)
        showingHint = true
        super.setVisible(true)
    }

    override fun focusGained(e: FocusEvent) {
        if (this.text.isEmpty()) {
            super.setText("")
            showingHint = false
        }
    }

    override fun focusLost(e: FocusEvent) {
        if (this.text.isEmpty()) {
            super.setText(hint)
            showingHint = true
        }
    }

    override fun getText(): String {
        return if (showingHint) "" else super.getText()
    }
}
