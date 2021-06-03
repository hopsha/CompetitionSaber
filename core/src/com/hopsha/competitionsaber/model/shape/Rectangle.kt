package com.hopsha.competitionsaber.model.shape

import com.badlogic.gdx.utils.Array
import com.hopsha.competitionsaber.growingRange
import com.hopsha.competitionsaber.model.findCollisions

data class Rectangle(
    val xStart: Float,
    val yStart: Float,
    val width: Float,
    val height: Float,
    override val isSolid: Boolean = true
): Shape {

    val xEnd = xStart + width
    val yEnd = yStart + height

    val perimeter: Array<StraightSegment> by lazy {
        Array.with(
            StraightSegment(xStart, yStart, xEnd, yStart),
            StraightSegment(xStart, yEnd, xEnd, yEnd),
            StraightSegment(xStart, yStart, xStart, yEnd),
            StraightSegment(xEnd, yStart, xEnd, yEnd),
        )
    }

    val bounds: List<Point> = listOf(
        Point(xStart, yStart),
        Point(xEnd, yStart),
        Point(xEnd, yEnd),
        Point(xStart, yEnd)
    )

    override fun contains(x: Float, y: Float): Boolean {
        return x in growingRange(xStart, xEnd) && y in growingRange(yStart, yEnd)
    }

    override fun findCollision(shape: Shape): List<Point> {
        return when(shape) {
            is StraightSegment -> findCollisions(shape, this)
            is Circle -> findCollisions(shape, this)
            is Rectangle -> findCollisions(this, shape)
            else -> throw NotImplementedError("Not implemented support for ${shape.javaClass.simpleName} collision in Circle")
        }
    }
}