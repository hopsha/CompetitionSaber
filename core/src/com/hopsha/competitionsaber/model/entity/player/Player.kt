package com.hopsha.competitionsaber.model.entity.player

import com.badlogic.gdx.graphics.Color
import com.hopsha.competitionsaber.Angle
import com.hopsha.competitionsaber.AngleRange
import com.hopsha.competitionsaber.degrees
import com.hopsha.competitionsaber.model.controller.IdlePlayerController
import com.hopsha.competitionsaber.model.controller.PlayerController
import com.hopsha.competitionsaber.model.entity.DynamicEntity
import com.hopsha.competitionsaber.model.entity.Saber
import com.hopsha.competitionsaber.model.shape.Circle
import com.hopsha.competitionsaber.model.shape.Shape
import com.hopsha.competitionsaber.range

class Player(
    override val id: String,
    val color: Color,
    val name: String,
    val saber: Saber,
    val controller: PlayerController = IdlePlayerController()
) : DynamicEntity {

    override var x: Float = 0f
        set(value) {
            field = value
            saber.x = value
        }
    override var y: Float = 0f
        set(value) {
            field = value
            saber.y = value
        }
    var angle: Angle = 0.degrees
        set(value) {
            field = value
            saber.angle = value
        }
    var hitPoints: Int = MAX_HIT_POINTS
    var isInvulnerable = false
    var isDisarmed = false
    var isInPain = false
    var timeOfDeathMillis = Long.MAX_VALUE
    val isAlive: Boolean
        get() = !isDead
    var isDead = false
        set(value) {
            field = value
            timeOfDeathMillis = System.currentTimeMillis()
        }

    override val shape: Shape
        get() = Circle(x, y, 20f)

    val visionAngleRange: AngleRange
        get() = range((angle - VISION_ANGLE / 2), (angle + VISION_ANGLE / 2))

    override val isActual: Boolean
        get() = isAlive

    companion object {
        const val MAX_HIT_POINTS = 3
        const val VISION_DISTANCE = 200f
        val VISION_ANGLE = 90.degrees
    }
}