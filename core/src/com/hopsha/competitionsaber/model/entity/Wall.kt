package com.hopsha.competitionsaber.model.entity

import com.hopsha.competitionsaber.model.shape.Rectangle
import com.hopsha.competitionsaber.model.shape.Shape

class Wall(
    override val id: String,
    val startX: Float,
    val startY: Float,
    val width: Float,
    val height: Float
) : Entity {

    override val x: Float
        get() = startX

    override val y: Float
        get() = startY

    val endX: Float = startX + width
    val endY: Float = startY + height

    override val shape: Shape = Rectangle(x, y, width, height)

    override val isActual: Boolean
        get() = true

    companion object {
        fun createPerimeter(
            baseId: String,
            startX: Float,
            startY: Float,
            width: Float,
            height: Float,
            depth: Float
        ): List<Wall> {
            return listOf(
                Wall("${baseId}_top", startX, startY, width, depth),
                Wall("${baseId}_bottom", startX, height - depth, width, depth),
                Wall("${baseId}_left", startX, startY, depth, height),
                Wall("${baseId}_right", startX + width - depth, startY, depth, height),
            )
        }
    }
}