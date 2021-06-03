package com.hopsha.competitionsaber.model

import com.hopsha.competitionsaber.*
import com.hopsha.competitionsaber.model.controller.Action
import com.hopsha.competitionsaber.model.controller.Action.*
import com.hopsha.competitionsaber.model.controller.PlayerState
import com.hopsha.competitionsaber.model.controller.Vision
import com.hopsha.competitionsaber.model.effect.Effect
import com.hopsha.competitionsaber.model.entity.Entity
import com.hopsha.competitionsaber.model.entity.Saber
import com.hopsha.competitionsaber.model.entity.Wall
import com.hopsha.competitionsaber.model.entity.ownedBy
import com.hopsha.competitionsaber.model.entity.player.Player
import com.hopsha.competitionsaber.model.shape.*
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.system.measureTimeMillis

class Engine(
    private val stage: Stage,
    private val effectRegistry: EffectRegistry,
    private val scoreRegistry: ScoreRegistry,
    private val onSaber: () -> Unit = {},
    private val onAttack: () -> Unit = {},
    private val onKill: () -> Unit = {}
) {

    private val Stage.alivePlayerNumber: Int
        get() = entities
            .filterIsInstance<Player>()
            .filter { it.isAlive }
            .count()

    fun refresh(input: Input) = measureTimeMillis {
        //val testPlayer = stage.getEntity<Player>("player1")
        //stage.debugVision = createVision(testPlayer)

        stage.entities
            .filterIsInstance<Player>()
            .filter { player -> player.isAlive }
            .associateWith { player -> player.controller.decide(createVision(player), input, player.createState()) }
            .forEach { pair ->
                val player = pair.key
                val action = pair.value
                player.handle(action)

                refreshAttacked(player)
                refreshEffectsState(player)
            }
    }

    private fun Player.createState() = PlayerState(
        saber.isArmed,
        isInvulnerable,
        isDisarmed,
        isInPain
    )

    private fun normalizeRotationAngle(angle: Angle): Angle {
        return when {
            angle.degrees <= -180 -> Angle(angle.degrees + 360)
            angle.degrees > 180 -> Angle(angle.degrees - 360)
            else -> angle
        }
    }

    private fun Player.handle(action: Action) {
        when(action) {
            TURN_LEFT -> {
                angle = normalizeRotationAngle(angle + 2.degrees)
            }
            TURN_RIGHT -> {
                angle = normalizeRotationAngle(angle - 2.degrees)
            }
            MOVE_FORWARD -> {
                if (isMovementAllowed(this, angle)) {
                    x += angle.cos * 2
                    y += angle.sin * 2
                }
            }
            MOVE_BACKWARD -> {
                if (isMovementAllowed(this, normalizeRotationAngle(Angle(angle.degrees - 180)))) {
                    x -= angle.cos * 2
                    y -= angle.sin * 2
                }
            }
            ATTACK -> {
                if (!effectRegistry.has(this, Effect.DISARM)) {
                    effectRegistry.add(this, Effect.ATTACK)
                    effectRegistry.add(this, Effect.DISARM)
                }
            }
            IDLE -> {}
        }
    }

    private fun refreshAttacked(player: Player) {
        val attackingSaber = getAttackingSaber(player)
        if (attackingSaber != null && !effectRegistry.has(player, Effect.INVULNERABILITY)) {
            val wasAlive = player.isAlive

            player.hitPoints--
            effectRegistry.add(player, Effect.INVULNERABILITY)
            effectRegistry.add(player, Effect.PAIN)

            if (player.hitPoints <= 0) {
                player.isDead = true
                if (wasAlive) {
                    onKilled(player, killerId = attackingSaber.ownerId)
                }
            }
        }
    }

    private fun onKilled(victim: Player, killerId: String) {
        victim.saber.isArmed = false
        effectRegistry.remove(victim, Effect.ATTACK)
        val scores = if (stage.alivePlayerNumber > 1) {
            1
        } else {
            2
        }
        scoreRegistry.add(killerId, scores)
        onKill()
    }

    private fun refreshEffectsState(player: Player) {
        player.isInvulnerable = effectRegistry.has(player, Effect.INVULNERABILITY)

        val wasInPain = player.isInPain
        player.isInPain = effectRegistry.has(player, Effect.PAIN)
        if (!wasInPain && player.isInPain) {
            onAttack()
        }

        player.isDisarmed = effectRegistry.has(player, Effect.DISARM)

        val wasArmed = player.saber.isArmed
        player.saber.isArmed = effectRegistry.has(player, Effect.ATTACK)
        if (!wasArmed && player.saber.isArmed) {
            onSaber()
        }
    }

    private fun angleBetween(
        start: Point,
        end: Point
    ): Angle = angleBetween(
        start.x,
        start.y,
        end.x,
        end.y
    )

    private fun angleBetween(
        xStart: Float,
        yStart: Float,
        xEnd: Float,
        yEnd: Float
    ): Angle {
        val deltaY = yEnd - yStart
        val deltaX = xEnd - xStart
        val radians = atan2(deltaY, deltaX)
        return Angle.radians(radians)
    }

    private fun isMovementAllowed(player: Player, movementAngle: Angle): Boolean {
        return stage.entities
            .filterNot { entity -> entity == player }
            .filter { it.isActual }
            .map { entity -> entity.shape }
            .filter { shape -> shape.isSolid }
            .flatMap { shape -> shape.findCollision(player.shape) }
            .map { point -> angleBetween(
                player.x,
                player.y,
                point.x,
                point.y
            ) }
            .all { angle -> abs(movementAngle.degrees - angle.degrees) > 90 }
    }

    private fun getAttackingSaber(player: Player): Saber? {
        val playerShape = player.shape
        return stage.entities
            .filterIsInstance<Saber>()
            .filterNot { saber -> saber.ownedBy(player) }
            .filter { saber -> saber.isArmed }
            .firstOrNull { saber -> saber.shape.collidesWith(playerShape) }
    }

    private fun createVision(player: Player): Vision {
        val visionRangeCircle = Circle(
            player.x,
            player.y,
            radius = Player.VISION_DISTANCE
        )
        val visionLeftBorder = StraightSegment(
            player.x,
            player.y,
            player.x + Player.VISION_DISTANCE * (player.angle - Player.VISION_ANGLE / 2 + 1.degrees).normalized.cos,
            player.y + Player.VISION_DISTANCE * (player.angle - Player.VISION_ANGLE / 2 + 1.degrees).normalized.sin,
        )
        val visionRightBorder = StraightSegment(
            player.x,
            player.y,
            player.x + Player.VISION_DISTANCE * (player.angle + Player.VISION_ANGLE / 2 - 1.degrees).normalized.cos,
            player.y + Player.VISION_DISTANCE * (player.angle + Player.VISION_ANGLE / 2 - 1.degrees).normalized.sin,
        )
        val items = stage.entities
            .filter { entity -> entity != player && entity != player.saber }
            .filter { entity -> entity.isActual }
            .mapNotNull { entity -> createVisionItem(
                entity,
                player,
                visionRangeCircle,
                visionLeftBorder,
                visionRightBorder
            ) }
        return Vision(items)
    }

    private fun createVisionItem(
        entity: Entity,
        player: Player,
        visionRangeCircle: Circle,
        visionLeftBorder: StraightSegment,
        visionRightBorder: StraightSegment,
    ): Vision.Item? {
        val halfVisionAngle = Player.VISION_ANGLE / 2
        val entityShape = entity.shape
        val pointDistances = entityShape.universalBounds().asSequence()
            .plus(entityShape.findCollision(visionRangeCircle))
            .plus(entityShape.findCollision(visionLeftBorder))
            .plus(entityShape.findCollision(visionRightBorder))
            .mapNotNull { point ->
                val distance = point.distanceTo(player.x, player.y)
                if (distance > Player.VISION_DISTANCE + EPS || distance.isNaN()) {
                    return@mapNotNull null
                }
                val angle = angleBetween(
                    player.x,
                    player.y,
                    point.x,
                    point.y
                )
                val signedCloseAngleDistance = player.angle.signedCloseDistance(angle)
                if (abs(signedCloseAngleDistance.degrees) > halfVisionAngle.degrees) {
                    return@mapNotNull null
                }
                PolarDistance(distance, signedCloseAngleDistance)
            }.toList()
        if (pointDistances.isEmpty()) {
            return null
        }

        val minDistance = pointDistances.minOf { it.distance }
        val minAngle = pointDistances.minOf { it.angle.degrees }
        val maxAngle = pointDistances.maxOf { it.angle.degrees }


        return Vision.Item(
            entity.createVisionInfo(),
            minDistance,
            angleRange(minAngle, maxAngle)
        )
    }

    private fun Shape.universalBounds(): List<Point> {
        return when(this) {
            is Circle -> bounds()
            is Rectangle -> bounds
            is StraightSegment -> bounds
            else -> throw NotImplementedError("No support for ${this.javaClass}")
        }
    }

    private fun Shape.isDebugging(): Boolean {
        return this is StraightSegment
    }

    private fun Player.isDebugging(): Boolean {
        return id == "player1"
    }

    data class Input(
        val leftPressed: Boolean,
        val rightPressed: Boolean,
        val forwardPressed: Boolean,
        val backPressed: Boolean,
        val spacePressed: Boolean
    )

    private data class PolarDistance(
        val distance: Float,
        val angle: Angle
    )

    private fun Entity.createVisionInfo(): Vision.Info {
        return when(this) {
            is Saber -> Vision.Info.Saber
            is Wall -> Vision.Info.Wall
            is Player -> Vision.Info.Player(
                effectRegistry.has(this, Effect.ATTACK),
                isInvulnerable,
                hitPoints,
                angle
            )
            else -> throw NotImplementedError("No support for $this")
        }
    }

    fun turnAlivePlayersRandomly() {
        stage.entities
            .filterIsInstance<Player>()
            .filter { it.isAlive }
            .forEach { it.angle = Angle.random() }
    }
}