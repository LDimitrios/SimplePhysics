package graphics

import physics.path
import java.awt.Color
import java.awt.Graphics
import javax.swing.JPanel

class Canvas(val projection: Projection) : JPanel() {
    init{
        projection.addAsListenerTo(this)
    }
    override fun paint(g: Graphics) {
        g.color = Color.black
        g.fillRect(0, 0, width, height)
        g.translate(width / 2, height / 2)

        projection.drawSetup(g)

        g.color = Color.white
        for (i in 1 until path.size) {
            projection.drawLine(g, path[i - 1], path[i])
        }
        projection.drawPoint(g, path[0])
    }
}