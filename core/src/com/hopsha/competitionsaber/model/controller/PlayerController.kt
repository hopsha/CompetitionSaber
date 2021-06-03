package com.hopsha.competitionsaber.model.controller

import com.hopsha.competitionsaber.model.Engine

interface PlayerController {

    suspend fun decide(vision: Vision, input: Engine.Input, state: PlayerState): Action
}