package com.hopsha.competitionsaber.model.shape

import com.hopsha.competitionsaber.Angle
import com.hopsha.competitionsaber.cos
import com.hopsha.competitionsaber.model.findCollisions
import com.hopsha.competitionsaber.sin
import kotlin.math.pow

data class Circle(
    val x: Float,
    val y: Float,
    val radius: Float,
    override val isSolid: Boolean = true
) : Shape {

    val center = Point(x, y)

    val a: Float = -2 * x
    val b: Float = -2 * y
    val c: Float = x.pow(2) + y.pow(2) - radius.pow(2)

    fun bounds(segments: Int = 18): List<Point> {
        val step = 360f / segments
        return generateSequence(0f) { degrees -> degrees + step }
            .map { degrees -> Angle(degrees) }
            .map { angle -> Point(
                x + angle.cos * radius,
                y + angle.sin * radius,
            ) }
            .take(segments)
            .toList()
    }

    override fun contains(x: Float, y: Float): Boolean {
        return x * x + y * y < radius * radius
    }

    override fun findCollision(shape: Shape): List<Point> {
        return when(shape) {
            is StraightSegment -> findCollisions(shape, this)
            is Circle -> findCollisions(this, shape)
            is Rectangle -> findCollisions(this, shape)
            else -> throw NotImplementedError("Not implemented support for ${shape.javaClass.simpleName} collision in Circle")
        }
    }
}