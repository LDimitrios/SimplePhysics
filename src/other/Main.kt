package other

import graphics.*
import mathematics.*
import physics.generatePath

fun main() {
    generatePath(Vector(-100,5, 0), .1, 10000, 1.0) {10 * it.unit() /it.norm}
    val canvas = Canvas(OrthogonalRCsLpProjection())
    canvasFrame(canvas, "Test Physics")
        .showFrame()
        .maximize()
        .setExitOnClose()
}