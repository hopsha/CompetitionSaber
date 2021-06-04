package com.hopsha.competitionsaber.model.controller.team

import com.hopsha.competitionsaber.degrees
import com.hopsha.competitionsaber.model.Engine
import com.hopsha.competitionsaber.model.controller.*
import com.hopsha.competitionsaber.model.entity.Saber
import kotlin.random.Random

class AlexPlayerController : PlayerController{

    private val isLeftAligned = Random.nextBoolean()
    private var countStepBack = 1000

    private var stepSometimesForvard = 2000
    private var stepSometimesForvardTIME = 100
    private fun clearflags(){
        stepSometimesForvard = 2000
        stepSometimesForvardTIME = 100
    }



    override suspend fun decide(vision: Vision, input: Engine.Input, state: PlayerState): Action {
        val closestPlayer = vision.findClosestPlayer()
        val largestFreeRange = vision.freeRanges
                .maxByOrNull { it.width }
        val closestItem = vision.items.minByOrNull { it.distance }
        val closestDistance = closestItem?.distance ?: Float.MAX_VALUE
        if(closestPlayer==null){
            countStepBack = 1000
        }
        stepSometimesForvard --
        if(stepSometimesForvardTIME<=0){
            clearflags()
        }
        return when {
            stepSometimesForvard <= 0 &&  stepSometimesForvardTIME>0-> {
                stepSometimesForvardTIME--
                return Action.MOVE_FORWARD}

            vision.items.isEmpty() -> Action.MOVE_BACKWARD
            shouldRandomlyTurnLeft() -> Action.TURN_LEFT
            shouldRandomlyTurnRight() -> Action.TURN_RIGHT

            closestPlayer != null -> {
                clearflags()

                val closestPlayerCenter = closestPlayer.angleRange.center
                when {
                    state.isAttacking && countStepBack > 0 -> {
                        countStepBack--
                        Action.MOVE_BACKWARD
                        }
                    !state.isAttacking && closestPlayer.distance < Saber.LENGTH -> Action.ATTACK
                    closestPlayer.angleRange.contains(0.degrees) -> Action.MOVE_FORWARD
                    closestPlayerCenter.degrees < 0 -> Action.TURN_RIGHT
                    closestPlayerCenter.degrees > 0 -> Action.TURN_LEFT
                    else -> Action.MOVE_BACKWARD
                }
            }
            largestFreeRange == null -> turnAlignedSide()
            largestFreeRange.width.degrees < 88 -> turnAlignedSide()
            largestFreeRange.center.degrees > 10 -> Action.TURN_LEFT
            largestFreeRange.center.degrees < -10 -> Action.TURN_RIGHT
            else -> Action.MOVE_BACKWARD//Action.MOVE_FORWARD
        }
    }

    private fun shouldRandomlyTurnLeft(): Boolean {
        return false
//        if (isLeftAligned && Random.nextInt() % 8 == 0) {
//
//            true
//        } else !isLeftAligned && Random.nextInt() % 10 == 0
    }

    private fun shouldRandomlyTurnRight(): Boolean {
        return false
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