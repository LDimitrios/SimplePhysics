package graphics

import mathematics.Vector
import java.awt.Graphics
import java.awt.event.*
import kotlin.math.PI

interface Projection : MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
    fun drawSetup(g: Graphics)
    fun drawPoint(g: Graphics, point: Vector)
    fun drawLine(g: Graphics, v1: Vector, v2: Vector)
}

interface LinePreservingProjection : Projection {
    fun pointOnScreen(point: Vector): Pair<Int, Int>
    override fun drawPoint(g: Graphics, point: Vector) {
        val r = 2
        val (x, y) = pointOnScreen(point)
        g.drawOval(x - r, y - r, 2 * r + 1, 2 * r + 1)
    }

    override fun drawLine(g: Graphics, v1: Vector, v2: Vector) {
        val (x1, y1) = pointOnScreen(v1)
        val (x2, y2) = pointOnScreen(v2)
        g.drawLine(x1, y1, x2, y2)
    }
}

const val rotationSensitivity = .05

abstract class SimpleRotationalProjection : LinePreservingProjection {
    var phi = 0.0
    var theta = PI / 6

    private var xprev = -1
    private var yprev = -1
    override fun mouseDragged(e: MouseEvent) {
        if (xprev == -1 && yprev == -1) return
        phi += (e.x - xprev) * rotationSensitivity
        theta = (theta + (e.y - yprev) * rotationSensitivity).run {
            when {
                this < -PI / 2 -> -PI / 2
                this > PI / 2 -> PI / 2
                else -> this
            }
        }
        xprev = e.x
        yprev = e.y
    }

    override fun mouseReleased(e: MouseEvent?) {
        xprev = -1
        yprev = -1
    }
}
