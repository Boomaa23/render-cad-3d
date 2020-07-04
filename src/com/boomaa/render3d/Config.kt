package com.boomaa.render3d

import com.boomaa.render3d.gfx.OverlayField
import java.awt.Desktop
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.net.URI
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.SwingUtilities

object Config : JFrame("3D Model Renderer") {
    const val DEMO_MODEL = "https://upload.wikimedia.org/wikipedia/commons/b/b1/Sphericon.stl"
    var inDemo = false

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.isNotEmpty()) {
            Display.main(args)
        } else {
            val content = super.getContentPane()
            content.layout = FlowLayout()

            val modelFileIn = OverlayField("Filename/Path/URL", 10)
            val scaleFactorIn = OverlayField("Scale", 6)

            val launchBtn = JButton("Launch")
            launchBtn.addActionListener {
                if (!modelFileIn.text.contentEquals("")) {
                    super.setVisible(false)
                    Display.main(arrayOf(modelFileIn.text, scaleFactorIn.text))
                }
            }
            val demoBtn = JButton("Demo")
            demoBtn.addActionListener {
                this.inDemo = true
                super.setVisible(false)
                Display.main(arrayOf(DEMO_MODEL, "0.25"))
            }
            val href = JLabel("<html><a href=''>github.com/Boomaa23/render-model-3d</a></html>")
            href.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    Desktop.getDesktop().browse(URI("https://github.com/Boomaa23/render-model-3d"))
                }
            })

            content.add(JLabel("3D Model Renderer"))
            content.add(href)
            content.add(modelFileIn)
            content.add(scaleFactorIn)
            content.add(demoBtn)
            content.add(launchBtn)
            SwingUtilities.getRootPane(launchBtn).defaultButton = launchBtn

            super.setDefaultCloseOperation(EXIT_ON_CLOSE)
            super.setPreferredSize(Dimension(260, 150))
            super.setResizable(false)
            super.pack()
            super.setLocationRelativeTo(null)
            super.setVisible(true)
        }
    }
}