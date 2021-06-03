package com.hopsha.competitionsaber.model.effect

enum class Effect(
    val durationMillis: Long
) {

    INVULNERABILITY(1000L),
    ATTACK(1000L),
    DISARM(2000L),
    PAIN(500L),
}