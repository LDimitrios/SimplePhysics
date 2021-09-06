package graphics

import java.awt.*
import java.awt.geom.AffineTransform
import javax.swing.JFrame
import javax.swing.WindowConstants
import kotlin.math.atan2
import kotlin.math.sqrt


private const val ARR_SIZE = 4

/*
 * Thanks to George_A from coderoad
 *
 * https://coderoad.ru/4112701/%D0%A0%D0%B8%D1%81%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5-%D0%BB%D0%B8%D0%BD%D0%B8%D0%B8-%D1%81%D0%BE-%D1%81%D1%82%D1%80%D0%B5%D0%BB%D0%BA%D0%BE%D0%B9-%D0%B2-Java
 */
fun Graphics.drawArrow(x1: Int, y1: Int, x2: Int, y2: Int) {
    val g = create() as Graphics2D
    val dx = (x2 - x1).toDouble()
    val dy = (y2 - y1).toDouble()
    val angle = atan2(dy, dx)
    val len = sqrt(dx * dx + dy * dy).toInt()
    val at = AffineTransform.getTranslateInstance(x1.toDouble(), y1.toDouble())
    at.concatenate(AffineTransform.getRotateInstance(angle))
    g.transform(at)

    // Draw horizontal arrow starting in (0, 0)
    g.drawLine(0, 0, len, 0)
    g.fillPolygon(intArrayOf(len, len - ARR_SIZE, len - ARR_SIZE, len), intArrayOf(0, -ARR_SIZE, ARR_SIZE, 0), 4)
}

fun canvasFrame(canvas:Component, title : String):JFrame {
    val frame = JFrame(title)
    frame.layout = GridLayout(1,1)
    frame.add(canvas)
    return frame
}

fun JFrame.showFrame() = also { isVisible = true }
fun JFrame.hideFrame() = also { isVisible = false }
fun JFrame.setDisposeOnClose() = also { defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE }
fun JFrame.setExitOnClose() = also { defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE }
fun JFrame.maximize() = also { extendedState = extendedState or Frame.MAXIMIZED_BOTH }
fun JFrame.unmaximize() = also { extendedState = extendedState and Frame.MAXIMIZED_BOTH.inv() }
