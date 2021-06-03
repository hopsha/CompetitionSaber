package com.hopsha.competitionsaber

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align
import com.hopsha.competitionsaber.model.ScoreRegistry
import com.hopsha.competitionsaber.model.Stage
import com.hopsha.competitionsaber.model.entity.player.Player

class StatsRenderer(
    private val xStart: Float,
    private val yStart: Float,
    private val width: Float,
    private val height: Float,
    private val scoreRegistry: ScoreRegistry,
    private val onChaos: () -> Unit = {},
    private val onStart: () -> Unit = {},
    private val onStop: () -> Unit = {}
) {

    private val font = BitmapFont()
    private val chaosButton = Button(
        xStart + INDENT_HORIZONTAL,
        yStart + height - INDENT_HORIZONTAL - HEIGHT_BUTTON,
        width - INDENT_HORIZONTAL * 2,
        HEIGHT_BUTTON,
        "Rotate randomly",
        onChaos
    )

    private val startStopButton = CheckedButton(
        xStart + INDENT_HORIZONTAL,
        yStart + height - INDENT_HORIZONTAL - HEIGHT_BUTTON - INDENT_HORIZONTAL - HEIGHT_BUTTON,
        width - INDENT_HORIZONTAL * 2,
        HEIGHT_BUTTON,
        { isChecked ->
            if (isChecked) {
                "Stop"
            } else {
                "Start"
            }
        },
        { isChecked ->
            if (isChecked) {
                onStart()
            } else {
                onStop()
            }
        }
    )

    fun render(shapeRenderer: ShapeRenderer, spriteBatch: SpriteBatch, mouseState: MouseState, remainingSeconds: Int, stage: Stage) {
        chaosButton.checkClick(mouseState)
        startStopButton.checkClick(mouseState)
        shapeRenderer.drawBackground()
        stage.entities
            .filterIsInstance<Player>()
            .sortedBy { scoreRegistry.get(it.id) }
            .forEachIndexed { index, player ->
                shapeRenderer.drawTableLine(spriteBatch, index, player)
            }
        shapeRenderer.drawButton(spriteBatch, chaosButton)
        shapeRenderer.drawButton(spriteBatch, startStopButton)

        spriteBatch.begin()
        font.draw(
            spriteBatch,
            remainingSeconds.toString(),
            xStart + INDENT_HORIZONTAL,
            yStart + height - INDENT_HORIZONTAL * 4 - HEIGHT_BUTTON * 2,
            0,
            remainingSeconds.toString().length,
            width - INDENT_HORIZONTAL * 2,
            Align.center,
            false
        )
        spriteBatch.end()
    }

    private fun ShapeRenderer.drawBackground() {
        begin(ShapeRenderer.ShapeType.Filled)
        color = COLOR_BACKGROUND
        rect(
            xStart,
            yStart,
            width,
            height
        )
        end()
    }

    private fun ShapeRenderer.drawButton(spriteBatch: SpriteBatch, button: Button) {
        if (button.isPressed) {
            begin(ShapeRenderer.ShapeType.Filled)
            color = COLOR_BUTTON_PRESSED
            rect(
                button.x,
                button.y,
                button.width,
                button.height
            )
            end()
        }
        spriteBatch.begin()
        font.draw(
            spriteBatch,
            button.text,
            button.x,
            button.centerY + 5f,
            0,
            button.text.length,
            button.width,
            Align.center,
            false
        )
        spriteBatch.end()
    }

    private fun ShapeRenderer.drawButton(spriteBatch: SpriteBatch, button: CheckedButton) {
        if (button.isPressed) {
            begin(ShapeRenderer.ShapeType.Filled)
            color = COLOR_BUTTON_PRESSED
            rect(
                button.x,
                button.y,
                button.width,
                button.height
            )
            end()
        }
        spriteBatch.begin()
        font.draw(
            spriteBatch,
            button.text,
            button.x,
            button.centerY + 5f,
            0,
            button.text.length,
            button.width,
            Align.center,
            false
        )
        spriteBatch.end()
    }

    private fun ShapeRenderer.drawTableLine(
        spriteBatch: SpriteBatch,
        index: Int,
        player: Player,
    ) {
        val topIndent = index * HEIGHT_TABLE_LINE
        val lineStart = yStart + topIndent
        val lineCenter = lineStart + HEIGHT_TABLE_LINE / 2
        begin(ShapeRenderer.ShapeType.Filled)
        color = player.color
        circle(
            xStart + INDENT_HORIZONTAL + RADIUS_CIRCLE,
            lineCenter,
            RADIUS_CIRCLE
        )
        end()

        spriteBatch.begin()
        font.draw(
            spriteBatch,
            player.name,
            xStart + INDENT_HORIZONTAL + 2 * RADIUS_CIRCLE + INDENT_HORIZONTAL,
            lineCenter + 5f,
            0,
            player.name.length,
            width - INDENT_HORIZONTAL * 4 + RADIUS_CIRCLE * 2 - WIDTH_SCORE,
            Align.left,
            false,
            "..."
        )

        font.draw(
            spriteBatch,
            scoreRegistry.get(player.id).toString(),
            xStart + width - INDENT_HORIZONTAL - WIDTH_SCORE,
            lineCenter + 5f,
            0,
            scoreRegistry.get(player.id).toString().length,
            WIDTH_SCORE,
            Align.center,
            false
        )

        spriteBatch.end()
    }

    private class CheckedButton(
        val x: Float,
        val y: Float,
        val width: Float,
        val height: Float,
        val textProvider: (isChecked: Boolean) -> String,
        val onClick: (isChecked: Boolean) -> Unit
    ) {

        val text: String
            get() = textProvider(isChecked)
        var isChecked: Boolean = false
        var isPressed: Boolean = false
        val centerY: Float = y + height / 2

        fun checkClick(mouseState: MouseState) {
            if (!isMouseOver(mouseState.x, mouseState.y)) {
                isPressed = false
                return
            }

            if (mouseState.pressed) {
                isPressed = true
            } else if (isPressed) {
                isPressed = false
                isChecked = !isChecked
                onClick(isChecked)
            }
        }

        private fun isMouseOver(mouseX: Float, mouseY: Float): Boolean {
            return mouseX in x..(x + width) && mouseY in y..(y + height)
        }
    }

    private class Button(
        val x: Float,
        val y: Float,
        val width: Float,
        val height: Float,
        val text: String,
        val onClick: () -> Unit
    ) {

        var isPressed: Boolean = false
        val centerY: Float = y + height / 2

        fun checkClick(mouseState: MouseState) {
            if (!isMouseOver(mouseState.x, mouseState.y)) {
                isPressed = false
                return
            }

            if (mouseState.pressed) {
                isPressed = true
            } else if (isPressed) {
                isPressed = false
                onClick()
            }
        }

        private fun isMouseOver(mouseX: Float, mouseY: Float): Boolean {
            return mouseX in x..(x + width) && mouseY in y..(y + height)
        }
    }

    data class MouseState(
        val x: Float,
        val y: Float,
        val pressed: Boolean
    )
}

private val COLOR_BACKGROUND = Color.DARK_GRAY
private val COLOR_BUTTON_PRESSED = Color.BLACK
private const val RADIUS_CIRCLE = 10f
private const val HEIGHT_TABLE_LINE = 50f
private const val HEIGHT_BUTTON = 30f
private const val INDENT_HORIZONTAL = 20f
private const val WIDTH_SCORE = 40f