package com.hopsha.competitionsaber

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Matrix4
import com.hopsha.competitionsaber.model.EffectRegistry
import com.hopsha.competitionsaber.model.Stage
import com.hopsha.competitionsaber.model.controller.Vision
import com.hopsha.competitionsaber.model.controller.freeRanges
import com.hopsha.competitionsaber.model.effect.Effect
import com.hopsha.competitionsaber.model.entity.Saber
import com.hopsha.competitionsaber.model.entity.Wall
import com.hopsha.competitionsaber.model.entity.player.Player
import com.hopsha.competitionsaber.model.shape.Point
import com.hopsha.competitionsaber.model.shape.StraightSegment
import com.hopsha.competitionsaber.model.shape.toVector3

class StageRenderer {

    private fun ShapeRenderer.drawTestVision(vision: Vision) {
        begin(ShapeRenderer.ShapeType.Filled)
        color = Color.WHITE
        rect(
            0f,
            0f,
            100f,
            100f
        )
        color = Color.BLACK
        circle(
            50f,
            100f,
            3f
        )
        end()

        vision.freeRanges
            .forEach { range ->
                begin(ShapeRenderer.ShapeType.Line)
                color = Color.GOLD
                line(
                    Point.polar(
                        50f,
                        100f,
                        30f,
                        (range.start - 90.degrees).normalized
                    ).toVector3(),
                    Point.polar(
                        50f,
                        100f,
                        30f,
                        (range.stop - 90.degrees).normalized
                    ).toVector3()
                )
                end()
            }
        vision.items.forEach { item ->
            begin(ShapeRenderer.ShapeType.Line)
            color = when(item.info) {
                is Vision.Info.Player -> Color.CYAN
                Vision.Info.Saber -> Color.RED
                Vision.Info.Wall -> WALL_COLOR
            }
            line(
                Point.polar(
                    50f,
                    100f,
                    normalizeDebugVisionDistance(item.distance),
                    (item.angleRange.start - 90.degrees).normalized
                ).toVector3(),
                Point.polar(
                    50f,
                    100f,
                    normalizeDebugVisionDistance(item.distance),
                    (item.angleRange.stop - 90.degrees).normalized
                ).toVector3()
            )
            end()
        }
    }

    private fun normalizeDebugVisionDistance(distance: Float): Float {
        return distance / Player.VISION_DISTANCE * 70f
    }

    fun render(shapeRenderer: ShapeRenderer, stage: Stage) {
        stage.debugVision?.let { vision ->
            shapeRenderer.drawTestVision(vision)
        }
        stage.entities.asIterable()
            .forEach { entity -> when(entity) {
                is Player -> shapeRenderer.drawPlayer(entity)
                is Wall -> shapeRenderer.drawWall(entity)
            } }
    }

    private fun ShapeRenderer.drawPlayer(player: Player) {
        if (!shouldDisplay(player)) {
            return
        }

        if (player.isAlive) {
            Gdx.gl.glEnable(GL20.GL_BLEND)
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
            begin(ShapeRenderer.ShapeType.Filled)
            color = Color.WHITE.withOpacity(0.2f)
            val start = player.angle.degrees - 45
            arc(
                player.x,
                player.y,
                Player.VISION_DISTANCE,
                start,
                90f
            )
            end()
            Gdx.gl.glDisable(GL20.GL_BLEND)
        }

        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        begin(ShapeRenderer.ShapeType.Filled)
        color = if (player.isAlive) {
            player.color
        } else {
            DEAD_COLOR
        }
        circle(player.x, player.y, 20f)
        if (player.isInPain) {
            color = PAIN_COLOR
            circle(player.x, player.y, 20f)
        }
        end()
        Gdx.gl.glDisable(GL20.GL_BLEND)

        Gdx.gl.glLineWidth(2f)
        begin(ShapeRenderer.ShapeType.Line)
        color = Color.WHITE
        line(
            vector3(
                player.x + player.angle.cos * 10f,
                player.y + player.angle.sin * 10f,
            ),
            vector3(
                player.x + player.angle.cos * 20f,
                player.y + player.angle.sin * 20f,
            )
        )
        end()
        Gdx.gl.glLineWidth(1f)

        if (player.isAlive && player.saber.isArmed) {
            drawSaber(player.saber, player)
        }

        begin(ShapeRenderer.ShapeType.Filled)
        drawHitBar(player)
        end()
    }

    private fun ShapeRenderer.drawHitBar(player: Player) {
        val height = 10f
        val width = 40f
        val indent = 3f
        val singleHitPointWidth = (width - (Player.MAX_HIT_POINTS - 1) * indent) / Player.MAX_HIT_POINTS
        val startX = player.x - 20f
        val startY = player.y + 20f + 10f + height
        for (hitPoint in 1..Player.MAX_HIT_POINTS) {
            val index = hitPoint - 1
            val isHealthy = hitPoint <= player.hitPoints
            color = if (isHealthy) {
                if (player.isInvulnerable) {
                    INVULNERABLE_HIT_POINT_COLOR
                } else {
                    HEALTHY_HIT_POINT_COLOR
                }
            } else {
                SICK_HIT_POINT_COLOR
            }
            val x = startX + singleHitPointWidth * index + indent * (index - 1)
            rect(x, startY, singleHitPointWidth, height)
        }
    }

    private fun ShapeRenderer.drawWall(wall: Wall) {
        begin(ShapeRenderer.ShapeType.Filled)
        color = WALL_COLOR
        rect(
            wall.startX,
            wall.startY,
            wall.width,
            wall.height
        )
        end()
    }

    private fun ShapeRenderer.drawSaber(saber: Saber, player: Player) {
        Gdx.gl.glLineWidth(3f)
        begin(ShapeRenderer.ShapeType.Line)
        color = player.color
        line(
            vector3(saber.x, saber.y),
            vector3(
                saber.x + saber.angle.cos * Saber.LENGTH,
                saber.y + saber.angle.sin * Saber.LENGTH,
            )
        )
        end()
        Gdx.gl.glLineWidth(1f)
    }

    private fun shouldDisplay(player: Player): Boolean {
        return player.isAlive || System.currentTimeMillis() - player.timeOfDeathMillis < DEAD_PLAYER_VISIBILITY_MILLIS
    }
}

private fun Color.withOpacity(opacity: Float): Color {
    return Color(r, g, b, opacity)
}

private val WALL_COLOR = Color.BROWN
private val HEALTHY_HIT_POINT_COLOR = Color.GREEN
private val INVULNERABLE_HIT_POINT_COLOR = Color.WHITE
private val SICK_HIT_POINT_COLOR = Color.RED
private val PAIN_COLOR = Color.RED.withOpacity(0.2f)
private val DEAD_COLOR = Color.GRAY
private const val DEAD_PLAYER_VISIBILITY_MILLIS = 1500L