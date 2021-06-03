package com.hopsha.competitionsaber.model.shape

interface Shape {

    fun contains(x: Float, y: Float): Boolean

    fun contains(point: Point): Boolean {
        return contains(point.x, point.y)
    }

    fun findCollision(shape: Shape): List<Point>
    fun collidesWith(shape: Shape): Boolean = findCollision(shape).isNotEmpty()

    val isSolid: Boolean
}