package com.hopsha.competitionsaber.model.controller

import com.hopsha.competitionsaber.model.Engine

class ManualPlayerController : PlayerController {

    override suspend fun decide(vision: Vision, input: Engine.Input, state: PlayerState): Action {
        return when {
            input.forwardPressed -> Action.MOVE_FORWARD
            input.backPressed -> Action.MOVE_BACKWARD
            input.leftPressed -> Action.TURN_LEFT
            input.rightPressed -> Action.TURN_RIGHT
            input.spacePressed -> Action.ATTACK
            else -> Action.IDLE
        }
    }
}