package com.hopsha.competitionsaber.model.entity

import com.hopsha.competitionsaber.Angle
import com.hopsha.competitionsaber.cos
import com.hopsha.competitionsaber.degrees
import com.hopsha.competitionsaber.model.entity.player.Player
import com.hopsha.competitionsaber.model.shape.Shape
import com.hopsha.competitionsaber.model.shape.StraightSegment
import com.hopsha.competitionsaber.sin

class Saber(
    override val id: String,
    val ownerId: String
) : DynamicEntity {

    var isArmed = false

    override var x: Float = 0f
    override var y: Float = 0f
    var angle: Angle = 0.degrees

    override val shape: Shape
        get() = StraightSegment(
            x,
            y,
            x + angle.cos * LENGTH,
            y + angle.sin * LENGTH,
            isSolid = false
        )

    override val isActual: Boolean
        get() = isArmed

    companion object {
        const val LENGTH = 100f
    }
}

fun Saber.ownedBy(player: Player): Boolean {
    return ownerId == player.id
}