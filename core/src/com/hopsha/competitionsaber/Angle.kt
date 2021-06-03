package com.hopsha.competitionsaber

import com.badlogic.gdx.math.MathUtils
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.min
import kotlin.random.Random

data class Angle(
    val degrees: Float
): Comparable<Angle> {

    operator fun plus(angle: Angle): Angle {
        return Angle(degrees + angle.degrees)
    }

    operator fun minus(angle: Angle): Angle {
        return Angle(degrees - angle.degrees)
    }

    operator fun div(divider: Int): Angle {
        return Angle(degrees / divider)
    }

    operator fun unaryMinus(): Angle {
        return Angle(-degrees)
    }

    val normalized: Angle
        get() = Angle(normalizeDegrees(degrees))

    companion object {
        fun radians(radians: Float): Angle {
            val degrees = radians * 180 / Math.PI
            return Angle(degrees.toFloat())
        }

        fun between(
            xStart: Float,
            yStart: Float,
            xEnd: Float,
            yEnd: Float
        ): Angle {
            val deltaY = yEnd - yStart
            val deltaX = xEnd - xStart
            val radians = atan2(deltaY, deltaX)
            return radians(radians)
        }

        fun random(): Angle = Angle(
            Random.nextInt(360).toFloat()
        )
    }

    override fun compareTo(other: Angle): Int {
        return normalizeDegrees(degrees).compareTo(normalizeDegrees(other.degrees))
    }
}

data class AngleRange(
    val start: Angle,
    val stop: Angle
) {

    val center: Angle = Angle(start.degrees + (stop.degrees - start.degrees) / 2)
    val width: Angle = start.closeDistance(stop)

    operator fun contains(angle: Angle): Boolean {
        return angle.degrees in start.degrees..stop.degrees
    }

    operator fun contains(range: AngleRange): Boolean {
        return range.start.degrees in start.degrees..stop.degrees
                && range.stop.degrees in start.degrees..stop.degrees
    }
}

fun AngleRange.subtract(range: AngleRange): List<AngleRange> {
    return when {
        range.contains(this) -> emptyList()
        this.contains(range) -> listOf(
            AngleRange(
                start,
                range.start
            ),
            AngleRange(
                range.stop,
                stop
            )
        )
        this.contains(range.start) -> listOf(
            AngleRange(
                start,
                range.start
            )
        )
        this.contains(range.stop) -> listOf(
            AngleRange(
                range.stop,
                stop
            )
        )
        else -> emptyList()
    }
}

fun Angle.closeDistance(angle: Angle): Angle {
    val deltaDegrees = abs(angle.degrees - degrees)
    val closeDistanceDegrees = min(
        deltaDegrees,
        360f - deltaDegrees
    )
    return Angle(closeDistanceDegrees)
}

fun Angle.signedCloseDistance(angle: Angle): Angle {
    val positiveDegrees = angle.degrees - degrees
    val negativeDegrees = ((angle.degrees - 360) - degrees).run {
        if (this < -360) {
            this + 720
        } else {
            this
        }
    }

    return if (abs(positiveDegrees) < abs(negativeDegrees)) {
        Angle(positiveDegrees)
    } else {
        Angle(negativeDegrees)
    }
}

fun range(one: Angle, another: Angle): AngleRange {
    return AngleRange(
        one,
        another
    )
}

fun angleRange(degreesOne: Float, degreesTwo: Float): AngleRange {
    return AngleRange(
        Angle(degreesOne),
        Angle(degreesTwo)
    )
}

val Float.degrees
    get() = Angle(this)

val Int.degrees
    get() = Angle(this.toFloat())

val Angle.sin: Float
    get() = MathUtils.sinDeg(degrees)

val Angle.cos: Float
    get() = MathUtils.cosDeg(degrees)

private fun normalizeDegrees(degrees: Float): Float {
    return when {
        degrees > 360 -> degrees - 360
        degrees < 0 -> 360 + degrees
        else -> degrees
    }
}