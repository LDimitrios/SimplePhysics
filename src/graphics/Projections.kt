package graphics

import mathematics.Quaternion
import mathematics.Vector
import other.map
import java.awt.Component
import java.awt.Graphics
import java.awt.event.*
import kotlin.math.PI
import kotlin.math.pow

interface Projection : MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
    fun drawSetup(g: Graphics)
    fun drawPoint(g: Graphics, point: Vector)
    fun drawLine(g: Graphics, v1: Vector, v2: Vector)
    fun addAsListenerTo(comp: Component) {
        comp.addMouseListener(this)
        comp.addMouseMotionListener(this)
        comp.addMouseWheelListener(this)
        comp.addKeyListener(this)
    }
}

abstract class AbstractRotationableCenterscalableLinepreservingProjection : Projection {
    val rotationSensitivity = .05
    val startTheta = PI / 6
    val scaleSensitivity = 1.1
    val startPPU = 100.0

    abstract fun pointOnScreen0(point: Vector): Pair<Double, Double>

    fun pointOnScreen(point: Vector): Pair<Int, Int> =
        pointOnScreen0((rotationQuaternion * point * rotationQuaternion.inverted).v)
            .map { (it * pixelsPerUnit).toInt() }

    override fun drawPoint(g: Graphics, point: Vector) = drawPoint(g, point, r = 3)

    fun drawPoint(g: Graphics, point: Vector, r: Int) {
        val (x, y) = pointOnScreen(point)
        g.drawOval(x - r, y - r, 2 * r + 1, 2 * r + 1)
    }

    override fun drawLine(g: Graphics, v1: Vector, v2: Vector) {
        val (x1, y1) = pointOnScreen(v1)
        val (x2, y2) = pointOnScreen(v2)
        g.drawLine(x1, y1, x2, y2)
    }

    val pointsOnAxis = 5
    val axisPointRadius = 1
    override fun drawSetup(g: Graphics) {
        val (xx, xy) = pointOnScreen(Vector(pointsOnAxis + .5, 0, 0))
        val (yx, yy) = pointOnScreen(Vector(0, pointsOnAxis + .5, 0))
        val (zx, zy) = pointOnScreen(Vector(0, 0, pointsOnAxis + .5))

        g.drawArrow(0, 0, xx, xy)
        g.drawArrow(0, 0, yx, yy)
        g.drawArrow(0, 0, zx, zy)

        for (i in 1..pointsOnAxis) {
            drawPoint(g, Vector(i, 0, 0), axisPointRadius)
            drawPoint(g, Vector(0, i, 0), axisPointRadius)
            drawPoint(g, Vector(0, 0, i), axisPointRadius)
        }
    }

    //--------- Rotation with mouse dragging ---------

    var phi = 0.0
    var theta = startTheta
    var rotationQuaternion = Quaternion.ofSpherical(phi, theta)

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
        rotationQuaternion = Quaternion.ofSpherical(phi, theta)
        xprev = e.x
        yprev = e.y
    }

    override fun mouseReleased(e: MouseEvent) {
        xprev = -1
        yprev = -1
    }

    //--------- Scaling with mouse wheel rotating ---------

    var scaleOrder = 0.0
    var pixelsPerUnit = startPPU


    override fun mouseWheelMoved(e: MouseWheelEvent) {
        scaleOrder += e.preciseWheelRotation
        pixelsPerUnit = startPPU * scaleSensitivity.pow(scaleOrder)
    }


    //--------- Stubs ---------

    override fun mouseClicked(e: MouseEvent) = Unit

    override fun mousePressed(e: MouseEvent) = Unit

    override fun mouseEntered(e: MouseEvent) = Unit

    override fun mouseExited(e: MouseEvent) = Unit

    override fun mouseMoved(e: MouseEvent) = Unit

    override fun keyTyped(e: KeyEvent) = Unit

    override fun keyPressed(e: KeyEvent) = Unit

    override fun keyReleased(e: KeyEvent) = Unit
}

class OrthogonalRCsLpProjection : AbstractRotationableCenterscalableLinepreservingProjection() {
    override fun pointOnScreen0(point: Vector) = point.x to -point.y
}

class PerspectiveRCsLpProjection(val perspectiveCoeff: Double) :
    AbstractRotationableCenterscalableLinepreservingProjection() {
    override fun pointOnScreen0(point: Vector) =
        (point.x * perspectiveCoeff.pow(point.z)) to -(point.y * perspectiveCoeff.pow(point.z))
}