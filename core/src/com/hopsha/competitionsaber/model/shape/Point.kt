package com.hopsha.competitionsaber.model.shape

import com.badlogic.gdx.math.Vector3
import com.hopsha.competitionsaber.Angle
import com.hopsha.competitionsaber.cos
import com.hopsha.competitionsaber.sin
import com.hopsha.competitionsaber.vector3
import kotlin.math.sqrt

data class Point(
    val x: Float,
    val y: Float
) {

    companion object {
        fun polar(
            xCenter: Float,
            yCenter: Float,
            distance: Float,
            angle: Angle
        ): Point = Point(
            xCenter + distance * angle.cos,
            yCenter + distance * angle.sin
        )
    }
}

fun Point.distanceTo(point: Point): Float = distanceTo(point.x, point.y)

fun Point.distanceTo(x: Float, y: Float): Float {
    val deltaX = this.x - x
    val deltaY = this.y - y
    return sqrt(deltaX * deltaX + deltaY * deltaY)
}

fun Point.toVector3(): Vector3 {
    return vector3(x, y)
}