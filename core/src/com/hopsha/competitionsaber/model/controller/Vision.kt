package com.hopsha.competitionsaber.model.controller

import com.hopsha.competitionsaber.Angle
import com.hopsha.competitionsaber.AngleRange
import com.hopsha.competitionsaber.degrees
import com.hopsha.competitionsaber.subtract

data class Vision(
    val items: List<Item>
) {

    data class Item(
        val info: Info,
        val distance: Float,
        val angleRange: AngleRange
    )

    sealed class Info {
        object Saber : Info()
        object Wall : Info()
        data class Player(
            val isAttacking: Boolean,
            val isInvulnerable: Boolean,
            val hitPoints: Int,
            val angle: Angle
        ) : Info()
    }
}

val Vision.freeRanges: List<AngleRange>
    get() {
        var freeRanges = listOf(AngleRange((-45).degrees, 45.degrees))
        items
            .map { it.angleRange }
            .forEach { itemRange ->
                freeRanges = freeRanges.flatMap { freeRange ->
                    freeRange.subtract(itemRange)
                }
            }
        return freeRanges
    }
