package com.boomaa.render3d

import com.boomaa.render3d.gfx.OverlayField
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.net.URI
import javax.swing.*

object Config : JFrame("3D Model Renderer") {
    const val DEMO_MODEL = "https://upload.wikimedia.org/wikipedia/commons/b/b1/Sphericon.stl"
    var inDemo = false

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isNotEmpty()) {
            Display.main(args)
        } else {
            val content = super.getContentPane()
            val modelPanel = JPanel()
            val scalePanel = JPanel()
            val scaleMiddle = JPanel()
            val scaleRight = JPanel()
            val buttonPanel = JPanel()

            content.layout = BoxLayout(content, BoxLayout.Y_AXIS)
            scaleMiddle.layout = BoxLayout(scaleMiddle, BoxLayout.Y_AXIS)
            scaleRight.layout = BoxLayout(scaleRight, BoxLayout.Y_AXIS)

            val modelFileIn = OverlayField("Filename/Path/URL", 10)
            val scaleIn = OverlayField("Auto", 4)

            val autoScale = JCheckBox()
            autoScale.addActionListener {
                if (autoScale.isSelected) {
                    scaleIn.hint = "Auto"
                    scaleIn.isEnabled = false
                } else {
                    scaleIn.hint = "1.0"
                    scaleIn.isEnabled = true
                }
                scaleIn.reset()
            }

            autoScale.isSelected = true
            scaleIn.isEnabled = false

            val launchBtn = JButton("Launch")
            launchBtn.addActionListener {
                if (!modelFileIn.text.contentEquals("")) {
                    super.setVisible(false)
                    Display.main(arrayOf(modelFileIn.text, scaleIn.text))
                }
            }
            val demoBtn = JButton("Demo")
            demoBtn.addActionListener {
                this.inDemo = true
                super.setVisible(false)
                Display.main(arrayOf(DEMO_MODEL))
            }

            val hrefPanel = JPanel()
            val href = JLabel("<html><a href=''>github.com/Boomaa23/render-model-3d</a></html>")
            href.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    Desktop.getDesktop().browse(URI("https://github.com/Boomaa23/render-model-3d"))
                }
            })
            hrefPanel.add(href)

            addToPanel(scaleMiddle, true, JLabel("Auto"), autoScale)
            addToPanel(scaleRight, true, JLabel("Manual"), scaleIn)
            addToPanel(modelPanel, false, JLabel("Model File: "), modelFileIn)
            addToPanel(scalePanel, false, JLabel("Scale: "), scaleMiddle, scaleRight)
            addToPanel(buttonPanel, false, demoBtn, launchBtn)

            addToPanel(content, true, JLabel("3D Model Renderer"), hrefPanel, modelPanel, scalePanel, buttonPanel)
            SwingUtilities.getRootPane(launchBtn).defaultButton = launchBtn

            super.setDefaultCloseOperation(EXIT_ON_CLOSE)
            super.setPreferredSize(Dimension(290, 200))
            super.setResizable(true)
            super.pack()
            super.setLocationRelativeTo(null)
            super.setVisible(true)
        }
    }

    private fun addToPanel(panel: Container, center: Boolean, vararg comps: JComponent) {
        for (comp in comps) {
            if (center) {
                comp.alignmentX = Component.CENTER_ALIGNMENT
            }
            panel.add(comp)
        }
    }
}