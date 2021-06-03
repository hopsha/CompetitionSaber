package com.hopsha.competitionsaber.model

import com.badlogic.gdx.graphics.Color
import com.hopsha.competitionsaber.Angle
import com.hopsha.competitionsaber.degrees
import com.hopsha.competitionsaber.model.controller.AutoPlayerController
import com.hopsha.competitionsaber.model.controller.PlayerController
import com.hopsha.competitionsaber.model.controller.team.*
import com.hopsha.competitionsaber.model.entity.Saber
import com.hopsha.competitionsaber.model.entity.Wall
import com.hopsha.competitionsaber.model.entity.player.Player

class InitialStageFactory {

    fun create(width: Float, height: Float): Stage {
        return Stage(width, height).apply {
            val perimeter = Wall.createPerimeter(
                "perimeter",
                0f,
                0f,
                width,
                height,
                5f
            )
            addEntities(perimeter)

            addPlayerWithSaber(
                "alex",
                Color.CYAN,
                "Alex",
                50f,
                50f,
                angle = (-135).degrees,
                controller = AlexPlayerController()
            )

            addPlayerWithSaber(
                "alexey",
                Color.BLUE,
                "Alexey",
                400f,
                50f,
                angle = (-90).degrees,
                controller = AlexeyPlayerController()
            )

            addPlayerWithSaber(
                "artem",
                Color.ORANGE,
                "Artem",
                750f,
                50f,
                angle = (-45).degrees,
                controller = ArtemPlayerController()
            )

            addPlayerWithSaber(
                "ilya",
                Color.MAGENTA,
                "Ilya",
                750f,
                430f,
                angle = (45).degrees,
                controller = IlyaPlayerController()
            )

            addPlayerWithSaber(
                "maksim",
                Color.CORAL,
                "Maksim",
                400f,
                430f,
                angle = (90).degrees,
                controller = MaksimPlayerController()
            )

            addPlayerWithSaber(
                "victor",
                Color.LIME,
                "Victor",
                50f,
                430f,
                angle = (135).degrees,
                controller = VictorPlayerController()
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