package com.hopsha.competitionsaber.model.shape

import com.hopsha.competitionsaber.growingRange
import com.hopsha.competitionsaber.model.findCollisions

data class StraightSegment(
    val xStart: Float,
    val yStart: Float,
    val xEnd: Float,
    val yEnd: Float,
    override val isSolid: Boolean = true
): Shape {

    val a: Float = yStart - yEnd
    val b: Float = xEnd - xStart
    val c: Float = xStart * yEnd - xEnd * yStart

    val angleCoefficients = AngleCoefficients(
        -a / b,
        -c / b
    )

    val xRange: ClosedRange<Float> = growingRange(xStart, xEnd)
    val yRange: ClosedRange<Float> = growingRange(yStart, yEnd)

    val bounds: List<Point> = listOf(
        Point(xStart, yStart),
        Point(xEnd, yEnd),
    )

    override fun contains(x: Float, y: Float): Boolean {
        return x in growingRange(xStart, xEnd) && y in growingRange(yStart, yEnd)
                && a * x + b * y + c == 0f
    }

    override fun findCollision(shape: Shape): List<Point> {
        return when(shape) {
            is StraightSegment -> findCollisions(this, shape)
            is Circle -> findCollisions(this, shape)
            is Rectangle -> findCollisions(this, shape)
            else -> throw NotImplementedError("Not implemented support for ${shape.javaClass.simpleName} collision in StraightSegment")
        }
    }

    fun isParallel(segment: StraightSegment): Boolean {
        return a == segment.a && b == segment.b && c == segment.c
    }

    val isVertical: Boolean = b == 0f
    val isHorizontal: Boolean = a == 0f

    data class AngleCoefficients(
        val k: Float,
        val b: Float
    )
}