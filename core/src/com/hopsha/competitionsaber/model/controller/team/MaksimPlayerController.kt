package com.hopsha.competitionsaber.model.controller.team

import com.hopsha.competitionsaber.model.Engine
import com.hopsha.competitionsaber.model.controller.Action
import com.hopsha.competitionsaber.model.controller.AutoPlayerController
import com.hopsha.competitionsaber.model.controller.PlayerState
import com.hopsha.competitionsaber.model.controller.Vision
import com.hopsha.competitionsaber.model.entity.player.Player

class MaksimPlayerController : AutoPlayerController() {
    override suspend fun decide(vision: Vision, input: Engine.Input, state: PlayerState): Action {
        val p = vision.findClosestPlayer()
        p?.let {
            it.info as Vision.Info.Player
            val degrees = it.angleRange.center.degrees
            return when {
                degrees < 0 -> Action.TURN_RIGHT
                degrees > 0 -> Action.TURN_LEFT
                else -> Action.ATTACK
            }
        }
        return Action.TURN_LEFT
    }

    private fun Vision.findClosestPlayer(): Vision.Item? {
        return items.asSequence()
                .filter { it.info is Vision.Info.Player }
                .minByOrNull { it.distance }
    }
}