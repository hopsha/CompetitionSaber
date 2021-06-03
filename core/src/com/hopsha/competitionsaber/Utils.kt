package com.hopsha.competitionsaber

import com.badlogic.gdx.math.Vector3
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

fun vector3(x: Float, y: Float, z: Float = 0f) = Vector3(x, y, z)

fun growingRange(start: Float, end: Float, eps: Float = EPS): ClosedRange<Float> {
    return (min(start, end) - eps)..(max(start, end) + EPS)
}

fun solveQuadraticEquation(a: Float, b: Float, c: Float): List<Float> {
    val determinant = b * b - 4f * a * c
    return when {
        determinant > 0 -> listOf(
            (-b + sqrt(determinant)) / (2 * a),
            (-b - sqrt(determinant)) / (2 * a)
        )
        determinant == 0f -> listOf(
            -b / (2 * a)
        )
        else -> {
            emptyList()
        }
    }
}

const val EPS = 0.005f