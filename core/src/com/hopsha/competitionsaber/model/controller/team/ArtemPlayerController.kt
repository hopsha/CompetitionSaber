package com.hopsha.competitionsaber.model.controller.team

import com.hopsha.competitionsaber.degrees
import com.hopsha.competitionsaber.model.Engine
import com.hopsha.competitionsaber.model.controller.*
import com.hopsha.competitionsaber.model.entity.Saber
import kotlin.random.Random

class ArtemPlayerController : PlayerController {

    private val isLeftAligned = Random.nextBoolean()

    override suspend fun decide(vision: Vision, input: Engine.Input, state: PlayerState): Action {
        val players = vision.items.filter { it.info is Vision.Info.Player }
        val largestFreeRange = vision.freeRanges
            .maxByOrNull { it.width }
        val closestItem = vision.items.minByOrNull { it.distance }
        val closestDistance = closestItem?.distance ?: Float.MAX_VALUE

        return when {
            players.isNotEmpty() -> {
                val closestVulnerablePlayer = players.filter { !(it.info as Vision.Info.Player).isInvulnerable }
                    .minByOrNull { it.distance }

                if ((!state.isDisarmed || state.isAttacking) && closestVulnerablePlayer != null) {
                    val closestPlayerCenter = closestVulnerablePlayer.angleRange.center
                    when {
                        !state.isAttacking
                                && closestVulnerablePlayer.distance < Saber.LENGTH - PLAYER_RADIUS / 4f
                                && closestVulnerablePlayer.angleRange.contains(0.degrees) -> Action.ATTACK
                        closestVulnerablePlayer.angleRange.contains(0.degrees) -> Action.MOVE_FORWARD
                        closestPlayerCenter.degrees < 0 -> Action.TURN_RIGHT
                        closestPlayerCenter.degrees > 0 -> Action.TURN_LEFT
                        else -> Action.MOVE_FORWARD
                    }
                } else {
                    Action.MOVE_BACKWARD
                }
            }

            shouldRandomlyTurnLeft() -> Action.TURN_LEFT
            shouldRandomlyTurnRight() -> Action.TURN_RIGHT
            largestFreeRange == null -> turnAlignedSide()

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
    companion object {
        private const val PLAYER_RADIUS = 20
    }
}