package com.hopsha.competitionsaber.model.controller

import com.hopsha.competitionsaber.model.Engine

class IdlePlayerController : PlayerController {
    override suspend fun decide(vision: Vision, input: Engine.Input, state: PlayerState): Action {
        return Action.IDLE
    }
}