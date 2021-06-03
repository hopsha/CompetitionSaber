package com.hopsha.competitionsaber.model.controller

import com.hopsha.competitionsaber.degrees
import com.hopsha.competitionsaber.model.Engine
import com.hopsha.competitionsaber.model.entity.Saber
import kotlin.random.Random

open class AutoPlayerController : PlayerController {

    private val isLeftAligned = Random.nextBoolean()

    override suspend fun decide(vision: Vision, input: Engine.Input, state: PlayerState): Action {
        val closestPlayer = vision.findClosestPlayer()
        val largestFreeRange = vision.freeRanges
            .maxByOrNull { it.width }
        val closestItem = vision.items.minByOrNull { it.distance }
        val closestDistance = closestItem?.distance ?: Float.MAX_VALUE

        return when {
            state.isInPain && Random.nextBoolean() -> if (Random.nextBoolean()) {
                Action.MOVE_FORWARD
            } else {
                turnAlignedSide()
            }
            closestPlayer != null && closestPlayer.distance > 50f -> {
                val closestPlayerCenter = closestPlayer.angleRange.center
                val closestPlayerInfo = closestPlayer.info as Vision.Info.Player
                when {
                    closestPlayerInfo.isAttacking && closestPlayer.distance < Saber.LENGTH + 10f && Random.nextBoolean() -> Action.MOVE_BACKWARD
                    !state.isAttacking && closestPlayer.distance < Saber.LENGTH + 10f -> Action.ATTACK
                    closestPlayer.angleRange.contains(0.degrees) -> Action.MOVE_FORWARD
                    closestPlayerCenter.degrees < 0 -> Action.TURN_RIGHT
                    closestPlayerCenter.degrees > 0 -> Action.TURN_LEFT
                    else -> Action.MOVE_FORWARD
                }
            }
            largestFreeRange == null -> turnAlignedSide()
            shouldRandomlyTurnLeft() -> Action.TURN_LEFT
            shouldRandomlyTurnRight() -> Action.TURN_RIGHT
            closestDistance > 70f -> Action.MOVE_FORWARD
            largestFreeRange.width.degrees < 20 -> turnAlignedSide()
            largestFreeRange.center.degrees > 10 -> Action.TURN_LEFT
            largestFreeRange.center.degrees < -10 -> Action.TURN_RIGHT
            else -> Action.MOVE_FORWARD
        }
    }

    private fun shouldRandomlyTurnLeft(): Boolean {
        return if (isLeftAligned && Random.nextInt() % 8 == 0) {
            true
        } else !isLeftAligned && Random.nextInt() % 10 == 0
    }

    private fun shouldRandomlyTurnRight(): Boolean {
        return if (!isLeftAligned && Random.nextInt() % 10 == 0) {
            true
        } else isLeftAligned && Random.nextInt() % 8 == 0
    }

    private fun Vision.findClosestPlayer(): Vision.Item? {
        return items.asSequence()
            .filter { it.info is Vision.Info.Player }
            .minByOrNull { it.distance }
    }

    private fun turnAlignedSide(): Action = if (isLeftAligned) {
        Action.TURN_LEFT
    } else {
        Action.TURN_RIGHT
    }
}