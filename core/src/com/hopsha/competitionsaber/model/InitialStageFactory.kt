package com.hopsha.competitionsaber.model

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.Array
import com.hopsha.competitionsaber.Angle
import com.hopsha.competitionsaber.degrees
import com.hopsha.competitionsaber.model.controller.AutoPlayerController
import com.hopsha.competitionsaber.model.controller.IdlePlayerController
import com.hopsha.competitionsaber.model.controller.ManualPlayerController
import com.hopsha.competitionsaber.model.controller.PlayerController
import com.hopsha.competitionsaber.model.entity.Entity
import com.hopsha.competitionsaber.model.entity.Saber
import com.hopsha.competitionsaber.model.entity.Wall
import com.hopsha.competitionsaber.model.entity.player.Player

class InitialStageFactory {

    fun create(width: Float, height: Float): Stage {
        return Stage(width, height).apply {
            val perimeter = Wall.createPerimeter(
                "parimeter",
                0f,
                0f,
                width,
                height,
                5f
            )
            addEntities(perimeter)

            addPlayerWithSaber(
                "player1",
                Color.CYAN,
                "Sergey",
                50f,
                100f,
                angle = (30).degrees
            )

            addPlayerWithSaber(
                "player2",
                Color.BLUE,
                "Dennis",
                750f,
                100f,
                angle = (150).degrees
            )

            addPlayerWithSaber(
                "player3",
                Color.ORANGE,
                "Alex",
                750f,
                380f,
                angle = (-150).degrees
            )

            addPlayerWithSaber(
                "player4",
                Color.GREEN,
                "Tom",
                50f,
                380f,
                angle = (-30).degrees
            )
        }
    }

    private fun Stage.addPlayerWithSaber(
        id: String,
        color: Color,
        name: String,
        x: Float,
        y: Float,
        angle: Angle,
        controller: PlayerController = AutoPlayerController()
    ) {
        val saberPlayer1 = Saber(
            "${id}_saber",
            id,
        )
        addEntity(saberPlayer1)
        val player1 = Player(
            id,
            color,
            name,
            saberPlayer1,
            controller
        ).apply {
            this.x = x
            this.y = y
            this.angle = angle
        }
        addEntity(player1)
        with(saberPlayer1) {
            this.x = player1.x
            this.y = player1.y
            this.angle = player1.angle
        }
    }
}